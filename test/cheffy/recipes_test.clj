(ns cheffy.recipes-test
  (:require [cheffy.core :refer :all]
            [cheffy.test-system :as ts]
            [clojure.test :refer :all]))

(use-fixtures :once ts/token-fixture)

(def recipe-id (atom nil))
(def step-id (atom nil))
(def ingredient-id (atom nil))

(reset! recipe-id "5d4439bd-9538-44bf-a7b4-45432b4cad52")

(def recipe
  {:img ""
   :prep-time 30
   :name "Test Recipe"})

(def step
  {:description "My Test Step"
   :sort 1})

(def ingredient
  {:amount 2
   :measure "30 grams"
   :sort 1
   :name "My test ingredient"})

(deftest recipes-test
  (testing "List recipes"
    (testing "with auth -- public and drafts"
      (let [{:keys [status body]} (ts/test-endpoint :get "/v1/recipes" {:auth true})]
        (is (= 200 status))
        (is (vector? (:public body)))
        (is (vector? (:drafts body)))))
    (testing "without auth -- public"
      (let [{:keys [status body]} (ts/test-endpoint :get "/v1/recipes")]
        (is (= 200 status))
        (is (vector? (:public body)))
        (is (nil? (:drafts body)))))))

(deftest recipe-test
  (testing "Create recipe"
    (let [{:keys [status body]} (ts/test-endpoint :post "/v1/recipes" {:auth true :body recipe})]
      (reset! recipe-id (:recipe-id body))
      (is (= status 201))))
  (testing "Update recipe"
    (let [{:keys [status body]} (ts/test-endpoint :put (str "/v1/recipes/" @recipe-id) {:auth true :body recipe})]
      (is (= status 204))))
  (testing "Favorite recipe"
    (let [{:keys [status body]} (ts/test-endpoint :post (str "/v1/recipes/" @recipe-id "/favorite") {:auth true})]
      (is (= status 204))))
  (testing "Unfavorite recipe"
    (let [{:keys [status body]} (ts/test-endpoint :delete (str "/v1/recipes/" @recipe-id "/favorite") {:auth true})]
      (is (= status 204))))
  (testing "Create step"
    (let [{:keys [status body]} (ts/test-endpoint :post (str "/v1/recipes/" @recipe-id "/steps")
                                                  {:auth true :body step})]
      (reset! step-id (:step-id body))
      (is (= status 201))))
  (testing "Update step"
    (let [{:keys [status body]} (ts/test-endpoint :put (str "/v1/recipes/" @recipe-id "/steps")
                                                  {:auth true :body {:step-id @step-id
                                                                     :sort 2
                                                                     :description "Updated step"}})]
      (is (= status 204))))
  (testing "Delete step"
    (let [{:keys [status body]} (ts/test-endpoint :delete (str "/v1/recipes/" @recipe-id "/steps")
                                                  {:auth true :body {:step-id @step-id}})]
      (is (= status 204))))
  (testing "Create ingredient"
    (let [{:keys [status body]} (ts/test-endpoint :post (str "/v1/recipes/" @recipe-id "/ingredients")
                                                  {:auth true :body ingredient})]
      (reset! ingredient-id (:ingredient-id body))
      (is (= status 201))))
  (testing "Update ingredient"
    (let [{:keys [status body]} (ts/test-endpoint :put (str "/v1/recipes/" @recipe-id "/ingredients")
                                                  {:auth true :body {:ingredient-id @ingredient-id
                                                                     :name "My updated name"
                                                                     :amount 5
                                                                     :measure "50 grams"
                                                                     :sort 3}})]
      (is (= status 204))))
  (testing "Delete ingredient"
    (let [{:keys [status body]} (ts/test-endpoint :delete (str "/v1/recipes/" @recipe-id "/ingredients")
                                                  {:auth true :body {:ingredient-id @ingredient-id}})]
      (is (= status 204))))
  (testing "Delete recipe"
    (let [{:keys [status body]} (ts/test-endpoint :delete (str "/v1/recipes/" @recipe-id) {:auth true})]
      (is (= status 204)))))

(comment
  (ts/test-endpoint :get "/v1/recipes" {:auth true})
  (ts/test-endpoint :delete "/v1/recipes/a1995316-80ea-4a98-939d-7c6295e4bb46/favorite" {:auth true})
  (ts/test-endpoint :post "/v1/recipes" {:auth true :body recipe})
  (ts/test-endpoint :delete (str "/v1/recipes/" recipe-id) {:auth true})
  (run-tests))
