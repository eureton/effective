(ns effective.predicate
  (:require [effective.checkpoint :as checkpoint]))

(defmulti make
  (fn [option _ _] option))

(defmethod make :from
  [_ from index]
  (let [before (checkpoint/before index)]
    (if (and (symbol? from)
             (-> from resolve var-get fn?))
      `(~from ~before)
      `(= ~from ~before))))
