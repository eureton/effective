(ns effective.assertion.composer
  "Converts abstract assertions into concrete ones."
  (:require [clojure.test :refer [is]]))

(defprotocol Composer
  "Composes data representations of assertions into `clojure.test` assertions."
  (intra [this] "Builds a function to compose assertions of a single entry.")
  (inter [this] "Builds a function to compose the results of `intra`."))

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

(deftype Test []
  Composer

  (intra [_]
    (fn [assertions]
      (reduce (fn [acc x]
                (let [{:keys [operation flag predicate]} x]
                  (conj acc
                        `(not ~predicate)
                        `(update-in [~operation ~flag] (fnil inc 0)))))
              []
              assertions)))

  (inter [_]
    (fn [assertions]
      [`(cond-> {}
          ~@(reduce concat assertions))])))

(defn make
  "Creates instance of `Composer` which matches `policy`."
  [policy]
  (case (keyword policy)
    :all (->All)
    :any (->Any)
    :test (->Test)))
