(ns patients.handler
  (:require [compojure.api.sweet :refer [api context GET POST]]
            [compojure.api.exception :as ex]
            [ring.util.http-response :refer [ok]]
            [schema.core :as s]
            [patients.patients :as patients]
            [mount.core :refer [defstate]]))


(s/defschema Patient
  {:name s/Str
   :email s/Str
   :password s/Str})


(defn create-web-handler []
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "App-patients"
                    :description "Compojure Api example"}
             :tags [{:name "api", :description "Patients service APIs"}]}}
     :exceptions {:handlers {::ex/request-validation (ex/with-logging ex/request-parsing-handler :info)
                             ::ex/response-validation (ex/with-logging ex/response-validation-handler :info)
                             ::ex/default (ex/with-logging ex/safe-handler :error)}}}

    (context "/patients" []
      :tags ["patients"]

      (GET "/" []
        :return [Patient]
        :summary "Returns list of patients from local KStore"
        (ok (patients/get-all)))

      (POST "/" []
        :return Patient
        :body [patient Patient]
        :summary "Saves a Patient record onto Kafka"
        (do
          (patients/save! patient)
          (ok patient)))

      (POST "/echo" []
        :return Patient
        :body [patient Patient]
        :summary "echoes the sent Patient record"
        (ok patient)))))


(defstate web-handler
  :start (create-web-handler))


