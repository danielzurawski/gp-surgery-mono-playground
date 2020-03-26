(ns patients.patients-kafka-util
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

(comment
  ;; create the "patient-record-topic"
  (ja/create-topics! admin-client [patient-record-topic]))
