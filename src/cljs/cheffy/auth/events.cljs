(ns cheffy.auth.events
  (:require [re-frame.core :as rf]
            [cljs.reader :refer [read-string]]))

(def cheffy-user-key "cheffy-user")

(defn set-user-ls! [{:keys [auth]}]
  (when (:uid auth)
    (.setItem js/localStorage cheffy-user-key (str auth))))

(defn remove-user-ls! []
  (.removeItem js/localStorage cheffy-user-key))

(rf/reg-cofx
 :local-store-user
 (fn [cofx _]
   (assoc cofx :local-store-user (read-string (.getItem js/localStorage cheffy-user-key)))))

(rf/reg-event-fx
 :login
 [(rf/after set-user-ls!)]
 (fn [{:keys [db]} [_ {:keys [email password]}]]
   (let [user (get-in db [:users email])
         correct-password? (= (get-in user [:profile :password])
                              password)]
     (cond
       (not user)
       {:db (assoc-in db [:errors :email] "User not found")}

       (not correct-password?)
       {:db (assoc-in db [:errors :email] "Wrong password")}

       correct-password?
       {:db (-> db
                (assoc-in [:auth :uid] email)
                (update-in [:errors] dissoc :email))
        :dispatch [:set-active-page :saved]
        :navigate-to {:path "/saved"}}))))

(rf/reg-event-fx
 :sign-up
 [(rf/after set-user-ls!)]
 (fn [{:keys [db]} [_ {:keys [first-name last-name email password]}]]
   {:db (-> db
            (assoc-in [:auth :uid] email)
            (assoc-in [:users email] {:id email
                                      :profile {:first-name first-name
                                                :last-name last-name
                                                :email email
                                                :password password
                                                :img "img/avatar.jpg"}
                                      :saved #{}
                                      :inboxes {}}))
    :dispatch [:set-active-page :saved]
    :navigate-to {:path "/saved"}}))

(rf/reg-event-fx
 :log-out
 [(rf/after remove-user-ls!)]
 (fn [{:keys [db]} _]
   {:db (assoc-in db [:auth :uid] nil)
    :dispatch [:set-active-page :recipes]
    :navigate-to {:path "/recipes"}}))

(rf/reg-event-db
 :update-profile
 (fn [db [_ profile]]
   (let [uid (get-in db [:auth :uid])]
     (update-in db [:users uid :profile] merge (select-keys profile [:first-name :last-name])))))

(rf/reg-event-fx
 :delete-account
 [(rf/after remove-user-ls!)]
 (fn [{:keys [db]} _]
   (let [uid (get-in db [:auth :uid])]
     {:db (-> db
              (assoc-in [:auth :uid] nil)
              (update-in [:users] dissoc uid))
      :dispatch [:set-active-page :recipes]
      :navigate-to {:path "/recipes"}})))
