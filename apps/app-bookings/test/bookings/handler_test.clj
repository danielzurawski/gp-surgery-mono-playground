(ns bookings.handler_test
  (:require
    [com.stuartsierra.component :as component]
    [cheshire.core :as cheshire]
    [clojure.test :refer :all]
    [ring.mock.request :as mock]
    [bookings.producer.test-bookings-producer :as test-bookings-producer]
    [bookings.web-handler :as web-handler]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(def test-system
  (component/system-map
    :kafka-config {:topic "test-topic"}
    :bookings-producer (test-bookings-producer/create-producer)

    :web-handler (component/using
                   (web-handler/map->WebHandler {})
                   [:bookings-producer :kafka-config])))

#_:web-server #_(component/using
                  (server/map->WebServer {:port 3000})
                  [:web-handler])

(deftest bookings-endpoint-tests
  (let [system-under-test (component/start test-system)
        handler (:handler (:web-handler system-under-test))]

    (testing "should POST booking"
      (let [booking-record {:patient_id 345
                            :date       1589130601988}
            response (handler (->
                                (mock/request :post "/bookings"
                                  (cheshire/generate-string booking-record))
                                (mock/content-type "application/json")))
            body (parse-body (:body response))]
        (is (= 200 (:status response)))
        (is (= booking-record (select-keys body [:patient_id :date])))
        (is (contains? body :id))))))
