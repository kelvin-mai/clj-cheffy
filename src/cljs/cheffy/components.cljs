(ns cheffy.components
  (:require ["rebass" :refer [Flex Box Link Text]]))

(defn page-nav [{:keys [left title right]}]
  [:> Flex {:justify-content "space-between"
            :py 3}
   [:> Box
    (when left
      [:> Link {:my 3
                :variant "light"
                :aria-label "Back"
                :href left}])]
   [:> Box
    [:> Text {:font-size 4
              :py 3
              :font-weight 700} title]]
   [:> Box
    (when right
      right)]])
