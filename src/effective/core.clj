(ns effective.core
  (:require [effective.predicate :as predicate]))

(defmacro effect
  "Asserts modifications specified by `config` using the `clojure.test` API.

   `form` is a Clojure form representing the effect to test.

   `config` is expected to be a collection of monitor configurations. Each of
   those to be a hashmap with the following keys:

   | key        | required? | description                                      |
   | ---------- | --------- | ------------------------------------------------ |
   | `:changes` | **yes**   | expression to evaluate                           |
   | `:from`    | **no**    | expected value of `:changes` before the effect   |
   | `:to`      | **no**    | expected value of `:changes` after the effect    |
   | `:by`      | **no**    | expected numerical difference of `:changes`      |
   |            |           | and after the effect                             |
  
   **Examples**:
   ``` clojure
   (let [x (atom 10)]
     (effect (swap! x inc)
             [{:changes @x
               :from 0
               :to 1}]))
   ```
   produces the following assertions:

   ``` clojure
   (is (= 0 10 \":from check failed\"))
   (is (= 1 11 \":to check failed\"))
   ```
   ``` clojure
   (let [x (atom {:a 100 :b -2})]
     (effect (swap! assoc :a 0 :b 10)
             [{:changes (:a @x) :by -100}
              {:changes (:b @x) :to    2}]))
   ```
   produces the following assertions:

   ``` clojure
   (is (= -100 -100 \":by check failed\"))
   (is (=  2     -2 \":to check failed\"))
   ```"
  [form config]
  `(->> (predicate/collect ~form ~config)
        (run! predicate/assert!)))

(comment
  (let [x (atom {:z -1})]
    (effect (swap! x update :z inc)
            [{:changes (:z @x)
              :from 0}])))
