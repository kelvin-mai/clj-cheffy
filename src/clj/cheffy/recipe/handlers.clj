(ns cheffy.recipe.handlers
  (:require [cheffy.recipe.db :as db]
            [cheffy.responses :refer [base-url]]
            [ring.util.response :as r])
  (:import [java.util UUID]))

(defn list-all-recipes [db]
  (fn [req]
    (let [uid (-> req :claims :sub)
          recipes (db/find-all-recipes db uid)]
      (r/response recipes))))

(defn create-recipe! [db]
  (fn [req]
    (let [recipe-id (str (UUID/randomUUID))
          uid (-> req :claims :sub)
          recipe (-> req :parameters :body)]
      (db/insert-recipe! db (assoc recipe
                              :recipe-id recipe-id
                              :uid uid))
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

(defn update-recipe! [db]
  (fn [req]
    (let [recipe-id (-> req :parameters :path :recipe-id)
          recipe (-> req :parameters :body)
          updated? (db/update-recipe! db (assoc recipe :recipe-id recipe-id))]
      (if updated?
        (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"
                      :data (str "recipe-id " recipe-id)})))))

(defn delete-recipe! [db]
  (fn [req]
    (let [recipe-id (-> req :parameters :path :recipe-id)
          deleted? (db/delete-recipe! db {:recipe-id recipe-id})]
      (if deleted?
        (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"
                      :data (str "recipe-id " recipe-id)})))))

(defn favorite-recipe! [db]
  (fn [req]
    (let [uid (-> req :claims :sub)
          recipe-id (-> req :parameters :path :recipe-id)
          updated? (db/favorite-recipe! db {:uid uid
                                            :recipe-id recipe-id})]
      (if updated? (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"
                      :data (str "recipe-id " recipe-id)})))))

(defn unfavorite-recipe! [db]
  (fn [req]
    (let [uid (-> req :claims :sub)
          recipe-id (-> req :parameters :path :recipe-id)
          deleted? (db/unfavorite-recipe! db {:uid uid
                                              :recipe-id recipe-id})]
      (if deleted?
        (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"
                      :data (str "recipe-id " recipe-id)})))))

(defn create-step! [db]
  (fn [req]
    (let [recipe-id (-> req :parameters :path :recipe-id)
          step (-> req :parameters :body)
          step-id (UUID/randomUUID)]
      (db/insert-step! db (assoc step
                            :step-id step-id
                            :recipe-id recipe-id))
      (r/created
        (str base-url "/recipes/")
        {:step-id step-id}))))

(defn update-step! [db]
  (fn [req]
    (let [step (-> req :parameters :body)
          updated? (db/update-step! db step)]
      (if updated?
        (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"})))))

(defn delete-step! [db]
  (fn [req]
    (let [step (-> req :parameters :body)
          deleted? (db/delete-step! step)]
      (if deleted?
        (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"})))))

(defn create-ingredient! [db]
  (fn [req]
    (let [recipe-id (-> req :parameters :path :recipe-id)
          ingredient (-> req :parameters :body)
          ingredient-id (UUID/randomUUID)]
      (db/insert-ingredient! db (assoc ingredient
                                  :ingredient-id ingredient-id
                                  :recipe-id recipe-id))
      (r/created
        (str base-url "/recipes/" recipe-id)
        {:ingredient-id ingredient-id}))))

(defn update-ingredient! [db]
  (fn [req]
    (let [ingredient (-> req :parameters :body)
          updated? (db/update-ingredient! db ingredient)]
      (if updated?
        (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"})))))

(defn delete-ingredient! [db]
  (fn [req]
    (let [ingredient (-> req :parameters :body)
          deleted? (db/delete-ingredient! ingredient)]
      (if deleted?
        (r/status 204)
        (r/not-found {:type "recipe-not-found"
                      :message "Recipe not found"})))))
