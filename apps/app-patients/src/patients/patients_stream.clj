(ns patients.patients-stream
  (:require
   [mount.core :refer [defstate]]
   [jackdaw.serdes.edn :refer [serde]]
   [patients.patients-kafka-util :as p-kafka-util])

  (:import
   (org.apache.kafka.streams StreamsConfig KafkaStreams StreamsBuilder)
   (org.apache.kafka.common.serialization Serde Serdes Serializer)
   (org.apache.kafka.streams.kstream ValueMapper Consumed Materialized)
   (org.apache.kafka.streams.state QueryableStoreTypes)))

(def store-name "patients-store")

(defn create-patients-materialised-view []
  (let [builder (StreamsBuilder.)]
    (-> builder
        (.table "patient-record"
                (Consumed/with (serde) (serde))
                (Materialized/as store-name))
        (.toStream))
    builder))

(defn build-patients-stream [config]
  (KafkaStreams. (.build (create-patients-materialised-view))
                 (StreamsConfig. config)))

(defn create-patients-stream []
  (let [stream (build-patients-stream p-kafka-util/kafka-config)]
    (doto stream
      (.cleanUp)
      (.start)
      (.state))
    stream))


(defstate patients-stream
  :start (create-patients-stream)
  :stop (.close patients-stream))


(defn get-all-from-store []
  (with-open [all (.all (.store patients-stream
                                "patients-store"
                                (QueryableStoreTypes/keyValueStore)))]
    (doall (map (fn [x] (.value x))
                (iterator-seq all)))))
