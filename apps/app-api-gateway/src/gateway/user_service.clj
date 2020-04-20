(ns gateway.user-service
  (:require [gateway.admin :as a]
            [gateway.patient :as p]))

(defmulti save-user (fn [data] (:type data)))
(defmulti get-user (fn [data] (:type data)))

(defmethod get-user :patient [data]
  (.get-record (p/->Patient (:email data) (:password data))))
(defmethod save-user :patient [data]
  (.save-record (p/->Patient (:email data) (:password data))))

(defmethod get-user :admin [data]
  (.get-record (a/->Admin (:email data) (:password data))))
(defmethod save-user :admin [data]
  (.save-record (a/->Admin (:email data) (:password data))))
