(ns effective.predicate
  (:require [effective.checkpoint :as checkpoint]))

(defmulti make
  "Quoted expressions representing the check specified by `flag`."
  (fn [flag _ _] flag))

(def ^:private function?
  "True if value is a symbol which represents a function, false otherwise."
  (every-pred symbol?
              (comp fn? var-get resolve)))

(defmethod make :from
  [_ from index]
  (let [before (checkpoint/before index)]
    [(if (function? from)
       `(~from ~before)
       `(= ~from ~before))]))

(defmethod make :from-lt
  [_ from-lt index]
  [`(> ~from-lt ~(checkpoint/before index))])

(defmethod make :from-lte
  [_ from-lte index]
  [`(>= ~from-lte ~(checkpoint/before index))])

(defmethod make :from-gt
  [_ from-gt index]
  [`(< ~from-gt ~(checkpoint/before index))])

(defmethod make :from-gte
  [_ from-gte index]
  [`(<= ~from-gte ~(checkpoint/before index))])

(defmethod make :from-not
  [_ from-not index]
  [`(not= ~from-not ~(checkpoint/before index))])

(defmethod make :from-within
  [_ from-within index]
  (let [[from-radius _ from-origin] from-within]
    [`(>= ~from-radius
          (Math/abs (- ~(checkpoint/before index) ~from-origin)))]))

(defmethod make :to
  [_ to index]
  (let [after (checkpoint/after index)]
    [(if (function? to)
       `(~to ~after)
       `(= ~to ~after))]))

(defmethod make :to-lt
  [_ to-lt index]
  [`(> ~to-lt ~(checkpoint/after index))])

(defmethod make :to-lte
  [_ to-lte index]
  [`(>= ~to-lte ~(checkpoint/after index))])

(defmethod make :to-gt
  [_ to-gt index]
  [`(< ~to-gt ~(checkpoint/after index))])

(defmethod make :to-gte
  [_ to-gte index]
  [`(<= ~to-gte ~(checkpoint/after index))])

(defmethod make :to-not
  [_ to-not index]
  [`(not= ~to-not ~(checkpoint/after index))])

(defmethod make :to-within
  [_ to-within index]
  (let [[to-radius _ to-origin] to-within]
    [`(>= ~to-radius (Math/abs (- ~(checkpoint/after index) ~to-origin)))]))

(defmethod make :by
  [_ by index]
  (let [before (checkpoint/before index)
        after (checkpoint/after index)]
    [(if (function? by)
       `(~by (- ~after ~before))
       `(= ~by (- ~after ~before)))]))

(defmethod make :by-lt
  [_ by-lt index]
  [`(> ~by-lt (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make :by-lte
  [_ by-lte index]
  [`(>= ~by-lte (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make :by-gt
  [_ by-gt index]
  [`(< ~by-gt (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make :by-gte
  [_ by-gte index]
  [`(<= ~by-gte (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make :by-not
  [_ by-not index]
  [`(not= ~by-not (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make :by-within
  [_ by-within index]
  (let [[by-radius _ by-origin] by-within]
    [`(>= ~by-radius
          (-> (- ~(checkpoint/after index) ~(checkpoint/before index))
              (- ~by-origin)
              (Math/abs)))]))

(defmethod make :to-not-change
  [_ _ index]
  [`(= ~(checkpoint/after index) ~(checkpoint/before index))])

(defmethod make :with
  [_ with index]
  [`(= ~(checkpoint/after index) (conj ~(checkpoint/before index) ~with))])

(defmethod make :with-hash-containing
  [_ value index]
  [`(= ~(checkpoint/before index) (pop ~(checkpoint/after index)))
   `(= ~value (-> ~(checkpoint/after index)
                  (peek)
                  (select-keys (keys ~value))))])
