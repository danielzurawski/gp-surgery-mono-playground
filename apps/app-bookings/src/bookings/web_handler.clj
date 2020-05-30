(ns bookings.web-handler
  (:require [com.stuartsierra.component :as component]
            [bookings.bookings :as bookings]
            [schema.core :as s]
            [compojure.api.exception :as ex]
            [compojure.api.sweet :refer [api context GET POST]]
            [ring.util.http-response :refer [ok]])
  (:import [java.util UUID]))

(s/defschema BookingRequest
  {:patient_id s/Num
   :date       long})

(s/defschema BookingResponse
  (merge BookingRequest
    {:id s/Str}))

(defn create-web-handler
  [{:keys [bookings-producer
           kafka-config]}]
  (api {:swagger
        {:ui   "/"
         :spec "/swagger.json"
         :data {:info {:title       "Bookings Service"
                       :description "Manage bookings"}
                :tags [{:name "api", :description "Bookings service APIs"}]}}
        :exceptions
        {:handlers
         {::ex/request-validation  (ex/with-logging ex/request-parsing-handler :debug)
          ::ex/response-validation (ex/with-logging ex/response-validation-handler :debug)
          ::ex/default             (ex/with-logging ex/safe-handler :debug)}}}

    (context "/bookings" []
      :tags ["bookings"]

      (POST "/" []
        :return BookingResponse
        :body [booking BookingRequest]
        :summary "Saves a Booking record onto Kafka"
        (let [uuid (str (UUID/randomUUID))
              booking-record (assoc booking :id uuid)]
          (bookings/save! bookings-producer
            (:topic kafka-config)
            booking-record)
          (ok booking-record))))))

;(GET "/" []
;  :return [Booking]
;  :summary "Returns list of bookings from local KStore"
;  (do (println "processing request" (bookings/get-all-from-store bookings-stream))
;      (ok (bookings/get-all-from-store bookings-stream))))
;

(defrecord WebHandler [bookings-producer kafka-config]
  component/Lifecycle
  (start [component]
    (assoc component
      :handler (create-web-handler
                 {:bookings-producer bookings-producer
                  :kafka-config      kafka-config})))
  (stop [component]
    (assoc component :handler nil)))
