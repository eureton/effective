(ns effective.failure.to-conjoin-test
  (:require [clojure.test :refer [deftest are]]
            [effective.core :refer [expect]]))

(defmacro ^:private failures [flag value]
  `(let [x# (atom [1 2 3])]
     (-> (swap! x# conj 4 5)
         (expect :test [{:to-conjoin @x#
                         ~flag ~value}])
         (get-in [:to-conjoin ~flag] 0))))

(deftest with
  (are [with tally] (= tally (failures :with with))
    [4 5]   0
    [4]     1
    [5]     1
    [5 4]   1
    [4 5 6] 1))

(deftest with-fn
  (are [with tally] (= tally (failures :with-fn with))
    [even? odd?]       0
    [even?]            2
    [odd?]             1
    [odd? even?]       2
    [even? odd? even?] 4))
