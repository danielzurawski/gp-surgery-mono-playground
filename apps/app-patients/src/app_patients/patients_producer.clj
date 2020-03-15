(ns app-patients.patients-producer
  (:require [jackdaw.client :as jc]
            [jackdaw.serdes.edn :refer [serde]]
            [jackdaw.admin :as ja]))

(def serdes
  {:key-serde (serde)
   :value-serde (serde)})

(def kafka-config
  {"application.id" "app-patients"
   "bootstrap.servers" "localhost:9092"
   "default.key.serde" "jackdaw.serdes.EdnSerde"
   "default.value.serde" "jackdaw.serdes.EdnSerde"
   "cache.max.bytes.buffering" "0"})

;; An admin client is needed to do things like create and delete topics
(def admin-client (ja/->AdminClient kafka-config))


(def patient-record-topic
  (merge {:topic-name "patient-record"
          :partition-count 1
          :replication-factor 1
          :topic-config {}}
         serdes))

(defn save-patient-record!
  "Publisher a message with latest patients details"
  [record]
  (let [message-id (rand-int 10000)]
    (with-open [patients-producer (jc/producer kafka-config serdes)]
      @(jc/produce! patients-producer patient-record-topic message-id record))))

(comment
  ;; create the "patient-record-topic"
  (ja/create-topics! admin-client [patient-record-topic])

  ;; example save-patient-record!
  (save-patient-record!
   {:name "Daniel Zurawski"
    :description "empty"
    :gender :M
    :origin {:country "UK"
             :city "London"}}))
