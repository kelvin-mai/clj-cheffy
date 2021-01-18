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
                 :middleware [[mw/wrap-manage-recipes]]
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
                 :middleware [[mw/wrap-recipe-owner db]
                              [mw/wrap-manage-recipes]]
                 :parameters {:path {:recipe-id string?}
                              :body {:name string?
                                     :prep-time number?
                                     :img string?}}
                 :summary "Update recipe"
                 :responses {204 {:body nil?}}}
           :delete {:handler (handle/delete-recipe! db)
                    :middleware [[mw/wrap-recipe-owner db]
                                 [mw/wrap-manage-recipes]]
                    :parameters {:path {:recipe-id string?}}
                    :responses {204 {:body nil?}}
                    :summary "Delete recipe"}}]
      ["/favorite" {:post {:handler (handle/favorite-recipe! db)
                           :parameters {:path {:recipe-id string?}}
                           :summary "Favorite recipe"
                           :responses {204 {:body nil?}}}
                    :delete {:handler (handle/unfavorite-recipe! db)
                             :parameters {:path {:recipe-id string?}}
                             :responses {204 {:body nil?}}
                             :summary "Unfavorite recipe"}}]
      ["/ingredients" {:middleware [[mw/wrap-recipe-owner db]
                                    [mw/wrap-manage-recipes]]
                       :post {:parameters {:path {:recipe-id string?}
                                           :body {:name string?
                                                  :amount number?
                                                  :measure string?
                                                  :sort number?}}
                              :handler (handle/create-ingredient! db)
                              :summary "Create ingredient"
                              :responses {201 {:body {:ingredient-id string?}}}}
                       :put {:parameters {:path {:recipe-id string?}
                                          :body {:ingredient-id string?
                                                 :name string?
                                                 :amount number?
                                                 :measure string?
                                                 :sort number?}}
                             :handler (handle/update-ingredient! db)
                             :summary "Update ingredient"
                             :responses {204 {:body nil?}}}
                       :delete {:parameters {:path {:recipe-id string?}
                                             :body {:ingredient-id string?}}
                                :handler (handle/delete-ingredient! db)
                                :summary "Delete ingredient"
                                :responses {204 {:body nil?}}}}]
      ["/steps" {:middleware [[mw/wrap-recipe-owner db]
                              [mw/wrap-manage-recipes]]
                 :post {:parameters {:path {:recipe-id string?}
                                     :body {:description string?
                                            :sort number?}}
                        :handler (handle/create-step! db)
                        :summary "Create step"
                        :responses {201 {:body {:step-id string?}}}}
                 :put {:parameters {:path {:recipe-id string?}
                                    :body {:step-id string?
                                           :description string?
                                           :sort number?}}
                       :handler (handle/update-step! db)
                       :summary "Update step"
                       :responses {204 {:body nil?}}}
                 :delete {:parameters {:path {:recipe-id string?}
                                       :body {:step-id string?}}
                          :handler (handle/delete-step! db)
                          :summary "Delete step"
                          :responses {204 {:body nil?}}}}]]]))
