(ns effective.assertion.composer
  "Composes data representations of assertions into `clojure.test` assertions."
  (:require [clojure.test :refer [is]]))

(defprotocol Composer
  (intra [this] "Builds a function to compose assertions within a single item.")
  (inter [this] "Builds a function to compose assertions of all items."))

(deftype All []
  Composer

  (intra [_]
    (fn [assertions]
      (map (fn [x] `(is ~(:predicate x) ~(:message x))) assertions)))

  (inter [_]
    (fn [assertions] (mapcat identity assertions))))

(deftype Any []
  Composer

  (intra [_]
    (fn [assertions]
      (if (-> assertions (count) (> 1))
        `(and ~@(map :predicate assertions))
        (-> assertions first :predicate))))

  (inter [_]
    (fn [assertions] `[(is (or ~@assertions))])))

(defn make
  "Creates instance of `Composer` which matches the input `policy`."
  [policy]
  (case (keyword policy)
    :all (->All)
    :any (->Any)))
