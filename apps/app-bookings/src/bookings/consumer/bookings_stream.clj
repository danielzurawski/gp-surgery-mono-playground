(ns bookings.consumer.bookings-stream
  (:require
    [jackdaw.serdes.edn :refer [serde]]
    [com.stuartsierra.component :as component])

  (:import
    (org.apache.kafka.streams StreamsConfig KafkaStreams StreamsBuilder)
    (org.apache.kafka.common.serialization Serde Serdes Serializer)
    (org.apache.kafka.streams.kstream ValueMapper Consumed Materialized)
    (org.apache.kafka.streams.state QueryableStoreTypes)))

(defn create-bookings-materialised-view [config]
  (let [builder (StreamsBuilder.)]
    (-> builder
      (.table (get config "topic")
        (Consumed/with (serde) (serde))
        (Materialized/as (get config "store")))
      (.toStream))
    builder))

(defn build-bookings-stream [config]
  (KafkaStreams. (.build (create-bookings-materialised-view config))
    (StreamsConfig. config)))

(defn create-bookings-stream [config]
  (let [stream (build-bookings-stream config)]
    (doto stream
      (.cleanUp)
      (.start)
      (.state))
    stream))

(defrecord BookingsStream [config stream]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start
    [component]
    (println "bookings stream config" config)
    (println ";; Starting stream")
    (let [stream (create-bookings-stream config)]
      (assoc component :stream stream)))

  (stop
    [component]

    (println ";; Stopping stream")
    (.close stream)
    (assoc component :stream nil)))


