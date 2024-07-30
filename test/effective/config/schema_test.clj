(ns effective.config.schema-test
  (:require [clojure.test :refer [deftest is]]
            [malli.core :as m]
            [effective.config.schema :refer [hook]]))

(deftest hook-function
  (is (m/validate hook 'odd?)))

(deftest hook-symbol-headed-list
  (is (m/validate hook '(constantly true))))
