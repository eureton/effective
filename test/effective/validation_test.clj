(ns effective.validation-test
  (:require [clojure.test :refer [deftest is]]
            [effective.core :refer [expect]]))

(defmacro ^:private is-invalid? [form]
  `(is (~'thrown? IllegalArgumentException ~form)))

(deftest no-observable
  (let [x (atom 0)]
    (is-invalid? (expect (swap! x inc) [{}]))))

(deftest multiple-observables
  (is-invalid? (expect 'x [{:to-change 'x
                            :to-not-change 'x
                            :to-conjoin 'x
                            :to-pop 'x}]))
  (is-invalid? (expect 'x [{:to-change 'x
                            :to-not-change 'x
                            :to-conjoin 'x}]))
  (is-invalid? (expect 'x [{:to-change 'x
                            :to-not-change 'x
                            :to-pop 'x}]))
  (is-invalid? (expect 'x [{:to-change 'x
                            :to-conjoin 'x
                            :to-pop 'x}]))
  (is-invalid? (expect 'x [{:to-not-change 'x
                            :to-conjoin 'x
                            :to-pop 'x}]))
  (is-invalid? (expect 'x [{:to-change 'x
                            :to-not-change 'x}]))
  (is-invalid? (expect 'x [{:to-change 'x
                            :to-conjoin 'x}]))
  (is-invalid? (expect 'x [{:to-change 'x
                            :to-pop 'x}]))
  (is-invalid? (expect 'x [{:to-not-change 'x
                            :to-conjoin 'x}]))
  (is-invalid? (expect 'x [{:to-not-change 'x
                            :to-pop 'x}]))
  (is-invalid? (expect 'x [{:to-conjoin 'x
                            :to-pop 'x}])))
