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
       42
       false
       {:x 42}
       [:x :y]
       #{:x :y}))
