(ns cheffy.nav.events
  (:require [re-frame.core :as rf]
            [cheffy.router :as router]))

(rf/reg-fx
 :navigate-to
 (fn [{:keys [path]}]
   (router/set-token! path)))

(rf/reg-event-db
 :set-active-nav
 (fn [db [_ active-nav]]
   (assoc-in db [:nav :active-nav] active-nav)))

(rf/reg-event-db
 :set-active-page
 (fn [db [_ active-page]]
   (assoc-in db [:nav :active-page] active-page)))

(rf/reg-event-db
 :route-changed
 (fn [db [_ {:keys [handler]}]]
   (assoc-in db [:nav :active-page] handler)))

