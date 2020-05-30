(ns bookings.main
  (:require
    [com.stuartsierra.component :as component]
    [bookings.server :as server]
    ;[bookings.consumer.bookings-stream :as bookings-stream]
    [bookings.producer.bookings-producer :as bookings-producer])
  (:gen-class))

(defn new-bookings-service-system []
  (component/system-map
    :kafka-config {"application.id"            "app-bookings"
                   "bootstrap.servers"         "localhost:9092"
                   "default.key.serde"         "jackdaw.serdes.EdnSerde"
                   "default.value.serde"       "jackdaw.serdes.EdnSerde"
                   "cache.max.bytes.buffering" "0"
                   "key.serializer"            "org.apache.kafka.common.serialization.StringSerializer"
                   "value.serializer"          "org.apache.kafka.common.serialization.StringSerializer"
                   "topic"                     "booking-record"
                   "store"                     "bookings-store"}
    #_:bookings-stream #_(component/using (bookings-stream/map->BookingsStream {})
                       {:config :kafka-config})

    :bookings-producer (component/using (bookings-producer/map->BookingsProducer {})
                         {:config :kafka-config})

    :web-server (component/using
                  (server/map->WebServer {:port 3000})
                  [:bookings-stream :bookings-producer :kafka-config])))

(defn -main [] (component/start (new-bookings-service-system)))
