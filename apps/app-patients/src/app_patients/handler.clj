(ns app-patients.handler
  (:require [compojure.api.sweet :refer :all]
            [compojure.api.exception :as ex]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [app-patients.patients-producer :as patients-producer]))

(s/defschema Patient
  {:name s/Str
   (s/optional-key :description) s/Str
   :gender (s/enum :F :M)
   :origin {:country s/Str
            :city s/Str}})

(defn custom-handler [f type]
  (fn [^Exception e data request]
    (f {:message e, :type type})))
;; (.getMessage e)

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "App-patients"
                    :description "Compojure Api example"}
             :tags [{:name "api", :description "Patients service APIs"}]}}
     :exceptions {:handlers {
                             ;;::ex/request-validation (ex/with-logging ex/request-parsing-handler :info)
                             ::ex/default (custom-handler internal-server-error :unknown)}}}

    (context "/patients" []
      :tags ["patients"]

      (POST "/echo" []
        :return Patient
        :body [patient Patient]
        :summary "echoes a Patient"
        (ok patient))

      (POST "/" []
        :return Patient
        :body [patient Patient]
        :summary "Saves a Patient record onto Kafka"
        (do
          (patients-producer/save-patient-record! patient)
          (ok patient))))))
