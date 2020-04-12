(ns gateway.handler
  (:require [compojure.api.sweet :refer :all]
            [compojure.api.exception :as ex]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [buddy.sign.jwt :as jwt]))

(s/defschema LoginResponse
  {:id s/Num
   :token s/Str
   :username s/Str})

(s/defschema LoginRequest
  {:username s/Str
   :password s/Str})

(def jwt-secret "so-secret")

(defn custom-handler [f type]
  (fn [^Exception e data request]
    (f {:message e, :type type})))

(defn init [db-service]
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "GP Api Gateway"
                   :description "Authentication gateway"}
            :tags [{:name "api", :description "auth apis"}]}}
    :exceptions {:handlers {::ex/request-validation (ex/with-logging ex/request-parsing-handler :info)
                            ::ex/response-validation (ex/with-logging ex/response-validation-handler :info)
                            ::ex/default (custom-handler internal-server-error :info)}}}

   (context "/api" []
     :tags ["api"]

     (POST "/login" []
       :return LoginResponse
       :body [login LoginRequest]
       :summary "Returns id, username and a signed JWT token"
       (let [user ((:get-user-fn db-service) login)
             token (jwt/sign user jwt-secret)]
         (ok {:token token
              :id (:id user)
              :username (:username user)}))))))
