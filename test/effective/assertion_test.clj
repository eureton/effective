(ns effective.assertion-test
  (:require [clojure.test :refer [deftest is]]
            [effective.assertion :refer [make]]))

(deftest from
  (let [assertions (make {:to-change nil :from 4} 3)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'=) operator))
    (is (= 4 expected))
    (is (= 'before-3 actual))))

(deftest from-lt
  (let [assertions (make {:to-change nil :from-lt 31} 9)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= 31 expected))
    (is (= 'before-9 actual))))

(deftest from-less-than
  (let [assertions (make {:to-change nil :from-less-than 28} 18)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= 28 expected))
    (is (= 'before-18 actual))))

(deftest from-lte
  (let [assertions (make {:to-change nil :from-lte 68} 7)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= 68 expected))
    (is (= 'before-7 actual))))

(deftest from-less-than-or-equal
  (let [assertions (make {:to-change nil :from-less-than-or-equal -52} 13)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= -52 expected))
    (is (= 'before-13 actual))))

(deftest from-gt
  (let [assertions (make {:to-change nil :from-gt 44} 6)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= 44 expected))
    (is (= 'before-6 actual))))

(deftest from-greater-than
  (let [assertions (make {:to-change nil :from-greater-than -25} 15)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= -25 expected))
    (is (= 'before-15 actual))))

(deftest from-gte
  (let [assertions (make {:to-change nil :from-gte 14} 31)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 14 expected))
    (is (= 'before-31 actual))))

(deftest from-greater-than-or-equal
  (let [assertions (make {:to-change nil :from-greater-than-or-equal 26} 4)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 26 expected))
    (is (= 'before-4 actual))))

(deftest from-not
  (let [assertions (make {:to-change nil :from-not 228} 42)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'not=) operator))
    (is (= 228 expected))
    (is (= 'before-42 actual))))

(deftest to
  (let [assertions (make {:to-change nil :to -2} 2)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'=) operator))
    (is (= -2 expected))
    (is (= 'after-2 actual))))

(deftest to-lt
  (let [assertions (make {:to-change nil :to-lt 39} 2)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= 39 expected))
    (is (= 'after-2 actual))))

(deftest to-less-than
  (let [assertions (make {:to-change nil :to-less-than -17} 2)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= -17 expected))
    (is (= 'after-2 actual))))

(deftest to-lte
  (let [assertions (make {:to-change nil :to-lte 74} 8)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= 74 expected))
    (is (= 'after-8 actual))))

(deftest to-less-than-or-equal
  (let [assertions (make {:to-change nil :to-less-than-or-equal -5} 0)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= -5 expected))
    (is (= 'after-0 actual))))

(deftest to-gt
  (let [assertions (make {:to-change nil :to-gt 23} 3)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= 23 expected))
    (is (= 'after-3 actual))))

(deftest to-greater-than
  (let [assertions (make {:to-change nil :to-greater-than -1} 9)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= -1 expected))
    (is (= 'after-9 actual))))

(deftest to-gte
  (let [assertions (make {:to-change nil :to-gte 91} 4)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 91 expected))
    (is (= 'after-4 actual))))

(deftest to-greater-than-or-equal
  (let [assertions (make {:to-change nil :to-greater-than-or-equal 103} 10)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 103 expected))
    (is (= 'after-10 actual))))

(deftest to-not
  (let [assertions (make {:to-change nil :to-not 121} 22)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'not=) operator))
    (is (= 121 expected))
    (is (= 'after-22 actual))))

(deftest by
  (let [assertions (make {:to-change nil :by 10} 0)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'=) operator))
    (is (= 10 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-0 (nth actual 1 nil)))
    (is (= 'before-0 (nth actual 2 nil)))))

(deftest by-lt
  (let [assertions (make {:to-change nil :by-lt 39} 16)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= 39 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-16 (nth actual 1 nil)))
    (is (= 'before-16 (nth actual 2 nil)))))

(deftest by-less-than
  (let [assertions (make {:to-change nil :by-less-than -8} 24)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>) operator))
    (is (= -8 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-24 (nth actual 1 nil)))
    (is (= 'before-24 (nth actual 2 nil)))))

(deftest by-lte
  (let [assertions (make {:to-change nil :by-lte 43} 34)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= 43 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-34 (nth actual 1 nil)))
    (is (= 'before-34 (nth actual 2 nil)))))

(deftest by-less-than-or-equal
  (let [assertions (make {:to-change nil :by-less-than-or-equal 66} 26)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'>=) operator))
    (is (= 66 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-26 (nth actual 1 nil)))
    (is (= 'before-26 (nth actual 2 nil)))))

(deftest by-gt
  (let [assertions (make {:to-change nil :by-gt 40} 38)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= 40 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-38 (nth actual 1 nil)))
    (is (= 'before-38 (nth actual 2 nil)))))

(deftest by-greater-than
  (let [assertions (make {:to-change nil :by-greater-than -10} 29)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<) operator))
    (is (= -10 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-29 (nth actual 1 nil)))
    (is (= 'before-29 (nth actual 2 nil)))))

(deftest by-gte
  (let [assertions (make {:to-change nil :by-gte 51} 33)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 51 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-33 (nth actual 1 nil)))
    (is (= 'before-33 (nth actual 2 nil)))))

(deftest by-greater-than-or-equal
  (let [assertions (make {:to-change nil :by-greater-than-or-equal 52} 46)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'<=) operator))
    (is (= 52 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-46 (nth actual 1 nil)))
    (is (= 'before-46 (nth actual 2 nil)))))

(deftest by-not
  (let [assertions (make {:to-change nil :by-not 71} 49)
        [operator expected actual] (:predicate (first assertions))]
    (is (= 1 (count assertions)))
    (is (= (symbol #'not=) operator))
    (is (= 71 expected))
    (is (= (symbol #'-) (nth actual 0 nil)))
    (is (= 'after-49 (nth actual 1 nil)))
    (is (= 'before-49 (nth actual 2 nil)))))

(deftest multiple
  (let [assertions (make {:to-change nil :from 1 :to 21 :by 20} 7)
        [from-assertion to-assertion by-assertion] assertions]
    (is (= 3 (count assertions)))
    (is (= 1 (-> from-assertion :predicate (nth 1 nil))))
    (is (= 21 (-> to-assertion :predicate (nth 1 nil))))
    (is (= 20 (-> by-assertion :predicate (nth 1 nil))))))
