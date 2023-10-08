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

(deftest to-lt
  (let [assertions (make {:to-lt 39} 2)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= 39 expected))
    (is (= 'after-2 actual))))

(deftest to-less-than
  (let [assertions (make {:to-less-than -17} 2)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= -17 expected))
    (is (= 'after-2 actual))))

(deftest to-lte
  (let [assertions (make {:to-lte 74} 8)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= 74 expected))
    (is (= 'after-8 actual))))

(deftest to-less-than-or-equal
  (let [assertions (make {:to-less-than-or-equal -5} 0)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= -5 expected))
    (is (= 'after-0 actual))))

(deftest by
  (let [assertions (make {:by 10} 0)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'=) operator))
    (is (= 10 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-0 (nth actual 1 nil)))
    (is (= 'before-0 (nth actual 2 nil)))))

(deftest multiple
  (let [assertions (make {:from 1 :to 21 :by 20} 7)
        [from-assertion to-assertion by-assertion] assertions]
    (is (= 3 (count assertions)))
    (is (= 1 (-> from-assertion (nth 1 nil) (nth 1 nil))))
    (is (= 21 (-> to-assertion (nth 1 nil) (nth 1 nil))))
    (is (= 20 (-> by-assertion (nth 1 nil) (nth 1 nil))))))
