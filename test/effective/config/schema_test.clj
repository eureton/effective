(ns effective.config.schema-test
  (:require [clojure.test :refer [deftest is]]
            [malli.core :as m]
            [effective.config.schema :refer [hook]]))

(deftest hook-quoted-function
  (is (m/validate hook 'odd?)))

(deftest hook-symbol-headed-list
  (is (m/validate hook '(constantly true))))

(deftest hook-function
  (is (not (m/validate hook odd?))))

(deftest hook-number
  (is (not (m/validate hook 42))))

(deftest hook-string
  (is (not (m/validate hook "abc"))))
