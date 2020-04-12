(ns gateway.main
  (:require [mount.core :as mount]
            [gateway.server :refer [server]])
  (:gen-class))

(defn -main [& args]
  (mount/start))
