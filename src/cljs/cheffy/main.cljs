(ns cheffy.main
  (:require [cheffy.db]
            [cheffy.nav.views :refer [nav]]
            [cheffy.theme :refer [theme]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [reagent.dom :refer [render]]
            ["theme-ui" :refer [ThemeProvider]]))

(defn app []
  [:> ThemeProvider {:theme theme}
   [nav]])

(defn start []
  (rf/dispatch-sync [:initialize-db])
  (render [app] (.getElementById js/document "root")))

(defn ^:export ^:dev/after-load init []
  (js/console.log theme)
  (start))
