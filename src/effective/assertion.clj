(ns effective.assertion
  (:require [clojure.test :refer [is]]
            [effective.predicate :as predicate]))

(defn- message
  "Error message for the assertion generated for `option`."
  [option]
  (str option " check failed"))

(defn make
  [config index]
  (let [{:keys [from from-lt from-lte from-gt from-gte from-not from-within
                from-less-than from-less-than-or-equal
                from-greater-than from-greater-than-or-equal
                to to-lt to-lte to-gt to-gte to-not to-within
                to-less-than to-less-than-or-equal
                to-greater-than to-greater-than-or-equal
                by by-lt by-lte by-gt by-gte by-not by-within
                by-less-than by-less-than-or-equal
                by-greater-than by-greater-than-or-equal
                to-not-change]} config
        from-lt (or from-lt from-less-than)
        from-lte (or from-lte from-less-than-or-equal)
        from-gt (or from-gt from-greater-than)
        from-gte (or from-gte from-greater-than-or-equal)
        to-lt (or to-lt to-less-than)
        to-lte (or to-lte to-less-than-or-equal)
        to-gt (or to-gt to-greater-than)
        to-gte (or to-gte to-greater-than-or-equal)
        by-lt (or by-lt by-less-than)
        by-lte (or by-lte by-less-than-or-equal)
        by-gt (or by-gt by-greater-than)
        by-gte (or by-gte by-greater-than-or-equal)]
    (cond-> []
      from          (conj `(is ~(predicate/make :from from index)     ~(message :from)))
      from-lt       (conj `(is ~(predicate/make :from-lt from-lt index) ~(message :from-lt)))
      from-lte      (conj `(is ~(predicate/make :from-lte from-lte index) ~(message :from-lte)))
      from-gt       (conj `(is ~(predicate/make :from-gt  from-gt  index) ~(message :from-gt)))
      from-gte      (conj `(is ~(predicate/make :from-gte from-gte index) ~(message :from-gte)))
      from-not      (conj `(is ~(predicate/make :from-not from-not index) ~(message :from-not)))
      from-within   (conj `(is ~(predicate/make :from-within from-within index) ~(message :from-within)))
      to            (conj `(is ~(predicate/make :to to index)     ~(message :to)))
      to-lt         (conj `(is ~(predicate/make :to-lt to-lt index)     ~(message :to-lt)))
      to-lte        (conj `(is ~(predicate/make :to-lte to-lte index)     ~(message :to-lte)))
      to-gt         (conj `(is ~(predicate/make :to-gt  to-gt  index)     ~(message :to-gt)))
      to-gte        (conj `(is ~(predicate/make :to-gte to-gte index)     ~(message :to-gte)))
      to-not        (conj `(is ~(predicate/make :to-not to-not index)     ~(message :to-not)))
      to-within     (conj `(is ~(predicate/make :to-within to-within index)     ~(message :to-within)))
      by            (conj `(is ~(predicate/make :by by index)     ~(message :by)))
      by-lt         (conj `(is ~(predicate/make :by-lt by-lt index)     ~(message :by-lt)))
      by-lte        (conj `(is ~(predicate/make :by-lte by-lte index)     ~(message :by-lte)))
      by-gt         (conj `(is ~(predicate/make :by-gt by-gt index)     ~(message :by-gt)))
      by-gte        (conj `(is ~(predicate/make :by-gte by-gte index)     ~(message :by-gte)))
      by-not        (conj `(is ~(predicate/make :by-not by-not index)     ~(message :by-not)))
      by-within     (conj `(is ~(predicate/make :by-within by-within index)     ~(message :by-within)))
      to-not-change (conj `(is ~(predicate/make :to-not-change nil index) ~(message :to-not-change))))))
