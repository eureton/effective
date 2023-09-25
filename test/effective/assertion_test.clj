(ns effective.assertion-test
  (:require [clojure.test :refer [deftest is]]
            [effective.assertion :refer [make]]))

(deftest from
  (let [assertions (make {:from 4} 3)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'=) operator))
    (is (= 4 expected))
    (is (= 'before-3 actual))))
