(ns app-patients.test.handler
  (:require [cheshire.core :as cheshire]
            [clojure.test :refer :all]
            [app-patients.handler :refer :all]
            [ring.mock.request :as mock]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(def patient-record
  {:name "Daniel Zurawski"
   :description "nothing"
   :gender "M"
   :origin {:country "Poland"
            :city "Bydgoszcz"}})

(deftest patients-test
  (testing "Test POST request to /patients/echo, returns patient record"
    (let [response (app (-> (mock/request :post "/patients/echo")
                            (mock/json-body patient-record)))
          body     (parse-body (:body response))]

      (is (= 200 (:status response)))
      (is (= patient-record body)))))
