(ns cheffy.main
  (:require [cheffy.router :as router]
            [cheffy.auth.events]
            [cheffy.auth.subs]
            [cheffy.auth.views :refer [profile sign-up login]]
            [cheffy.db]
            [cheffy.nav.events]
            [cheffy.nav.subs]
            [cheffy.nav.views :refer [nav]]
            [cheffy.theme :refer [theme]]
            [re-frame.core :as rf]
            [reagent.dom :refer [render]]
            ["theme-ui" :refer [ThemeProvider]]
            ["rebass" :refer [Box]]))

(defn pages [page-name]
  (js/console.log "page-name" page-name)
  (case page-name
    :sign-up [sign-up]
    :login [login]
    :profile [profile]
    :become-a-chef nil
    :inbox nil
    :recipes [profile]
    :saved nil
    [profile]))

(defn app []
  (let [active-page @(rf/subscribe [:active-page])]
    [:> ThemeProvider {:theme theme}
     [:> Box {:variant "container"}
      [nav]
      [pages active-page]]]))

(defn start []
  (router/start!)
  (rf/dispatch-sync [:initialize-db])
  (render [app] (.getElementById js/document "root")))

(defn ^:export ^:dev/after-load init []
  (start))

(comment
  (js/console.log theme)
  (js/console.log (:nav @re-frame.db/app-db))
  (js/console.log re-frame.db/app-db))
