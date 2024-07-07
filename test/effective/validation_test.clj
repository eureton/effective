(ns effective.validation-test
  (:require [clojure.test :refer [deftest is]]
            [effective.core :refer [expect]]))

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

(deftest to-conjoin-without-value
  (let [x (atom 0)]
    (is (thrown-with-msg? IllegalArgumentException
                          #"(?i)invalid"
                          (expect (swap! x inc) [{:to-conjoin @x}])))))
