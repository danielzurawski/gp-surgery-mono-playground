(ns gateway.server
  (:require [mount.core :refer [defstate]]
            [gateway.handler :as handler]
            [ring.adapter.jetty :as jetty]))

(defn start-server []
  (-> handler/web-handler
      (jetty/run-jetty
       {:port  3000
        :join? false})))

(defstate server
  :start (start-server)
  :stop (.stop server))
