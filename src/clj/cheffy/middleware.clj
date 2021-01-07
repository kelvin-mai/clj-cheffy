(ns cheffy.middleware
  (:require [cheffy.recipe.db :as recipe-db]
            [ring.middleware.jwt :as jwt]
            [ring.util.response :as r]))

(def wrap-auth0
  {:name ::auth0
   :description "Middleware for auth0 authentication and authorization"
   :wrap (fn [handler]
           (jwt/wrap-jwt
             handler
             {:issuers {"https://kelvin-cheffy.us.auth0.com/"
                          {:alg :RS256
                           :jwk-endpoint "https://kelvin-cheffy.us.auth0.com/.well-known/jwks.json"}}}))})

(def wrap-recipe-owner
  {:name ::recipe-owner
   :description "Middleware to check if requester is recipe owner"
   :wrap (fn [handler db]
           (fn [req]
             (def temp
               (-> req :claims))
             (let [uid (-> req :claims :sub)
                   recipe-id (-> req :parameters :path :recipe-id)
                   recipe (recipe-db/find-recipe-by-id db recipe-id)]
               (if (= (:recipe/uid recipe) uid)
                 (handler req)
                 (-> (r/response {:message "You need to be the recipe owner"
                                  :data (str "recipe-id" recipe-id)
                                  :type :authorization-required})
                     (r/status 401))))))})
