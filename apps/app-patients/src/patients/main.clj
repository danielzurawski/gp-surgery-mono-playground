(ns patients.main
  (:require
   [ring.adapter.jetty :as jetty]
   [clojure.tools.logging :as log]
   [patients.handler :refer [web-handler]]
   [mount.core :as mount])
  (:gen-class))

(defn -main [& args]
  (mount/start)
  (jetty/run-jetty web-handler
                   {:port  3001
                    :join? false}))
