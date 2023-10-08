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

(deftest from-lt
  (let [assertions (make {:from-lt 31} 9)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= 31 expected))
    (is (= 'before-9 actual))))

(deftest from-less-than
  (let [assertions (make {:from-less-than 28} 18)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= 28 expected))
    (is (= 'before-18 actual))))

(deftest from-lte
  (let [assertions (make {:from-lte 68} 7)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= 68 expected))
    (is (= 'before-7 actual))))

(deftest from-less-than-or-equal
  (let [assertions (make {:from-less-than-or-equal -52} 13)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= -52 expected))
    (is (= 'before-13 actual))))

(deftest from-gt
  (let [assertions (make {:from-gt 44} 6)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= 44 expected))
    (is (= 'before-6 actual))))

(deftest from-greater-than
  (let [assertions (make {:from-greater-than -25} 15)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= -25 expected))
    (is (= 'before-15 actual))))

(deftest from-gte
  (let [assertions (make {:from-gte 14} 31)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 14 expected))
    (is (= 'before-31 actual))))

(deftest from-greater-than-or-equal
  (let [assertions (make {:from-greater-than-or-equal 26} 4)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 26 expected))
    (is (= 'before-4 actual))))

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

(deftest to-gt
  (let [assertions (make {:to-gt 23} 3)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= 23 expected))
    (is (= 'after-3 actual))))

(deftest to-greater-than
  (let [assertions (make {:to-greater-than -1} 9)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= -1 expected))
    (is (= 'after-9 actual))))

(deftest to-gte
  (let [assertions (make {:to-gte 91} 4)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 91 expected))
    (is (= 'after-4 actual))))

(deftest to-greater-than-or-equal
  (let [assertions (make {:to-greater-than-or-equal 103} 10)
        [[_ [operator expected actual] _]] assertions]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 103 expected))
    (is (= 'after-10 actual))))

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
