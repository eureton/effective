(ns effective.failure.to-pop-test
  (:require [clojure.test :refer [deftest is are]]
            [effective.core :refer [expect]]))

(defmacro ^:private failures [effect-fn flag value]
  `(let [x# (atom [1 2 3])]
     (-> (swap! x# ~effect-fn)
         (expect :test [{:to-pop @x#
                         ~flag ~value}])
         (get-in [:to-pop ~flag] 0))))

(deftest not-popped
  (is (= 1 (failures identity :times 1))))

(deftest popped-twice
  (are [times tally] (= tally (failures (comp pop pop) :times times))
    0 1
    1 1
    3 1))
