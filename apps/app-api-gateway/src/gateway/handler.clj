(ns gateway.handler
  (:require [compojure.api.sweet :refer [api context POST]]
            [compojure.api.exception :as ex]
            [ring.util.http-response :refer [ok]]
            [schema.core :as s]
            [buddy.sign.jwt :as jwt]
            [gateway.user-service :as user]
            [mount.core :refer [defstate]]))

(s/defschema LoginResponse
  {:token s/Str
   :email s/Str})

(s/defschema LoginRequest
  {:email s/Str
   :password s/Str})

(s/defschema RegisterRequest
  {:email s/Str
   :password s/Str
   :type (s/enum :patient :admin)})

(def jwt-secret "so-secret")


(defn create-web-handler []
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "GP Api Gateway"
                   :description "Authentication gateway"}
            :tags [{:name "api", :description "auth apis"}]}}
    :exceptions {:handlers {::ex/request-validation (ex/with-logging ex/request-parsing-handler :info)
                            ::ex/response-validation (ex/with-logging ex/response-validation-handler :info)
                            ::ex/default (ex/with-logging ex/safe-handler :error)}}}

   (context "/api" []
     :tags ["api"]

     (POST "/register" []
       :body [data RegisterRequest]
       :summary "Saves the user record"
       (let [user (user/save-user data)]
         (ok {:email (:email user)})))

     (POST "/login" []
       :return LoginResponse
       :body [login LoginRequest]
       :summary "Returns email and a signed JWT token"
       (let [user (user/get-user login)
             token (jwt/sign user jwt-secret)]
         (ok {:token token
              :email (:email user)}))))))

(defstate web-handler
  :start (create-web-handler))


