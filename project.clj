(defproject gp-surgery-mono-playground/all "MONOLITH"
  :description "Overarching GP Surgery Mono Playground project."

  :plugins
  [[lein-monolith "1.4.0"]
   [lein-pprint "1.2.0"]
   [io.sarnowski/lein-docker "1.0.0"]]

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [metosin/compojure-api "2.0.0-alpha30"]
                 [ring "1.8.0"]
                 [buddy/buddy-auth "2.2.0"]
                 [mount "0.1.16"]]

  :test-selectors
  {:unit (complement :integration)
   :integration :integration}

  :monolith
  {:inherit
   [:test-selectors
    :env
    :dependencies]

   :inherit-leaky
   [:repositories
    :managed-dependencies]

   :project-selectors
   {:deployable :deployable
    :unstable #(= (first (:version %)) \0)}

   :project-dirs
   ["apps/app-api-gateway"
    "apps/app-patients"
    "libs/*"
    "not-found"]}

  :env
  {:foo "bar"})
