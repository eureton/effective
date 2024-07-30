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
