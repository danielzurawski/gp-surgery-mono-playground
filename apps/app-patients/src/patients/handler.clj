(ns patients.handler
  (:require [compojure.api.sweet :refer :all]
            [compojure.api.exception :as ex]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(s/defschema Patient
  {:name s/Str
   (s/optional-key :description) s/Str
   :gender (s/enum :F :M)
   :origin {:country s/Str
            :city s/Str}})

(defn custom-handler [f type]
  (fn [^Exception e data request]
    (f {:message e, :type type})))

(defn init [get-all-record-fn save-record-fn! stream]
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

      (GET "/" []
        :return [Patient]
        :summary "Returns list of patients from local KStore"
        (ok (get-all-record-fn stream)))

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
          (save-record-fn! patient)
          (ok patient))))))
