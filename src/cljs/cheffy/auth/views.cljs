(ns cheffy.auth.views
  (:require [cheffy.components :refer [page-nav form-group]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            ["rebass" :refer [Flex Heading Box Button Link]]))

(defn profile []
  (let [{:keys [first-name last-name]} @(rf/subscribe [:active-user-profile])
        initial-values {:first-name first-name :last-name last-name}
        values (r/atom initial-values)]
    (fn []
      [:<>
       [page-nav {:title "Profile"
                  :right [:> Button {:variant "light"
                                     :on-click #(rf/dispatch [:log-out])}
                          "Log out"]}]
       [:> Box {:as "form"
                :width "50%"
                :m "auto"
                :on-submit #(.preventDefault %)}
        [:> Box {:background-color "white"
                 :border-radius 4
                 :p 3
                 :pt 1}
         [:> Heading {:as "h4"
                      :py 10
                      :font-weight 700}
          "Personal Info"]
         [form-group {:id :first-name
                      :label "First name"
                      :type "text"
                      :values values}]
         [form-group {:id :last-name
                      :label "Last name"
                      :type "text"
                      :values values}]
         [:> Flex {:justify-content "flex-end"
                   :pt 2}
          [:> Button {:type "submit"
                      :on-click #(rf/dispatch [:update-profile @values])}
           "Save"]]]
        [:> Box {:background-color "white"
                 :border-radius 4
                 :p 3
                 :pt 1
                 :mt 4}
         [:> Heading {:as "h4"
                      :py 10
                      :font-weight 700}
          "Danger Zone"]
         [:> Button {:variant "danger"
                     :on-click #(rf/dispatch [:delete-account])}
          "Delete Account"]]]])))

(defn sign-up []
  (let [initial-values {:first-name ""
                        :last-name ""
                        :email ""
                        :password ""}
        values (r/atom initial-values)]
    (fn []
      [:<>
       [page-nav {:title "Sign Up"}]
       [:> Box {:as "form"
                :on-submit #(.preventDefault %)
                :width "50%"
                :m "auto"}
        [form-group {:id :first-name
                     :label "First name"
                     :type "text"
                     :values values}]
        [form-group {:id :last-name
                     :label "Last name"
                     :type "text"
                     :values values}]
        [form-group {:id :email
                     :label "Email"
                     :type "email"
                     :values values}]
        [form-group {:id :password
                     :label "Password"
                     :type "password"
                     :values values}]
        [:> Flex {:justify-content "space-between"
                  :py 2
                  :pr 2}
         [:> Link {:href "#log-in"
                   :on-click #(rf/dispatch [:set-active-nav :login])}
          "Login instead?"]
         [:> Button {:type "submit"
                     :on-click #(rf/dispatch [:sign-up @values])}
          "Sign Up"]]]])))

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
        [form-group {:id :email
                     :label "Email"
                     :type "email"
                     :values values}]
        [form-group {:id :password
                     :label "Password"
                     :type "password"
                     :values values}]
        [:> Flex {:justify-content "space-between"
                  :py 2
                  :pr 2}
         [:> Link {:href "#sign-up"
                   :on-click #(rf/dispatch [:set-active-nav :sign-up])}
          "Create an account"]
         [:> Button {:type "submit"
                     :on-click #(rf/dispatch [:login @values])}
          "Login"]]]])))
