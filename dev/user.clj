(ns user
  (:require [cheffy.core]
            [integrant.core :as ig]
            [integrant.repl :as ig-repl :refer [go
                                                halt
                                                reset
                                                reset-all]]
            [integrant.repl.state :as state]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]))

(ig-repl/set-prep!
  (fn [] (-> "resources/config.edn"
             slurp
             ig/read-string)))

(def app (-> state/system :cheffy/app))
(def db (-> state/system :db/postgres))

(comment
  (app {:request-method :get
        :uri "/v1/recipes"})
  (app {:request-method :get
        :uri "/"})
  (jdbc/execute! db ["select * from recipe where public = true"])
  (sql/find-by-keys db :recipe {:public true})
  (go)
  (halt)
  (reset)
  (reset-all))
