(ns patients.patients-stream-test
  (:require
   [patients.patients-stream :as ps]
   [patients.patients-kafka-util :as p-kafka-util]
   [clojure.test :as t :refer [deftest is testing]]
   [jackdaw.test :as jd.test])
  (:import
   (java.util Properties)
   (org.apache.kafka.streams TopologyTestDriver)))

(defn props-for
  [x]
  (doto (Properties.)
    (.putAll (reduce-kv (fn [m k v] (assoc m (str k) (str v)))
                        {}
                        x))))

(defn mock-transport-config
  []
  {:driver (TopologyTestDriver.
            (.build (ps/create-patients-materialised-view))
            (props-for p-kafka-util/kafka-config))})

(defn test-transport
  []
  (jd.test/mock-transport (mock-transport-config)
                          {:input p-kafka-util/patient-record-topic
                           :output (assoc p-kafka-util/patient-record-topic
                                          :topic-name "app-patients-patients-store-changelog") })) ;; should this be patients-store or patient-record?

(defn get-last-matching-email-from-journal
  [journal patient-record]
  (println "all data in journal" (get-in journal [:topics :output]))
  (->> (get-in journal [:topics :output])
       (filter (fn [record] (= (:key record) (:email patient-record))))
       last
       :value))

(defn until-email-matches-fn
  [patient-record]
  (fn [journal]
    (some #(= (:email patient-record) (:key %))
          (get-in journal [:topics :output]))))

(deftest test-patients-stream
  (jd.test/with-test-machine
    (test-transport)
    (fn [machine]
      (let [patient-record {:email "other-email@gmail.com" :password "test123"}
            commands [[:write! :input patient-record {:key-fn #(:email %)}]
                      [:write! :input (assoc patient-record :email "zurawski.daniel@gmail.com") {:key-fn #(:email %)}]
                      [:watch (until-email-matches-fn patient-record) {:timeout 15000}]]
            {:keys [results journal]} (jd.test/run-test machine commands)]

        (is (every? #(= :ok (:status %)) results))

        (is (= "other-email@gmail.com" (:email (get-last-matching-email-from-journal journal patient-record))))

        ))))
