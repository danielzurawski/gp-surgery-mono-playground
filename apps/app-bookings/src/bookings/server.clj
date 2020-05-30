(ns bookings.server
  (:require [ring.adapter.jetty :as jetty]
            [com.stuartsierra.component :as component]))

(defrecord WebServer [port http-server web-handler]
  component/Lifecycle
  (start [component]
    (print ";; Starting WebServer")
    (assoc component
      :http-server
      (jetty/run-jetty
        (:handler web-handler)
        {:port  port
         :join? false}))
    (println "Server started on port" port))
  (stop [component]
    (.stop (:http-server component))
    component))

