(ns effective.predicate-test
  (:require [clojure.test :refer [deftest is]]
            [effective.predicate :refer [collect]]))

(deftest from
  (let [x (atom [:a :b :c])
        predicates (collect (swap! x pop)
                            [{:changes (count @x)
                              :from 4}])
        [expected actual] (first predicates)]
    (is (= 1 (count predicates)))
    (is (= 4 expected))
    (is (= 3 actual))))

(deftest to
  (let [x (atom [:a :b :c])
        predicates (collect (swap! x pop)
                            [{:changes (count @x)
                              :to 1}])
        [expected actual] (first predicates)]
    (is (= 1 (count predicates)))
    (is (= 1 expected))
    (is (= 2 actual))))

(deftest by
  (let [x (atom 2)
        predicates (collect (swap! x #(* % 10))
                            [{:changes @x
                              :by 8}])
        [expected actual] (first predicates)]
    (is (= 1 (count predicates)))
    (is (= 8 expected))
    (is (= 18 actual))))

(deftest multiple
  (let [x (atom 2)
        predicates (collect (swap! x #(* % 10))
                            [{:changes @x
                              :from 2
                              :by 8}])
        expected-values (map first predicates)]
    (is (= 2 (count predicates)))
    (is (some #{2} expected-values))
    (is (some #{8} expected-values))))
