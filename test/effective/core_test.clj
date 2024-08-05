(ns effective.core-test
  (:require [clojure.set :as cljset]
            [clojure.test :refer [deftest]]
            [effective.core :refer [expect]]))

(deftest from-value
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :from -1}])))

(deftest from-predicate
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :from-f neg?}])))

(deftest from-lt
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :from-lt 0}])))

(deftest from-less-than
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :from-less-than 0}])))

(deftest from-lte-equality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-lte -1}])))

(deftest from-less-than-or-equal-equality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-less-than-or-equal -1}])))

(deftest from-lte-inequality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-lte 2}])))

(deftest from-less-than-or-equal-inequality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-less-than-or-equal 2}])))

(deftest from-gt
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-gt -2}])))

(deftest from-greater-than
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-greater-than -2}])))

(deftest from-gte-equality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-gte -1}])))

(deftest from-greater-than-or-equal-equality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-greater-than-or-equal -1}])))

(deftest from-gte-inequality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-gte -2}])))

(deftest from-greater-than-or-equal-inequality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :from-greater-than-or-equal -2}])))

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

(deftest to
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to 0}])))

(deftest to-f
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-f zero?}])))

(deftest to-lt
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-lt 1}])))

(deftest to-less-than
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-less-than 1}])))

(deftest to-lte-equality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :to-lte -2}])))

(deftest to-less-than-or-equal-equality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :to-less-than-or-equal -2}])))

(deftest to-lte-inequality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :to-lte 0}])))

(deftest to-less-than-or-equal-inequality
  (let [x (atom -1)]
    (expect (swap! x dec)
            [{:to-change @x :to-less-than-or-equal 0}])))

(deftest to-gt
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-gt -1}])))

(deftest to-greater-than
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-greater-than -1}])))

(deftest to-gte-equality
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-gte 0}])))

(deftest to-greater-than-or-equal-equality
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-greater-than-or-equal 0}])))

(deftest to-gte-inequality
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-gte -1}])))

(deftest to-greater-than-or-equal-inequality
  (let [x (atom -1)]
    (expect (swap! x inc)
            [{:to-change @x :to-greater-than-or-equal -1}])))

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

(deftest by-less-than
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-less-than 20}])))

(deftest by-lte-equality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-lte 12}])))

(deftest by-less-than-or-equal-equality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-less-than-or-equal 12}])))

(deftest by-lte-inequality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-lte 13}])))

(deftest by-less-than-or-equal-inequality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-less-than-or-equal 13}])))

(deftest by-gt
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-gt 11}])))

(deftest by-greater-than
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-greater-than 11}])))

(deftest by-gte-equality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-gte 12}])))

(deftest by-greater-than-or-equal-equality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-greater-than-or-equal 12}])))

(deftest by-gte-inequality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-gte 11}])))

(deftest by-greater-than-or-equal-inequality
  (let [x (atom 4)]
    (expect (swap! x #(* % %))
            [{:to-change @x :by-greater-than-or-equal 11}])))

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
            [{:to-change @x :from-f odd?}
             {:to-change @x :from 10}])))

(deftest conjoin-vector-with-value
  (let [x (atom [:a :b])]
    (expect (reset! x [:a :b :c])
            [{:to-conjoin @x :with [:c]}])))

(deftest conjoin-vector-with-multiple-values
  (let [x (atom [:a :b])]
    (expect (reset! x [:a :b :c :d])
            [{:to-conjoin @x :with [:c :d]}])))

(deftest conjoin-vector-with-value-and-function
  (let [x (atom [-2 -1])]
    (expect (reset! x [-2 -1 0 1 2])
            [{:to-conjoin @x :with [zero? 1 even?]}])))

(deftest conjoin-list-with-value
  (let [x (atom '(:b :c))]
    (expect (reset! x '(:a :b :c))
            [{:to-conjoin @x :with [:a]}])))

(deftest conjoin-list-with-multiple-values
  (let [x (atom '(:c :d))]
    (expect (reset! x '(:a :b :c :d))
            [{:to-conjoin @x :with [:b :a]}])))

(deftest conjoin-list-with-value-and-function
  (let [x (atom '(1 2))]
    (expect (reset! x '(-2 -1 0 1 2))
            [{:to-conjoin @x :with [zero? -1 (every-pred neg? even?)]}])))

(defn- contains-hash? [h1]
  (fn [h2]
    (cljset/subset? (set h1) (set h2))))

(deftest conjoin-vector-with-function
  (let [x (atom [{:a 1 :w 0 :z -9}
                 {:b 2 :w 0 :z -8}])]
    (expect (reset! x [{:a 1 :w 0 :z -9}
                       {:b 2 :w 0 :z -8}
                       {:c 3 :w 0 :z -7}])
            [{:to-conjoin @x :with [(contains-hash? {:c 3 :z -7})]}])))

(deftest conjoin-list-with-function
  (let [x (atom '({:b 2 :w 0 :z -8}
                  {:c 3 :w 0 :z -7}))]
    (expect (reset! x '({:a 1 :w 0 :z -9}
                        {:b 2 :w 0 :z -8}
                        {:c 3 :w 0 :z -7}))
            [{:to-conjoin @x :with [(contains-hash? {:a 1 :z -9})]}])))

(deftest pop-vector-single
  (let [x (atom [:a :b :c])]
    (expect (reset! x [:a :b])
            [{:to-pop @x}])))

(deftest pop-vector-multiple
  (let [x (atom [:a :b :c :d :e])]
    (expect (reset! x [:a :b])
            [{:to-pop @x :times 3}])))

(deftest pop-list-single
  (let [x (atom '(:a :b :c))]
    (expect (reset! x '(:b :c))
            [{:to-pop @x}])))

(deftest pop-list-multiple
  (let [x (atom '(:a :b :c :d :e))]
    (expect (reset! x '(:d :e))
            [{:to-pop @x :times 3}])))
