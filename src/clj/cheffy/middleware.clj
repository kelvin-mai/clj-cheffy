(ns cheffy.middleware
  (:require [cheffy.recipe.db :as recipe-db]
            [ring.middleware.jwt :as jwt]))

(def wrap-auth0
  {:name ::auth0
   :description "Middleware for auth0 authentication and authorization"
   :wrap (fn [handler]
           (jwt/wrap-jwt
             handler
             {:issuers {"https://kelvin-cheffy.us.auth0.com/"
                          {:alg :RS256
                           :jwk-endpoint "https://kelvin-cheffy.us.auth0.com/.well-known/jwks.json"}}}))})
