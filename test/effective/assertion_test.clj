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

(deftest to
  (let [assertions (make {:to -2} 2)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'=) operator))
    (is (= -2 expected))
    (is (= 'after-2 actual))))

(deftest by
  (let [assertions (make {:by 10} 0)
        [[_ [operator expected actual] _]] assertions
        diff (nth actual 0 nil)
        after (nth actual 1 nil)
        before (nth actual 2 nil)]
    (is (= 1 (count assertions)))
    (is (= (symbol #'=) operator))
    (is (= 10 expected))
    (is (= (symbol #'-) diff))
    (is (= 'before-0 before))
    (is (= 'after-0 after))))

(deftest multiple
  (let [assertions (make {:from 1 :to 21 :by 20} 7)
        [from-assertion to-assertion by-assertion] assertions]
    (is (= 3 (count assertions)))
    (is (= 1 (-> from-assertion (nth 1 nil) (nth 1 nil))))
    (is (= 21 (-> to-assertion (nth 1 nil) (nth 1 nil))))
    (is (= 20 (-> by-assertion (nth 1 nil) (nth 1 nil))))))
