(ns gateway.user)

(defprotocol User
  "Define a generic User abstraction (for both patients and admins)"
  (save-record [this])
  (get-record [this]))

