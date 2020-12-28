(ns cheffy.recipe.routes
  (:require [cheffy.recipe.handlers :as handle]))

(defn routes [env]
  (let [db (:jdbc-url env)]
    ["/recipes" {:swagger {:tags ["recipes"]}
                 :get {:summary "List all recipes"
                       :handler (handle/list-all-recipes db)}}]))
