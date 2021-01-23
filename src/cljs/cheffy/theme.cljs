(ns cheffy.theme
  (:require ["@rebass/preset" :refer [default]]))

(def cheffy-theme
  {:colors {:black "#243B53"
            :primary "#27AB83"
            :secondary "#F7D070"
            :light "#D9E2EC"
            :background "#F0F4F8"}
   :radii {:default "14px"
           :sm "10px"
           :lg "18px"}
   :forms {:input {:background-color "white"}}
   :variants {:container {:max-width "1024px"
                          :margin "auto"}
              :modal-header {:border-bottom-color "white"}}})

(def theme
  (merge-with into
              (js->clj default :keywordize-keys true)
              cheffy-theme))
