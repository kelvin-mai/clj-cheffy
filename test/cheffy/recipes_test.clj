(ns cheffy.recipes-test
  (:require [cheffy.core :refer :all]
            [cheffy.test-system :as ts]
            [clojure.test :refer :all]))

(use-fixtures :once ts/token-fixture)

(def recipe-id (atom nil))

(def recipe
  {:img ""
   :prep-time 30
   :name "Test Recipe"})

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
  (testing "Delete recipe"
    (let [{:keys [status body]} (ts/test-endpoint :delete (str "/v1/recipes/" @recipe-id) {:auth true})]
      (is (= status 204)))))

(comment
  (ts/test-endpoint :get "/v1/recipes" {:auth true})
  (ts/test-endpoint :post "/v1/recipes" {:auth true :body recipe})
  (ts/test-endpoint :delete (str "/v1/recipes/" recipe-id) {:auth true})
  (run-tests))
