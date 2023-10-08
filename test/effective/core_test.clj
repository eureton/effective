(ns effective.core-test
  (:require [clojure.test :refer [deftest]]
            [effective.core :refer [effect]]))

(deftest from
  (let [x (atom -1)]
    (effect (swap! x inc)
            [{:changes @x :from -1}])))

(deftest to
  (let [x (atom -1)]
    (effect (swap! x inc)
            [{:changes @x :to 0}])))

(deftest to-lt
  (let [x (atom -1)]
    (effect (swap! x inc)
            [{:changes @x :to-lt 1}])))

(deftest by
  (let [x (atom 4)]
    (effect (swap! x #(* % %))
            [{:changes @x :by 12}])))

(deftest multiple
  (let [x (atom {:a 1 :b 10})]
    (effect (swap! x assoc :a 10 :b 100)
            [{:changes (:a @x) :by 9}
             {:changes (:b @x) :by 90}])))
