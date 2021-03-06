(ns cheffy.theme
  (:require ["@rebass/preset" :refer [default]]))

(def cheffy-theme
  {:colors {:black "#243B53"
            :primary "#27AB83"
            :secondary "#F7D070"
            :danger "#e3513e"
            :light "#D9E2EC"
            :background "#F0F4F8"}
   :radii {:default "8px"
           :sm "10px"
           :lg "18px"}
   :buttons {:light {:font-size 2
                     :color "black"
                     :border-radius "default"
                     :background-color "light"}
             :danger {:font-size 2
                      :font-weight "bold"
                      :border-radius "default"
                      :background-color "danger"}}
   :forms {:input {:background-color "white"}}
   :variants {:container {:max-width "1024px"
                          :margin "auto"}
              :modal-header {:border-bottom-color "white"}}})

(def theme
  (merge-with into
              (js->clj default :keywordize-keys true)
              cheffy-theme))
