(ns gateway.handler-test
  (:require [cheshire.core :as cheshire]
            [clojure.test :refer :all]
            [gateway.handler :as handler]
            ;; [gateway.user :as user]
            [ring.mock.request :as mock]
            [mount.core :as mount]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(defn get-user [& args]
  {:id 123 :type "patient" :username "test123"})

(defn start-states [f]
  (-> (mount.core/only #{#'gateway.handler/web-handler
                         #'gateway.user/get-user})
      (mount.core/swap {#'gateway.user/get-user get-user})
      mount/start)
  (f))

(use-fixtures :once start-states)

(deftest authentication-tests
  (testing "test 123"
    (is (= 1 1) "should be equal"))

  (testing "Test POST request to /api/login returns a JWT token"
    (let [response (handler/web-handler (-> (mock/request :post "/api/login")
                                            (mock/content-type "application/json")
                                            (mock/body (cheshire.core/generate-string {:username "test123" :password "test"}))))
          body     (parse-body (:body response))]

      (is (= 200 (:status response)))
      (is (not-empty (:token body))  "Should return token")
      (is (= "test123" (:username body)) "Should return username")
      (is (= 123 (:id body))) "Should return ID")))
