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
  (-> (app {:request-method :get
            :uri "/v1/recipes/a1995316-80ea-4a98-939d-7c6295e4bb46"})
      :body
      slurp)
  (app {:request-method :get
        :uri "/"})
  (jdbc/execute! db ["select * from recipe where public = true"])
  (sql/find-by-keys db :recipe {:public false})
  (cheffy.recipe.db/find-all-recipes db "auth0|5ef440986e8fbb001355fd9c")
  (with-open [conn (jdbc/get-connection db)]
    (let [recipe-id "a1995316-80ea-4a98-939d-7c6295e4bb46"]
      (sql/get-by-id conn :recipe recipe-id :recipe_id {})))
  (with-open [conn (jdbc/get-connection db)]
    (let [recipe-id "a1995316-80ea-4a98-939d-7c6295e4bb46"
          [recipe] (sql/find-by-keys conn :recipe {:recipe_id recipe-id})
          steps (sql/find-by-keys conn :step {:recipe_id recipe-id})
          ingredients (sql/find-by-keys conn :ingredient {:recipe_id recipe-id})]
      (when (seq recipe)
        (assoc recipe
          :recipe/steps steps
          :recipe/ingredients ingredients))))
  (go)
  (halt)
  (reset)
  (reset-all))
