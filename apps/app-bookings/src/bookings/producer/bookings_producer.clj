(ns bookings.producer.bookings-producer
  (:require [com.stuartsierra.component :as component])
  (:import [org.apache.kafka.clients.producer KafkaProducer]
           [java.util Properties]))

(defn ^KafkaProducer create-producer
  "Given properties, build a KafkaProducer"
  ([^Properties config]
   (KafkaProducer. (doto (Properties.)
                     (.putAll config)))))

(defrecord BookingsProducer [config producer]
  component/Lifecycle

  (start
    [component]

    (println "bookings producer config" config)
    (println ";; Starting the producer")
    (let [producer (create-producer config)]
      (assoc component :producer producer)))

  (stop
    [component]

    (println ";; Stopping producer")
    (.close producer)
    (assoc component :producer nil)))

