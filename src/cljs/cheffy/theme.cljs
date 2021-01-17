(ns cheffy.theme
  (:require ["@rebass/preset" :refer [default]]))

(def cheffy-theme
  {:colors {:black "#243B53"
            :primary "#27AB83"
            :secondary "#F7D070"
            :light "#D9E2EC"}
   :radii {:default "14px"
           :sm "10px"
           :lg "18px"}
   :border-bottom-color {:modal-header "white"}})

(def theme
  (merge-with into
              (js->clj default :keywordize-keys true)
              cheffy-theme))
