(ns cheffy.nav.views
  (:require [re-frame.core :as rf]
            ["rebass" :refer [Flex Link]]))

(def public-nav-items
  [{:id :recipes
    :text "Recipes"
    :href "#recipes"}
   {:id :become-a-chef
    :text "Chef"
    :href "#become-a-chef"}
   {:id :sign-up
    :text "Sign Up"
    :href "#sign-up"}
   {:id :login
    :text "Login"
    :href "#login"}])

(def authenticated-nav-items
  [{:id :saved
    :text "Saved"
    :href "#saved"}
   {:id :inbox
    :text "Inbox"
    :href "#inbox"}
   {:id :recipes
    :text "Recipes"
    :href "#recipes"}
   {:id :become-a-chef
    :text "Chef"
    :href "#become-a-chef"}
   {:id :profile
    :name "Profile"
    :href "#profile"}])

(defn nav-item
  [{:keys [id href text active-nav]}]
  [:> Link {:href href
            :on-click #(rf/dispatch [:set-active-nav id])
            :ml 2
            :pb 10
            :variant "nav"
            :sx {:border-bottom (when (= active-nav id)
                                  "2px solid #102A43")}}
   text])

(defn nav-items [items]
  (let [active-nav @(rf/subscribe [:active-nav])]
    [:> Flex {:justify-content "flex-end"
              :py 1}
     (for [item items]
       [nav-item (assoc item :active-nav active-nav :key (:id item))])]))

(defn nav []
  (let [logged-in? @(rf/subscribe [:logged-in?])]
    (if logged-in?
      [nav-items authenticated-nav-items]
      [nav-items public-nav-items])))
