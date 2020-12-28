(ns cheffy.core
  (:require [integrant.core :as ig]
            [reitit.ring :as ring]
            [ring.adapter.jetty :as jetty]))

(defn app [env]
  (ring/ring-handler
    (ring/router
      [["/" {:get (fn [_]
                    {:status 200
                     :body "hello world"})}]])))

(defmethod ig/init-key :server/jetty
  [_ {:keys [handler port]}]
  (println (str "Server running on port " port))
  (jetty/run-jetty handler {:port port
                            :join? false}))

(defmethod ig/init-key :cheffy/app
  [_ config]
  (app config))

(defmethod ig/init-key :db/postgres
  [_ config]
  (println "Configured database")
  (:jdbc-url config))

(defmethod ig/halt-key! :server/jetty
  [_ jetty]
  (.stop jetty))

(defn -main
  [config-file]
  (let [config (-> config-file
                   slurp
                   ig/read-string)]
    (ig/init config)))
