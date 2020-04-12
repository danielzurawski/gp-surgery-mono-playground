(ns gateway.main
  (:require
   [ring.adapter.jetty :as jetty]
   [gateway.handler :as web-handler])
  (:gen-class))

(def state (atom {}))

(def mock-users-by-username {"test123" {:id 123 :type "patient" :username "test123"}})

(defn -main [& args]
  (let [mock-db-service {:get-user-fn (fn [login] (get mock-users-by-username (:username login)))}
        web-api (web-handler/init mock-db-service)]
    (reset! state {:jetty (jetty/run-jetty
                           web-api
                           {:port  3000
                            :join? false})})))
