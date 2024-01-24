(ns effective.core-test
  (:require [clojure.test :refer [deftest]]
            [effective.core :refer [expect]]))

(deftest from-value
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :from -1}])))

(deftest from-predicate
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :from neg?}])))

(deftest from-lt
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :from-lt 0}])))

(deftest from-lte-equality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-lte -1}])))

(deftest from-lte-inequality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-lte 2}])))

(deftest from-gt
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-gt -2}])))

(deftest from-gte-equality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-gte -1}])))

(deftest from-gte-inequality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-gte -2}])))

(deftest from-not
  (let [x (atom (rand-int 4))]
    (expect (swap! x inc)
            [{:to-change @x :from-not 4}])))

(deftest from-within-of-lower
  (let [x (atom 0)]
    (expect (swap! x inc)
            [{:to-change @x :from-within [0.6 :of -0.05]}])))

(deftest from-within-of-upper
  (let [x (atom 0)]
    (expect (swap! x inc)
            [{:to-change @x :from-within [0.6 :of 0.05]}])))

(deftest to-value
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to 0}])))

(deftest to-function
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to zero?}])))

(deftest to-lt
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-lt 1}])))

(deftest to-lte-equality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :to-lte -2}])))

(deftest to-lte-inequality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :to-lte 0}])))

(deftest to-gt
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-gt -1}])))

(deftest to-gte-equality
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-gte 0}])))

(deftest to-gte-inequality
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-gte -1}])))

(deftest to-not
  (let [x (atom 4)]
    (expect (swap! x #(int (Math/sqrt %)))
            [{:to-change @x :to-not -2}])))

(deftest to-within-of-lower
  (let [x (atom 0)]
    (expect (swap! x inc)
            [{:to-change @x :to-within [0.1 :of 0.91]}])))

(deftest to-within-of-upper
  (let [x (atom 0)]
    (expect (swap! x inc)
            [{:to-change @x :to-within [0.1 :of 1.09]}])))

(deftest by-value
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by 12}])))

(deftest by-function
  (let [x (atom -2)]
    (expect (swap! x #(* % 2))
            [{:to-change @x :by neg?}])))

(deftest by-lt
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-lt 20}])))

(deftest by-lte-equality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-lte 12}])))

(deftest by-lte-inequality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-lte 13}])))

(deftest by-gt
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-gt 11}])))

(deftest by-gte-equality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-gte 12}])))

(deftest by-gte-inequality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-gte 11}])))

(deftest by-not
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-not 4}])))

(deftest by-within-of-lower
  (let [x (atom 10)]
    (expect (swap! x inc)
            [{:to-change @x :by-within [0.1 :of 0.91]}])))

(deftest by-within-of-upper
  (let [x (atom 10)]
    (expect (swap! x inc)
            [{:to-change @x :by-within [0.1 :of 1.09]}])))

(deftest not-change
  (let [x (atom 2)]
    (expect (swap! x #(/ (* % %) 2))
            [{:to-not-change @x}])))

(deftest multiple
  (let [x (atom {:a 1 :b 10})]
    (expect (swap! x assoc :a 10 :b 100)
            [{:to-change (:a @x) :by 9}
             {:to-change (:b @x) :by 90}])))

(deftest multiple-disjunction
  (let [x (atom 1)]
    (expect (swap! x inc)
            :any
            [{:to-change @x :from odd? :to even?}
             {:to-change @x :from 10}])))
