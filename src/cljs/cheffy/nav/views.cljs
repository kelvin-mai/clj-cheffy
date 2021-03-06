(ns cheffy.nav.views
  (:require [re-frame.core :as rf]
            [cheffy.router :as router]
            ["rebass" :refer [Flex Link]]))

(def public-nav-items
  [{:id :recipes
    :text "Recipes"}
   {:id :become-a-chef
    :text "Chef"}
   {:id :sign-up
    :text "Sign Up"}
   {:id :login
    :text "Login"}])

(def authenticated-nav-items
  [{:id :saved
    :text "Saved"}
   {:id :inboxes
    :text "Inbox"}
   {:id :recipes
    :text "Recipes"}
   {:id :become-a-chef
    :text "Chef"}
   {:id :profile
    :name "Profile"}])

(defn nav-item [{:keys [id text active-page]}]
  [:> Link {:href (router/path-for id)
            :on-click #(rf/dispatch [:set-active-nav id])
            :ml 2
            :pb 10
            :variant "nav"
            :sx {:border-bottom (when (= active-page id)
                                  "2px solid #102A43")}}
   text])

(defn nav-items [items]
  (let [active-page @(rf/subscribe [:active-page])]
    [:> Flex {:justify-content "flex-end"
              :py 1}
     (for [item items]
       [nav-item (assoc item :active-page active-page :key (:id item))])]))

(defn nav []
  (let [logged-in? @(rf/subscribe [:logged-in?])]
    (if logged-in?
      [nav-items authenticated-nav-items]
      [nav-items public-nav-items])))
