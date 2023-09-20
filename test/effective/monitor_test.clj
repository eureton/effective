(ns effective.monitor-test
  (:require [clojure.test :refer [deftest is]]
            [effective.predicate :refer [collect]]))

(deftest single
  (let [x (atom {:a 1 :b 0})
        predicates (collect (swap! x update :a inc)
                            [{:changes (:a @x)
                              :to 2}])]
    (is (= 1 (count predicates)))))

(deftest multiple
  (let [x (atom {:a 1 :b 0})
        predicates (collect (swap! x assoc :a -1 :b 10)
                            [{:changes (:a @x)
                              :to -1}
                             {:changes (:b @x)
                              :to 10}])]
    (is (= 2 (count predicates)))))
