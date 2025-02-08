(ns effective.config.schema-test
  (:require [clojure.test :refer [deftest is are]]
            [malli.core :as m]
            [malli.transform :as mt]
            [effective.config.schema :as schema]))

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

(deftest assertion-with-multiple-observables
  (are [assertion] (not (m/validate schema/assertion assertion))
    {:to-change 'x
     :to-not-change 'x
     :to-conjoin 'x
     :to-pop 'x}
    {:to-change 'x
     :to-not-change 'x
     :to-conjoin 'x}
    {:to-change 'x
     :to-not-change 'x
     :to-pop 'x}
    {:to-change 'x
     :to-conjoin 'x
     :to-pop 'x}
    {:to-not-change 'x
     :to-conjoin 'x
     :to-pop 'x}
    {:to-change 'x
     :to-not-change 'x}
    {:to-change 'x
     :to-conjoin 'x}
    {:to-change 'x
     :to-pop 'x}
    {:to-not-change 'x
     :to-conjoin 'x}
    {:to-not-change 'x
     :to-pop 'x}
    {:to-conjoin 'x
     :to-pop 'x}))

(deftest assertion-with-no-observables
  (is (not (m/validate schema/assertion {:y 'x}))))

(deftest assertion-with-one-observable
  (are [operation] (m/validate schema/assertion {operation 'x})
       :to-change
       :to-not-change
       :to-conjoin
       :to-pop))

(deftest to-change-no-flags
  (is (m/validate schema/to-change {:to-change 'x})))

(deftest to-change-with-multiple-flags
  (is (m/validate schema/to-change {:to-change 'x
                                    :from 42
                                    :to 142
                                    :by 100})))

(deftest to-change-by-not-number
  (are [value] (not (m/validate schema/to-change {:to-change 'x
                                                  :by value}))
    nil
    :x
    "abc"
    odd?
    false
    {:x 42}
    [:x :y]
    #{:x :y}))

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

(deftest to-conjoin-non-empty-vector
  (is (m/validate schema/to-conjoin {:to-conjoin 'x :with [42]})))

(deftest to-conjoin-empty-vector
  (is (not (m/validate schema/to-conjoin {:to-conjoin 'x :with []}))))

(deftest to-conjoin-no-flags
  (is (not (m/validate schema/to-conjoin {:to-conjoin 'x}))))

(deftest to-conjoin-scalar
  (is (not (m/validate schema/to-conjoin {:to-conjoin 'x :with 42}))))

(deftest to-pop-without-times
  (is (= 1 (-> schema/to-pop
               (m/decode {:to-pop 'x} mt/default-value-transformer)
               (:times)))))

(deftest to-pop-with-times
  (is (= 42 (-> schema/to-pop
                (m/decode {:to-pop 'x :times 42} mt/default-value-transformer)
                (:times)))))
