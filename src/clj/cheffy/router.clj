(ns cheffy.router
  (:require [cheffy.recipe.routes :as recipe]
            [muuntaja.core :as m]
            [reitit.ring :as ring]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]))

(def swagger-docs
  ["/swagger.json" {:get {:no-doc true
                          :swagger {:basePath "/"
                                    :info {:title "Cheffy API Reference"
                                           :description "The Cheffy API is organized around REST. Returns JSON, Transit (msgpack, json), or EDN  encoded responses."
                                           :version "1.0.0"}}
                          :handler (swagger/create-swagger-handler)}}])

(def router-config
  {:data {:muuntaja m/instance
          :middleware [swagger/swagger-feature
                       muuntaja/format-middleware]}})

(defn routes [env]
  (ring/ring-handler
    (ring/router
      [swagger-docs
       ["/v1"
        (recipe/routes env)]]
      router-config)
    (ring/routes
      (swagger-ui/create-swagger-ui-handler {:path "/"}))))
