(ns patients.patients-producer
  (:require [mount.core :refer [defstate]]
            [jackdaw.client :as jc]
            [patients.patients-kafka-util :as p-kafka-util]))

(defn create-patients-producer []
  (jc/producer p-kafka-util/kafka-config
               p-kafka-util/serdes))


(defstate patients-producer :start (create-patients-producer))


(defn produce! [record]
  (let [message-id (rand-int 10000)]
    @(jc/produce! patients-producer
                  p-kafka-util/patient-record-topic
                  message-id
                  record)))
