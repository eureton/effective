(ns effective.failure.to-not-change-test
  (:require [clojure.test :refer [deftest is]]
            [effective.core :refer [expect]]))

(deftest to-not-change
  (let [x (atom 10)]
    (is (= 1 (-> (swap! x inc)
                 (expect :test [{:to-not-change @x}])
                 (get-in [:to-not-change :to-not-change] 0))))))
