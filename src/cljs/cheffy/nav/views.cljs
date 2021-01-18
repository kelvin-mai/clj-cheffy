(ns cheffy.nav.views
  (:require [re-frame.core :as rf]
            ["rebass" :refer [Flex Box Link]]))

(def nav-items
  [{:id :saved
    :text "Saved"
    :href "#saved"
    :dispatch #(rf/dispatch [:set-active-nav :saved])}
   {:id :recipes
    :text "Recipes"
    :href "#recipes"
    :dispatch #(rf/dispatch [:set-active-nav :recipes])}])

(defn nav-item
  [{:keys [id href text dispatch active-nav]}]
  [:> Link {:href href
            :on-click dispatch
            :ml 2
            :pb 10
            :variant "nav"
            :sx {:border-bottom (when (= active-nav id)
                                  "2px solid #102A43")}}
   text])

(defn authenticated []
  (let [active-nav @(rf/subscribe [:active-nav])]
    [:> Flex {:justify-content "flex-end"
              :py 1}
     (for [item nav-items]
       [nav-item (assoc item :active-nav active-nav :key (:id item))])]))

(defn nav []
  (let [user true]
    (if user
      [authenticated]
      "public")))
