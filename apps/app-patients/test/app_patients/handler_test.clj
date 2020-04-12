(ns patients.handler-test
  (:require [cheshire.core :as cheshire]
            [clojure.test :refer :all]
            [patients.handler :as handler]
            [ring.mock.request :as mock]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(def patient-record
  {:name "Daniel Zurawski"
   :description "nothing"
   :gender :M
   :origin {:country "Poland"
            :city "Bydgoszcz"}})

(defn mock-get-all-records [& args]
  [patient-record])

(defn mock-save-patient-record! [& args]
  patient-record)

(defn mock-stream [& args])

(defn stringify-gender [record]
  (update-in record [:gender] name))

(defn expected-patient-records []
  (map stringify-gender (mock-get-all-records)))

(deftest patients-test
  (testing "Test POST request to /patients, return sent record"
    (let [web-handler (handler/init mock-get-all-records mock-save-patient-record! mock-stream)
          response    (web-handler (-> (mock/request :post "/patients")
                                       (mock/content-type "application/json")
                                       (mock/body (cheshire.core/generate-string patient-record))))
          body        (parse-body (:body response))]
      (is (= (:status response) 200))
      (is (= (stringify-gender patient-record) body))))

  (testing "Test GET request to /patients, returns patient record"
    (let [web-handler (handler/init mock-get-all-records mock-save-patient-record! mock-stream)
          response    (web-handler (mock/request :get "/patients"))
          body        (parse-body (:body response))]

      (is (= 200 (:status response)))
      (is (= (expected-patient-records) body)))))
