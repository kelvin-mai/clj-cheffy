(ns cheffy.account.handlers
  (:require [cheffy.account.db :as db]
            [cheffy.auth0 :as auth0]
            [clj-http.client :as http]
            [muuntaja.core :as m]
            [ring.util.response :as r]))

(defn create-account! [db]
  (fn [req]
    (let [{:keys [sub name picture]} (-> req :claims)]
      (db/create-account! db {:uid sub
                              :name name
                              :picture picture})
      (r/status 204))))

(defn delete-account! [db]
  (fn [req]
    (let [uid (-> req :claims :sub)
          delete-auth0-account! (http/delete (str "https://kelvin-cheffy.us.auth0.com/api/v2/users/" uid)
                                             {:headers {"Authorization" (str "Bearer " (auth0/get-management-token))}})]
      (when (= (:status delete-auth0-account!) 204)
        (db/delete-account! db {:uid uid})
        (r/status 204)))))

(defn update-role-to-cook! []
  (fn [req]
    (let [uid (-> req :claims :sub)
          token (auth0/get-management-token)]
      (->> {:headers {"Authorization" (str "Bearer " (auth0/get-management-token))}
            :cookie-policy :standard
            :content-type :json
            :throw-exceptions false
            :body (m/encode "application/json" {:roles [(auth0/get-role-id token)]})}
           (http/post (str "https://kelvin-cheffy.us.auth0.com/api/v2/users/" uid "/roles"))))))
