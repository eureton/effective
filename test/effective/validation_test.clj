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
                                                  :to-not-change @x}])))))
