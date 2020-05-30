(defproject gp-surgery-mono-playground/app-bookings "0.0.1"
  :description "Bookings service"

  :plugins [[lein-monolith "1.4.0"]]

  :monolith/inherit true

  :dependencies [[metosin/compojure-api "2.0.0-alpha30"]
                 [org.apache.kafka/kafka-streams "2.3.1"]
                 [org.apache.kafka/kafka-streams-test-utils "2.3.1"]
                 [com.stuartsierra/component "1.0.0"]
                 [fundingcircle/jackdaw "0.6.7"]]

  :main bookings.main

  :uberjar-name "app-bookings.jar"

  :docker {:image-name "myregistry.example.org/app-bookings"
           :dockerfile "Dockerfile"
           :build-dir  "."}

  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [ring/ring-mock "0.3.2"]]
                   :plugins [[lein-ring "0.12.5"]]}})
