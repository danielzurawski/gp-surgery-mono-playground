(defproject gp-surgery-mono-playground/app-patients "0.0.1"
  :description "Patients service"
  :monolith/inherit true

  :dependencies
  [[commons-io "2.5"]
  ;  [example/lib-a "1.0.0"]
  ;  [example/lib-b "0.2.0"]
   [org.clojure/clojure "1.8.0"]]

  :uberjar-name "app-patients.jar"
  :docker {:image-name "myregistry.example.org/app-patients"
           :dockerfile "Dockerfile"
           :build-dir  "target"}

  :main nil)
