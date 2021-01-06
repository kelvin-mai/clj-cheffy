(ns cheffy.auth0
  (:require [clj-http.client :as http]
            [muuntaja.core :as m]))

(defn get-test-token []
  (->> {:content-type :json
        :cookie-policy :standard
        :body (m/encode "application/json"
                        {:client_id "O2ZJEiTtavznXhAJX45ollR06jK92a6R"
                         :audience "https://kelvin-cheffy.us.auth0.com/api/v2/"
                         :grant_type "password"
                         :username "testing@cheffy.app"
                         :password "mK!Ha5^0%Q23"
                         :scope "openid profile email"})}
       (http/post "https://kelvin-cheffy.us.auth0.com/oauth/token/")
       (m/decode-response-body)
       :access_token))

