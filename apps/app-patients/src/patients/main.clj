(ns patients.main
  (:require
   [ring.adapter.jetty :as jetty]
   [clojure.tools.logging :as log]
   [patients.handler :as web-handler]
   [patients.patients-producer :as patients-producer]
   [patients.patients-consumer :as patients-consumer])
  (:gen-class))

(def state (atom {}))

(defn -main [& args]
  ;; (nrepl/start-server :port 3002 :bind "0.0.0.0")
  (log/info "Waiting for kafka to be ready")
  ;; (k/wait-for-kafka "kafka1" 9092)
  ;; (log/info "Waiting for topics to be created")
  ;; (k/wait-for-topic "share-holders")
  (Thread/sleep 5000)
  ;; (log/info "Starting Kafka Streams")
  (let [patients-stream (patients-consumer/init)
        web-api (web-handler/init patients-consumer/get-all-patients-local-store
                                  patients-producer/save-patient-record!
                                  patients-stream)]
    (reset! state {:kstream   patients-stream
                   :jetty     (jetty/run-jetty
                               web-api
                               {:port  3000
                                :join? false})})))
