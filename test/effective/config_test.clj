(ns effective.config-test
  (:require [clojure.test :refer [deftest is]]
            [effective.config :as config]))

(deftest observables
  (is (= [:w :x :y :z]
         (config/observables [{:to-change :w :from odd? :to even?}
                              {:to-conjoin :x :with 12}
                              {:to-pop :y}
                              {:to-not-change :z}]))))
