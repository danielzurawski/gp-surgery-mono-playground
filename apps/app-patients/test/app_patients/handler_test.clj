(ns patients.handler-test
  (:require [cheshire.core :as cheshire]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [patients.handler :refer [web-handler]]
            [ring.mock.request :as mock]
            [mount.core :as mount]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(defn start-states [f]
  (-> (mount.core/only #{#'patients.handler/web-handler
                         #'patients.patients-producer/patients-producer
                         #'patients.patients-stream/patients-stream})
      (mount.core/swap {#'patients.patients-producer/patients-producer (fn [])
                        #'patients.patients-stream/patients-stream (fn [])})
      mount/start)
  (f))

(use-fixtures :once start-states)

(def patient-record
  {:name "Daniel Zurawski"
   :email "zurawski.daniel@gmail.com"
   :password "test123"})

(deftest patients-test
  (testing "Test POST request to /patients, return sent record"
    (with-redefs-fn
      {#'patients.patients-producer/produce! (fn [& args])}
      #(let [response    (web-handler (-> (mock/request :post "/patients")
                                          (mock/content-type "application/json")
                                          (mock/body (cheshire.core/generate-string patient-record))))
             body        (parse-body (:body response))]
         (is (= (:status response) 200))
         (is (= patient-record body)))))

  (testing "Test GET request to /patients, returns patient record"
    (with-redefs-fn
      {#'patients.patients-stream/get-all-from-store (fn [& args] [patient-record])}
      #(let [response    (web-handler (mock/request :get "/patients"))
             body        (parse-body (:body response))]
         (is (= 200 (:status response)))
         (is (= [patient-record] body))))))
