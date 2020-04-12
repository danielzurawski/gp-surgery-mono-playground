(ns gateway.user
  (:require
  ;;  [gateway.db :as db]
   [mount.core :refer [defstate]]))

(def mock-users-by-username {"test123" {:id 123 :type "patient" :username "test345"}})

(defn get-user [login] (get mock-users-by-username (:username login)))

(defstate get-user :start get-user)
