(ns effective.core
  (:require [effective.assertion :as assertion]
            [effective.checkpoint :as checkpoint]
            [effective.validation :as validation]))

(defmacro expect
  "Asserts modifications specified by `config` using the `clojure.test` API.

   `effect` is a Clojure form representing the effect to test.

   `config` is expected to be a collection of monitor configurations. Each of
   those to be a hashmap with the following keys:

   | key          | required? | description                    |
   | ------------ | --------- | ------------------------------ |
   | `:to-change` | **yes**   | expression to evaluate         |
   | `:from`      | **no**    | expected value of `:to-change` |
   |              |           | before the effect              |
   | `:to`        | **no**    | expected value of `:to-change` |
   |              |           | after the effect               |
   | `:by`        | **no**    | expected numerical difference  |
   |              |           | of `:to-change` before and     |
   |              |           | after the effect               |
  
   **Examples**:
   ``` clojure
   (let [x (atom 10)]
     (expect (swap! x inc)
             [{:to-change @x
               :from 0
               :to 1}]))
   ```

   expands to the following:
   ``` clojure
   (let [x (atom 10)]
     (let [before-0 @x
           _ (swap! x inc)
           after-0 @x]
       (is (= 0 before-0) \":from check failed\")
       (is (= 1 after-0) \":to check failed\")))
   ```

   Multiple expressions can be monitored for changes:
   ``` clojure
   (let [x (atom {:a 100 :b -2})]
     (expect (swap! assoc :a 0 :b 10)
             [{:to-change (:a @x) :by -100}
              {:to-change (:b @x) :to    2}]))
   ```
   expands to the following:
   ``` clojure
   (let [x (atom {:a 100 :b -2})]
     (let [before-0 (:a @x)
           before-1 (:b @x)
           _ (swap! x assoc :a 0 :b 10)
           after-0 (:a @x)
           after-1 (:b @x)]
       (is (= -100 (- after-0 before-0)) \":by check failed\")
       (is (= 2 after-1) \":to check failed\")))
   ```"
  [effect config]
  (when-not (validation/config-valid? config)
    (throw (IllegalArgumentException.)))
  (let [changes-seq (map :to-change config)
        before-vars (interleave (map checkpoint/before (range)) changes-seq)
        after-vars (interleave (map checkpoint/after (range)) changes-seq)
        assertions (mapcat assertion/make config (range))]
    `(let [~@before-vars
           _# ~effect
           ~@after-vars]
       ~@assertions)))
