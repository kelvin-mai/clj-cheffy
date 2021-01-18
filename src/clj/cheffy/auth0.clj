(ns cheffy.auth0
  (:require [clj-http.client :as http]
            [muuntaja.core :as m]))

(defn get-test-token []
  (->> {:content-type :json
        :cookie-policy :standard
        :body (m/encode "application/json"
                        {:client_id ""
                         :audience "https://kelvin-cheffy.us.auth0.com/api/v2/"
                         :grant_type "password"
                         :username "testing@cheffy.app"
                         :password ""
                         :scope "openid profile email"})}
       (http/post "https://kelvin-cheffy.us.auth0.com/oauth/token/")
       (m/decode-response-body)
       :access_token))

(defn get-management-token []
  (->> {:content-type :json
        :cookie-policy :standard
        :throw-exceptions false
        :body (m/encode "application/json"
                        {:client_id ""
                         :client_secret ""
                         :audience "https://kelvin-cheffy.us.auth0.com/api/v2/"
                         :grant_type "client_credentials"})}
       (http/post "https://kelvin-cheffy.us.auth0.com/oauth/token")
       (m/decode-response-body)
       :access_token))

(defn get-role-id [token]
  (->> {:headers {"Authorization" (str "Bearer " token)}
        :content-type :json
        :throw-exceptions false
        :cookie-policy :standard}
       (http/get "https://kelvin-cheffy.us.auth0.com/api/v2/roles")
       (m/decode-response-body)
       (filter #(= (:name %) "manage-recipes"))
       (first)
       :id))

