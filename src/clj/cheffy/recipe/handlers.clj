(ns cheffy.recipe.handlers
  (:require [cheffy.recipe.db :as db]
            [ring.util.response :as r]))

(defn list-all-recipes [db]
  (fn [req]
    (let [recipes (db/find-all-recipes db)]
      (r/response recipes))))
