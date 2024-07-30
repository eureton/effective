(ns effective.config.schema-test
  (:require [clojure.test :refer [deftest is are]]
            [malli.core :as m]
            [effective.config.schema :as schema]))

(deftest hook-quoted-function
  (is (m/validate schema/hook 'odd?)))

(deftest hook-symbol-headed-list
  (is (m/validate schema/hook '(constantly true))))

(deftest hook-invalid
  (are [v] (not (m/validate schema/hook v))
       nil
       :x
       "abc"
       odd?
       42
       false
       {:x 42}
       [:x :y]
       #{:x :y}))

(deftest observable-deref
  (is (m/validate schema/observable '@x)))

(deftest observable-form
  (is (m/validate schema/observable '(count x))))

(deftest observable-invalid
  (are [v] (not (m/validate schema/observable v))
       nil
       :x
       "abc"
       odd?
       42
       false
       {:x 42}
       [:x :y]
       #{:x :y}))

(deftest value-range
  (are [radius origin] (m/validate schema/value-range [radius :of origin])
         1   2
       1.2 3.4
         1 3.4
       1.2   3))

(deftest value-range-invalid
  (are [v] (not (m/validate schema/value-range v))
       nil
       :x
       "abc"
       odd?
       42
       false
       {:x 42}
       [:x :y]
       #{:x :y}
       [nil :of 1]
       [1 :of nil]
       [1 :if 2]
       ["1" :of 2]
       [1 :of "2"]
       [:1 :of 2]
       [1 :of :2]
       [[1] :of 2]
       [1 :of [2]]
       ['(1) :of 2]
       [1 :of '(2)]
       [#{1} :of 2]
       [1 :of #{2}]
       [:of 2]
       [1 :of]))

(deftest to-change-without-flags
  (is (not (m/validate schema/to-change {:to-change 'x}))))

(deftest to-change-with-multiple-flags
  (is (m/validate schema/to-change {:to-change 'x
                                    :from 42
                                    :to 142
                                    :by 'even?})))

(deftest to-change-number
  (are [flag] (m/validate schema/to-change {:to-change 'x flag 42})
       :from
       :to
       :by
       :from-lt
       :to-lt
       :by-lt
       :from-lte
       :to-lte
       :by-lte
       :from-gt
       :to-gt
       :by-gt
       :from-gte
       :to-gte
       :by-gte
       :from-not
       :to-not
       :by-not))

(deftest to-change-invalid-numeric-assertions
  (are [flag] (not (m/validate schema/to-change {:to-change 'x flag "abc"}))
       :from-lt
       :to-lt
       :by-lt
       :from-lte
       :to-lte
       :by-lte
       :from-gt
       :to-gt
       :by-gt
       :from-gte
       :to-gte
       :by-gte))

(deftest to-change-from-valid-range
  (is (m/validate schema/to-change {:to-change 'x :from-within [1 :of 2]})))

(deftest to-change-from-invalid-range
  (is (not (m/validate schema/to-change {:to-change 'x :from-within 42}))))
