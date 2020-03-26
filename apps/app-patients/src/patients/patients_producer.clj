(ns patients.patients-producer
  (:require [jackdaw.client :as jc]
            [patients.patients-kafka-util :as p-kafka-util]))

(defn save-patient-record!
  "Publisher a message with latest patients details"
  [record]
  (let [message-id (rand-int 10000)]
    (with-open [patients-producer (jc/producer p-kafka-util/kafka-config
                                               p-kafka-util/serdes)]
      @(jc/produce! patients-producer
                    p-kafka-util/patient-record-topic
                    message-id
                    record))))


(comment
  ;; example save-patient-record!
  (save-patient-record!
   {:name "Daniel Zurawski 2"
    :description "empty"
    :gender :M
    :origin {:country "UK"
             :city "London"}}))
