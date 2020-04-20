(ns gateway.patient
  (:require [clj-http.client :as client]
            [gateway.user :as u])
  (:import [gateway.user User]))

(defrecord
 Patient [^String email ^String password]
  u/User
  (save-record
   [this]
   (println "save-user got patient data" this)
   {:email email :password password :type :patient})

  (get-record
   [this]
   (println "get-user got email" (:email this))
   {:email email :password password :type :patient}))
