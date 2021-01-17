(ns cheffy.nav.views
  (:require ["rebass" :refer [Flex Box]]))

(defn nav-item
  [{:keys [id href text]}]
  [:> Box {:key id
           :as "a"
           :href href
           :ml 2
           :pb 10}
   text])

(defn authenticated []
  (let [nav-items [{:id :saved
                    :text "Saved"
                    :href "#saved"}
                   {:id :recipes
                    :text "Recipes"
                    :href "#recipes"}]]
    [:> Flex {:justify-content "flex-end"
              :py 1}
     (for [item nav-items]
       [nav-item item])]))

(defn nav []
  (let [user true]
    (if user
      [authenticated]
      "public")))
