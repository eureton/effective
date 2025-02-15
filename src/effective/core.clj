(ns effective.core
  (:require [flatland.useful.fn :as ufn]
            [effective.assertion :as assertion]
            [effective.assertion.composer :as composer]
            [effective.checkpoint :as checkpoint]
            [effective.config :as config]))

(defmacro expect
  "Asserts modifications specified by `config` using the `clojure.test` API.

   `effect` is a Clojure form representing the effect to test.

   `config` is a collection of declarations, each of which consists of:
     * an **observable**
     * one or more **assertions** on the observable

   The observable is an expression and is declared by one of the following keys:

   | key            | use when the value of the observable is expected |
   | -------------- | ------------------------------------------------ |
   | :to-change     | to change with the effect                        |
   | :to-not-change | to remain unchanged by the effect                |
   | :to-conjoin    | to be a collection which the effect conjoins to  |
   | :to-pop        | to be a collection which the effect pops         |

   Assertions are bound to **checkpoints**. Each observable has two checkpoints:
     * **before** the effect (**b-val**)
     * **after** the effect (**a-val**)

   Assertions are declared by the following keys:

   | key          | description                                               |
   | ------------ | --------------------------------------------------------- |
   | :from        | b-val                                                     |
   | :from-fn     | predicate to assert on b-val                              |
   | :from-lt     | non-inclusive upper bound of b-val                        |
   | :from-lte    | inclusive upper bound of b-val                            |
   | :from-gt     | non-inclusive lower bound of b-val                        |
   | :from-gte    | inclusive lower bound of b-val                            |
   | :from-not    | value which b-val is expected to not be equal to          |
   | :from-within | radius declaration vector (see example below) on b-val    |
   | :to          | a-val                                                     |
   | :to-fn       | predicate to assert on a-val                              |
   | :to-lt       | non-inclusive upper bound of a-val                        |
   | :to-lte      | inclusive upper bound of a-val                            |
   | :to-gt       | non-inclusive lower bound of a-val                        |
   | :to-gte      | inclusive lower bound of a-val                            |
   | :to-not      | value which a-val is expected to not be equal to          |
   | :to-within   | radius declaration vector (see example below) on a-val    |
   | :by          | numerical difference between b-val and a-val              |
   | :by-fn       | predicate to assert on numerical difference between       |
   |              | b-val and a-val              |.                           |
   | :times       | number of times to pop b-val collection                   |

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
   ```

   ---

   ``` clojure
   (let [x (atom 0)]
     (expect (swap! x inc)
             [{:to-change @x :from-within [0.6 :of -0.05]}]))
   ```

   The above expands to the following:

   ``` clojure
   (let [x (atom 0)]
     (let [before-0 @x
           _ (swap! x inc)
           after-0 @x]
       (is (>= 0.6 (java.lang.Math/abs (- before-0 -0.05)))
           \":from-within check failed\")))
   ```

   ---

   ``` clojure
   (let [x (atom [-2 -1])]
     (expect (reset! x [-2 -1 0 1 2])
             [{:to-conjoin @x :with [zero? 1 even?]}]))
   ```

   The above expands to the following:

   ``` clojure
   (let [x (atom [1 2])]
     (let [before-0 @x
           _ (reset! x [1 2 3 4 5])
           after-0 @x]
       (is (= after-0 (conj before-0 3 4 5)) \":with check failed\")))
   ```

   ---

   ``` clojure
   (let [x (atom [-2 -1])]
     (expect (reset! x [-2 -1 0 1 2])
             [{:to-conjoin @x :with-fn [zero? odd? even?]}]))
   ```

   The above expands to the following:

   ``` clojure
   (let [x (atom [-2 -1])]
     (let [before-0 @x
           _ (reset! x [-2 -1 0 1 2])
           after-0 @x]
       (is (= before-0 (pop (pop (pop after-0)))) \":with-fn check failed\")
       (is (zero? (peek (pop (pop after-0)))) \":with-fn check failed\")
       (is (odd? (peek (pop after-0))) \":with-fn check failed\")
       (is (even? (peek after-0)) \":with-fn check failed\")))
   ```
  "
  ([effect config]
   `(expect ~effect :all ~config))
  ([effect composition config]
   (if (config/valid? config)
     (let [observables-seq (config/observables config)
           before (interleave (map checkpoint/before (range)) observables-seq)
           after (interleave (map checkpoint/after (range)) observables-seq)
           composer (composer/make composition)
           join-intra (composer/intra composer)
           join-inter (composer/inter composer)
           assertions (->> config
                           (config/groom)
                           (map-indexed (comp join-intra
                                              (ufn/ap assertion/make)
                                              reverse
                                              vector))
                           (join-inter))]
       `(let [~@before
              _# ~effect
              ~@after]
          ~@assertions))
     `(throw (IllegalArgumentException. ~(str (config/errors config)))))))
