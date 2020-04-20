(ns gateway.admin
  (:require
   [gateway.user :as u]
   [clj-http.client :as client]))

(defrecord
 Admin [^String email ^String password]
  u/User
  (save-record
   [this]
   (println "save-user got patient data" this)
   {:email email :password password :type :admin})

  (get-record
   [this]
   (println "get-user got email" (:email this))
   {:email email :password password :type :admin}))
