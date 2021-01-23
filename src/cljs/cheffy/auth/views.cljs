(ns cheffy.auth.views
  (:require [cheffy.components :refer [page-nav]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            ["rebass" :refer [Flex Box Button Link]]
            ["@rebass/forms" :refer [Label Input]]))

(defn profile []
  [:<>
   [page-nav {:title "Profile"}]
   [:div "profile"]])

(defn sign-up []
  [:<>
   [page-nav {:title "Sign Up"}]
   [:div "sign-up"]])

(defn login []
  (let [initial-values {:email ""
                        :password ""}
        values (r/atom initial-values)]
    (fn []
      [:<>
       [page-nav {:title "Login"}]
       [:> Box {:as "form"
                :on-submit #(.preventDefault %)
                :width "50%"
                :m "auto"}
        [:> Box
         [:> Label {:html-for "email"
                    :py 2} "Email"]
         [:> Input {:id :email
                    :type "email"
                    :value (:email @values)
                    :on-change #(swap! values assoc :email (.. % -target -value))}]]
        [:> Box
         [:> Label {:html-for "password"
                    :py 2} "Password"]
         [:> Input {:id :password
                    :type "password"
                    :value (:email @values)
                    :on-change #(swap! values assoc :email (.. % -target -value))}]]
        [:> Flex {:justify-content "space-between"
                  :py 2
                  :pr 2}
         [:> Link {:href "#sign-up"
                   :on-click #(rf/dispatch [:set-active-nav :sign-up])}
          "Create an account"]
         [:> Button {:type "submit"
                     :on-click #(js/console.log "login")}
          "Login"]]]])))
