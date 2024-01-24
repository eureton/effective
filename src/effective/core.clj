(ns effective.core
  (:require [effective.assertion :as assertion]
            [effective.assertion.composer :as composer]
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

   The above expands to the following:

   ``` clojure
   (let [x (atom 10)]
     (let [before-0 @x
           _ (swap! x inc)
           after-0 @x]
       (is (= 0 before-0) \":from check failed\")
       (is (= 1 after-0) \":to check failed\")))
   ```

   ---

   ``` clojure
   (let [x (atom [:a :b :c])]
     (expect (swap! x pop)
             [{:to-change (count @x) :by -1}]))
   ```

   The above expands to the following:

   ``` clojure
   (let [x (atom [:a :b :c])]
     (let [before-0 (count @x)
           _ (swap! x pop)
           after-0 (count @x)]
       (is (= -1 (- after-0 before-0)) \":by check failed\")))
   ```

   ---

   ``` clojure
   (let [x (atom \"ABC\")]
     (expect (swap! x clojure.string/upper-case)
             :any
             [{:to-change (count @x) :from 3 :to 4}
              {:to-not-change @x}]))
   ```

   The above expands to the following:

   ``` clojure
   (let [x (atom \"ABC\")]
     (let [before-0 (count @x)
           before-1 @x
           _ (swap! x clojure.string/upper-case)
           after-0 (count @x)
           after-1 @x]
       (is (or (and (= 3 before-0)
                    (= 4 after-0))
               (= after-1 before-1)))))
   ```"
  ([effect config]
   `(expect ~effect :all ~config))
  ([effect composition config]
   (if (validation/config-valid? config)
     (let [observables-seq (map #(or (:to-change %) (:to-not-change %)) config)
           before (interleave (map checkpoint/before (range)) observables-seq)
           after (interleave (map checkpoint/after (range)) observables-seq)
           composer (composer/make composition)
           join-intra (composer/intra composer)
           join-inter (composer/inter composer)
           assertions (->> (map assertion/make config (range))
                           (map join-intra)
                           (join-inter))]
       `(let [~@before
              _# ~effect
              ~@after]
          ~@assertions))
     `(throw (IllegalArgumentException. "Invalid configuration")))))
