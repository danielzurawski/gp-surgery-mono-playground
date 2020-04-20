(defproject gp-surgery-mono-playground/app-api-gateway "0.0.1"
  :description "API Gateway service"

  :plugins [[lein-monolith "1.4.0"]]

  :monolith/inherit true

  :dependencies [[clj-http "3.10.1"]]

  :main gateway.main

  :uberjar-name "app-api-gateway.jar"

  :docker {:image-name "myregistry.example.org/app-api-gateway"
           :dockerfile "Dockerfile"
           :build-dir  "."}

  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [ring/ring-mock "0.3.2"]]
                   :plugins [[lein-ring "0.12.5"]]}})
