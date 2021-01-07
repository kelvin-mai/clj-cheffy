(ns cheffy.core
  (:require [aero.core :as aero]
            [cheffy.router :refer [routes]]
            [integrant.core :as ig]
            [next.jdbc :as jdbc]
            [ring.adapter.jetty :as jetty]))

(defmethod aero/reader 'ig/ref
  [_ _ value]
  (ig/ref value))

(defn app [env]
  (routes env))

(defmethod ig/init-key :server/jetty
  [_ {:keys [handler port]}]
  (println (str "Server running on port " port))
  (jetty/run-jetty handler {:port port
                            :join? false}))

(defmethod ig/init-key :cheffy/app
  [_ config]
  (println "Initialized application")
  (app config))

(defmethod ig/init-key :db/postgres
  [_ {:keys [jdbc-url]}]
  (println "Configured database")
  (jdbc/with-options jdbc-url jdbc/snake-kebab-opts))

(defmethod ig/halt-key! :server/jetty
  [_ jetty]
  (.stop jetty))

(defn system-config [file]
  (aero/read-config file))

(defn -main
  [config-file]
  (-> config-file
      (system-config)
      (ig/init)))

