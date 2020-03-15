(ns app-api-gateway.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "App-api-gateway"
                    :description "Compojure Api example"}
             :tags [{:name "api", :description "some apis"}]}}}

    (context "/api" []
      :tags ["api"]

      (GET "/sum" []
        :return {:result Long}
        :query-params [x :- Long, y :- Long]
        :summary "Retrieves a list of patients"
        (ok {:result (+ x y)})))))
