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
      (->> assertions
           (mapcat (fn [x] (for [predicate (:predicates x)]
                             {:predicate predicate
                              :message (:message x)})))
           (map (fn [x] `(is ~(:predicate x) ~(:message x)))))))

  (inter [_]
    (fn [assertions] (mapcat identity assertions))))

(deftype Any []
  Composer

  (intra [_]
    (fn [assertions]
      `(and ~@(mapcat :predicates assertions))))

  (inter [_]
    (fn [assertions] `[(is (or ~@assertions))])))

(defn make
  "Creates instance of `Composer` which matches the input `policy`."
  [policy]
  (case (keyword policy)
    :all (->All)
    :any (->Any)))
