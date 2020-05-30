(ns bookings.bookings
  (:import
    (org.apache.kafka.streams.state QueryableStoreTypes)
    (org.apache.kafka.clients.producer ProducerRecord)))

(defn save! [bookings-producer topic booking]
  (println "saving booking" booking)
  (.send bookings-producer (ProducerRecord. topic booking)))


(defn get-all-from-store [bookings-stream]
  (println "123" (.all (.store (:stream bookings-stream))))
  (with-open [all (.all (.store (:stream bookings-stream)
                          "bookings-store"
                          (QueryableStoreTypes/keyValueStore)))]
    (doall (map (fn [x] (.value x))
             (iterator-seq all)))))
