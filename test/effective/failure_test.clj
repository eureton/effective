(ns effective.failure-test
  (:require [clojure.test :refer [deftest is]]
            [effective.core :refer [expect]]))

(def ^:private ^:const N 10)

(defmacro ^:private failures [operation flag value]
  `(let [x# (atom N)]
     (-> (swap! x# inc)
         (expect :test [{~operation @x#
                         ~flag ~value}])
         (get-in [~operation ~flag] 0))))

(deftest from-value
  (is (= 1 (failures :to-change :from 9))))

(deftest from-function
  (is (= 1 (failures :to-change :from zero?))))

(deftest from-lt
  (is (= 1 (failures :to-change :from-lt 10))))

(deftest from-lte
  (is (= 1 (failures :to-change :from-lte 9))))

(deftest from-gt
  (is (= 1 (failures :to-change :from-gt 10))))

(deftest from-gte
  (is (= 1 (failures :to-change :from-gte 11))))

(deftest from-within
  (is (= 1 (failures :to-change :from-within [1 :of 8]))))

(deftest from-not
  (is (= 1 (failures :to-change :from-not 10))))

(deftest to-value
  (is (= 1 (failures :to-change :to 10))))

(deftest to-function
  (is (= 1 (failures :to-change :to zero?))))

(deftest to-lt
  (is (= 1 (failures :to-change :to-lt 11))))

(deftest to-lte
  (is (= 1 (failures :to-change :to-lte 10))))

(deftest to-gt
  (is (= 1 (failures :to-change :to-gt 11))))

(deftest to-gte
  (is (= 1 (failures :to-change :to-gte 12))))

(deftest to-within
  (is (= 1 (failures :to-change :to-within [1 :of 13]))))

(deftest to-not
  (is (= 1 (failures :to-change :to-not 11))))
