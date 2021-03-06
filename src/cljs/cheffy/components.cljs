(ns cheffy.components
  (:require ["rebass" :refer [Flex Box Link Heading Text]]
            ["@rebass/forms" :refer [Label Input]]))

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
    [:> Heading {:py 3
                 :font-weight 700} title]]
   [:> Box
    (when right
      right)]])

(defn form-group [{:keys [id label type values]}]
  [:> Box
   [:> Label {:html-for id
              :py 2} label]
   [:> Input {:id id
              :type type
              :value (id @values)
              :on-change #(swap! values assoc id (.. % -target -value))}]])
