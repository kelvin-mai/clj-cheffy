(ns cheffy.nav.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :active-nav
 (fn [db _]
   (get-in db [:nav :active-nav])))

(rf/reg-sub
 :active-page
 (fn [db _]
   (get-in db [:nav :active-page])))
