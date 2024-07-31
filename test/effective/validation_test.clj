(ns effective.validation-test
  (:require [clojure.test :refer [deftest is]]
            [effective.core :refer [expect]]
            [effective.config :refer [valid?]]))

(defmacro ^:private is-invalid? [form]
  `(is (~'thrown? IllegalArgumentException ~form)))

(deftest no-observable
  (let [x (atom 0)]
    (is-invalid? (expect (swap! x inc) [{}]))))

(deftest multiple-observables
  (let [x (atom 0)]
    (is-invalid? (expect (swap! x inc) [{:to-change @x
                                         :to-not-change @x
                                         :to-conjoin @x}]))
    (is-invalid? (expect (swap! x inc) [{:to-change @x
                                         :to-not-change @x}]))
    (is-invalid? (expect (swap! x inc) [{:to-change @x
                                         :to-conjoin @x}]))
    (is-invalid? (expect (swap! x inc) [{:to-not-change @x
                                         :to-conjoin @x}]))))

(deftest to-conjoin-with-scalar-value
  (is (not (valid? [{:to-conjoin 'x :with 123}]))))

(deftest to-conjoin-with-scalar-function
  (is (not (valid? [{:to-conjoin 'x :with (constantly nil)}]))))

(deftest to-conjoin-with-vector-value
  (is (valid? [{:to-conjoin 'x :with [123]}])))

(deftest to-conjoin-with-vector-function
  (is (valid? [{:to-conjoin 'x :with [(constantly nil)]}])))

(deftest to-conjoin-without-value
  (let [x (atom 0)]
    (is-invalid? (expect (swap! x inc) [{:to-conjoin @x}]))))
