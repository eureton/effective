(ns effective.predicate
  (:require [effective.checkpoint :as checkpoint]))

(defmulti make
  (fn [option _ _] option))

(def ^:private predicate?
  (every-pred symbol?
              (comp fn? var-get resolve)))

(defmethod make :from
  [_ from index]
  (let [before (checkpoint/before index)]
    (if (predicate? from)
      `(~from ~before)
      `(= ~from ~before))))

(defmethod make :to
  [_ to index]
  (let [after (checkpoint/after index)]
    (if (predicate? to)
      `(~to ~after)
      `(= ~to ~after))))
