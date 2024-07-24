(ns effective.validation-test
  (:require [clojure.test :refer [deftest is]]
            [effective.core :refer [expect]]
            [effective.validation :refer [config-valid?]]))

(deftest no-observable
  (let [x (atom 0)]
    (is (thrown-with-msg? IllegalArgumentException
                          #"(?i)invalid"
                          (expect (swap! x inc) [{}])))))

(deftest multiple-observables
  (let [x (atom 0)]
    (is (thrown-with-msg? IllegalArgumentException
                          #"(?i)invalid"
                          (expect (swap! x inc) [{:to-change @x
                                                  :to-not-change @x
                                                  :to-conjoin @x}])))
    (is (thrown-with-msg? IllegalArgumentException
                          #"(?i)invalid"
                          (expect (swap! x inc) [{:to-change @x
                                                  :to-not-change @x}])))
    (is (thrown-with-msg? IllegalArgumentException
                          #"(?i)invalid"
                          (expect (swap! x inc) [{:to-change @x
                                                  :to-conjoin @x}])))
    (is (thrown-with-msg? IllegalArgumentException
                          #"(?i)invalid"
                          (expect (swap! x inc) [{:to-not-change @x
                                                  :to-conjoin @x}])))))

(deftest to-conjoin-with-scalar-value
  (is (not (config-valid? [{:to-conjoin :x :with 123}]))))

(deftest to-conjoin-with-scalar-function
  (is (not (config-valid? [{:to-conjoin :x :with (constantly nil)}]))))

(deftest to-conjoin-with-vector-value
  (is (config-valid? [{:to-conjoin :x :with [123]}])))

(deftest to-conjoin-with-vector-function
  (is (config-valid? [{:to-conjoin :x :with [(constantly nil)]}])))

(deftest to-conjoin-without-value
  (let [x (atom 0)]
    (is (thrown-with-msg? IllegalArgumentException
                          #"(?i)invalid"
                          (expect (swap! x inc) [{:to-conjoin @x}])))))
