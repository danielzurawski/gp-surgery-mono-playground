(ns gateway.user-service-test
  (:require [clojure.test :refer [testing deftest is]]
            [gateway.user-service :as user]))

(deftest user-service-tests
  (testing "Creating user with correct type"
    (let [patient {:email "zurawski.daniel@gmail.com" :password "test123" :type :patient}
          admin (assoc patient :type :admin)]

      (is (= patient (user/save-user patient)) "patient should be equal")
      (is (= admin (user/save-user admin)) "admin should be equal")))

  (testing "Retrieving user with correct type"
    (let [patient {:email "zurawski.daniel@gmail.com" :password "test123" :type :patient}
          admin (assoc patient :type :admin)]
      (is (= patient (user/get-user patient)) "patient should be equal")
      (is (= admin (user/get-user admin)) "admin should be equal"))))
