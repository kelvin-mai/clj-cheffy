(ns cheffy.account.routes
  (:require [cheffy.account.handlers :as handle]
            [cheffy.middleware :as mw]))

(defn routes [env]
  (let [db (:jdbc-url env)]
    ["/account" {:swagger {:tags ["account"]}
                 :middleware [[mw/wrap-auth0]]}
     ["" {:post {:handler (handle/create-account! db)
                 :responses {204 {:body nil?}}
                 :summary "Create account"}
          :put {:handler (handle/update-role-to-cook!)
                :responses {204 {:body nil?}}
                :summary "Update account role to cook"}
          :delete {:handler (handle/delete-account! db)
                   :responses {204 {:body nil?}}
                   :summary "Delete account"}}]]))
