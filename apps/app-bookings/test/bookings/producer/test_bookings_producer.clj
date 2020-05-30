(ns bookings.producer.test-bookings-producer
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component])
  (:import [org.apache.kafka.clients.producer MockProducer]))

(defn ^MockProducer create-producer
  []
  "Returns a MockProducer"
  (MockProducer.))

(defrecord TestBookingsProducer [^MockProducer producer]
  component/Lifecycle

  (start
    [component]
    (println ";; Starting the producer")
    (let [producer (create-producer)]
      (assoc component :producer producer)))

  (stop
    [component]
    (println ";; Stopping producer")
    (.close producer)
    (assoc component :producer nil)))

