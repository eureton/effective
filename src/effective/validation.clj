(ns effective.validation
  (:require [clojure.set :as cljset]))

(def ^:private within-valid?
  (every-pred vector?
              (comp number? #(nth % 0))
              (comp #{:of} #(nth % 1))
              (comp number? #(nth % 2))))

(defn- single-observable? [assertion]
  (->> assertion
       (keys)
       (set)
       (cljset/intersection #{:to-change :to-not-change :to-conjoin})
       (count)
       (= 1)))

(defn- missing?
  "Predicate which returns `true` if the input contains `x`, `false` otherwise."
  [x]
  #(not (contains? % x)))

(def ^:private to-conjoin-valid?
  (every-pred map?
              :to-conjoin
              (some-fn :with :with-hash-containing)
              (some-fn (missing? :with-hash-containing)
                       (comp map? :with-hash-containing))))

(def ^:private assertion-config-valid?
  (every-pred map?
              single-observable?
              (some-fn (missing? :to-conjoin)
                       to-conjoin-valid?)
              (some-fn (missing? :from-within)
                       (comp within-valid? :from-within))
              (some-fn (missing? :to-within)
                       (comp within-valid? :to-within))
              (some-fn (missing? :by-within)
                       (comp within-valid? :by-within))))

(def config-valid?
  (every-pred coll?
              #(every? assertion-config-valid? %)))
