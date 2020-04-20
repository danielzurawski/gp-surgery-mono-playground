(ns patients.patients
  (:require [patients.patients-stream :as patients-stream]
            [patients.patients-producer :as patients-producer]))

(defn get-all []
  (patients-stream/get-all-from-store))

(defn get-by-email [])

(defn save!
  "Publisher a message with latest patients details"
  [record]
  (patients-producer/produce! record))

(comment
  ;; example save-patient-record!
  (save!
   {:name "Daniel Zurawski 2"
    :email "zurawski.daniel@gmail.com"
    :password "test123"}))

