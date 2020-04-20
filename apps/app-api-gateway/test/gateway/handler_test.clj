(ns gateway.handler-test
  (:require [cheshire.core :as cheshire]
            [clojure.test :refer :all]
            [gateway.handler :as handler]
            [gateway.user-service :as user-service]
            [ring.mock.request :as mock]
            [mount.core :as mount]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(defn get-user [& args]
  {:id 123 :type "patient" :username "test123"})

(defn start-states [f]
  (-> (mount.core/only #{#'gateway.handler/web-handler})
      mount/start)
  (f))

(use-fixtures :once start-states)

(defn mock-get-user [data] data)
(defn mock-save-user [data] data)

(deftest authentication-tests
  (testing "test 123"
    (is (= 1 1) "should be equal"))

  (testing "Test POST request to /api/register returns OK"
    (with-redefs-fn {#'gateway.user-service/save-user mock-save-user}
      #(let [test-user {:email "zurawski.daniel@gmail.com" :password "test123" :type :patient}
             response  (handler/web-handler (-> (mock/request :post "/api/register")
                                                (mock/content-type "application/json")
                                                (mock/body (cheshire.core/generate-string test-user))))
             body      (parse-body (:body response))]
         (is (= 200 (:status response)))
         (is (= "zurawski.daniel@gmail.com" (:email body))))))

  (testing "Test POST request to /api/login returns a JWT token"
    (with-redefs-fn {#'gateway.user-service/get-user mock-get-user}
      #(let [patient-data {:email "zurawski.daniel@gmail.com" :password "test"}
             response (handler/web-handler (-> (mock/request :post "/api/login")
                                               (mock/content-type "application/json")
                                               (mock/body (cheshire.core/generate-string patient-data))))
             body     (parse-body (:body response))]
         (is (= 200 (:status response)))
         (is (not-empty (:token body))  "Should return token")
         (is (= "zurawski.daniel@gmail.com" (:email body)) "Should return username")))))
