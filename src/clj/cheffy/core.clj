(ns cheffy.core
  (:require [cheffy.router :refer [routes]]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]))

(defn app [env]
  (routes env))

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
