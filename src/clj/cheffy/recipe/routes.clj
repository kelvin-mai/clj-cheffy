(ns cheffy.recipe.routes
  (:require [cheffy.middleware :as mw]
            [cheffy.recipe.handlers :as handle]
            [cheffy.responses :as responses]))

(defn routes [env]
  (let [db (:jdbc-url env)]
    ["/recipes" {:swagger {:tags ["recipes"]}
                 :middleware [[mw/wrap-auth0]]}
     ["" {:get {:handler (handle/list-all-recipes db)
                :summary "List all recipes"
                :responses {200 {:body responses/recipes}}}
          :post {:handler (handle/create-recipe! db)
                 :parameters {:body {:name string?
                                     :prep-time number?
                                     :img string?}}
                 :summary "Create recipe"
                 :responses {201 {:body {:recipe-id string?}}}}}]
     ["/:recipe-id"
      ["" {:get {:handler (handle/retreive-recipe db)
                 :parameters {:path {:recipe-id string?}}
                 :summary "Retreive recipe"
                 :responses {200 {:body responses/recipe}}}
           :put {:handler (handle/update-recipe! db)
                 :middleware [[mw/wrap-recipe-owner db]]
                 :parameters {:path {:recipe-id string?}
                              :body {:name string?
                                     :prep-time number?
                                     :img string?}}
                 :summary "Update recipe"
                 :responses {204 {:body nil?}}}
           :delete {:handler (handle/delete-recipe! db)
                    :middleware [[mw/wrap-recipe-owner db]]
                    :parameters {:path {:recipe-id string?}}
                    :responses {204 {:body nil?}}
                    :summary "Delete recipe"}}]
      ["/favorite" {:post {:handler (handle/update-recipe! db)
                           :middleware [[mw/wrap-recipe-owner db]]
                           :parameters {:path {:recipe-id string?}
                                        :body {:name string?
                                               :prep-time number?
                                               :img string?}}
                           :summary "Update recipe"
                           :responses {204 {:body nil?}}}
                    :delete {:handler (handle/delete-recipe! db)
                             :middleware [[mw/wrap-recipe-owner db]]
                             :parameters {:path {:recipe-id string?}}
                             :responses {204 {:body nil?}}
                             :summary "Delete recipe"}}]]]))
