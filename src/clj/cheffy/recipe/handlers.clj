(ns cheffy.recipe.handlers
  (:require [cheffy.recipe.db :as db]
            [cheffy.responses :refer [base-url]]
            [ring.util.response :as r])
  (:import [java.util UUID]))

(defn list-all-recipes [db]
  (fn [req]
    (let [uid "auth0|5ef440986e8fbb001355fd9c"
          recipes (db/find-all-recipes db uid)]
      (r/response recipes))))

(defn create-recipe! [db]
  (fn [req]
    (let [recipe-id (str (UUID/randomUUID))
          uid "auth0|5ef440986e8fbb001355fd9c"
          recipe (-> req :parameters :body)]
      (db/insert-recipe! db (assoc recipe :recipe-id recipe-id :uid uid))
      (r/created (str base-url "/recipes/" recipe-id)
                 {:recipe-id recipe-id}))))

(defn retreive-recipe [db]
  (fn [req]
    (let [recipe-id (-> req :parameters :path :recipe-id)
          recipe (db/find-recipe-by-id db recipe-id)]
      (if recipe
        (r/response recipe)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"
                      :data (str "recipe-id " recipe-id)})))))

(defn update-recipe!
  [db]
  (fn [request]
    (let [recipe-id (-> request :parameters :path :recipe-id)
          recipe (-> request :parameters :body)
          updated? (db/update-recipe! db (assoc recipe :recipe-id recipe-id))]
      (if updated?
        (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"
                      :data (str "recipe-id " recipe-id)})))))

(defn delete-recipe!
  [db]
  (fn [request]
    (let [recipe-id (-> request :parameters :path :recipe-id)
          deleted? (db/delete-recipe! db {:recipe-id recipe-id})]
      (if deleted?
        (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"
                      :data (str "recipe-id " recipe-id)})))))

