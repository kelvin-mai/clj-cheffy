(ns cheffy.main
  (:require [reagent.core :as r]
            [reagent.dom :refer [render]]))

(defn app []
  [:div "Cheffy"])

(defn ^:export ^:dev/after-load init []
  (render [app] (.getElementById js/document "root")))
