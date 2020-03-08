(defproject gp-surgery-mono-playground/app-api-gateway "0.0.1"
  :description "API gateway service"
  :monolith/inherit true

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [metosin/compojure-api "2.0.0-alpha30"]]

  :ring {:handler app-api-gateway.handler/app}
  :uberjar-name "app-api-gateway.jar"

  :docker {:image-name "myregistry.example.org/app-api-gateway"
           :dockerfile "Dockerfile"
           :build-dir  "."}

  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [ring/ring-mock "0.3.2"]]
                   :plugins [[lein-ring "0.12.5"]]}}

)
