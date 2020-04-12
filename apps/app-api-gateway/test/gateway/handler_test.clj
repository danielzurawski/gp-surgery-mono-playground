(ns gateway.handler-test
  (:require [cheshire.core :as cheshire]
            [clojure.test :refer :all]
            [gateway.handler :as handler]
            [ring.mock.request :as mock]))

(def mock-users-by-username {"test123" {:id 123 :type "patient" :username "test123"}})

(def mock-db-service
  {:get-user-fn (fn [login] (get mock-users-by-username (:username login) ))})

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(deftest authentication-tests
  (testing "test 123"
    (is (= 1 1) "should be equal"))

  (testing "Test POST request to /api/login returns a JWT token"
    (let [app (handler/init mock-db-service)
          response (app (-> (mock/request :post "/api/login")
                            (mock/content-type "application/json")
                            (mock/body (cheshire.core/generate-string {:username "test123" :password "test"}))))
          body     (parse-body (:body response))]

      (is (= 200 (:status response)))
      (is (not-empty (:token body))  "Should return token")
      (is (= "test123" (:username body)) "Should return username")
      (is (= 123 (:id body))) "Should return ID")))
