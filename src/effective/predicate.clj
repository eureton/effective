(ns effective.predicate
  (:require [effective.checkpoint :as checkpoint]
            [effective.config :as config]))

(defn- solitary?
  "True if `k` is the only key in `entry`, false otherwise."
  [entry k]
  (-> entry (dissoc k) (empty?)))

(defmulti make
  "Quoted expressions representing the check specified by `flag`."
  (fn [entry flag _ _]
    [(config/operation entry) flag]))

(defmethod make :default
  [_ _ _ _]
  [])

(defmethod make [:to-change :from]
  [_ _ from index]
  [`(= ~from ~(checkpoint/before index))])

(defmethod make [:to-change :from-fn]
  [_ _ from-fn index]
  [`(~from-fn ~(checkpoint/before index))])

(defmethod make [:to-change :from-lt]
  [_ _ from-lt index]
  [`(> ~from-lt ~(checkpoint/before index))])

(defmethod make [:to-change :from-lte]
  [_ _ from-lte index]
  [`(>= ~from-lte ~(checkpoint/before index))])

(defmethod make [:to-change :from-gt]
  [_ _ from-gt index]
  [`(< ~from-gt ~(checkpoint/before index))])

(defmethod make [:to-change :from-gte]
  [_ _ from-gte index]
  [`(<= ~from-gte ~(checkpoint/before index))])

(defmethod make [:to-change :from-not]
  [_ _ from-not index]
  [`(not= ~from-not ~(checkpoint/before index))])

(defmethod make [:to-change :from-within]
  [_ _ from-within index]
  (let [[from-radius _ from-origin] from-within]
    [`(>= ~from-radius
          (Math/abs (- ~(checkpoint/before index) ~from-origin)))]))

(defmethod make [:to-change :to]
  [_ _ to index]
  [`(= ~to ~(checkpoint/after index))])

(defmethod make [:to-change :to-fn]
  [_ _ to-fn index]
  [`(~to-fn ~(checkpoint/after index))])

(defmethod make [:to-change :to-lt]
  [_ _ to-lt index]
  [`(> ~to-lt ~(checkpoint/after index))])

(defmethod make [:to-change :to-lte]
  [_ _ to-lte index]
  [`(>= ~to-lte ~(checkpoint/after index))])

(defmethod make [:to-change :to-gt]
  [_ _ to-gt index]
  [`(< ~to-gt ~(checkpoint/after index))])

(defmethod make [:to-change :to-gte]
  [_ _ to-gte index]
  [`(<= ~to-gte ~(checkpoint/after index))])

(defmethod make [:to-change :to-not]
  [_ _ to-not index]
  [`(not= ~to-not ~(checkpoint/after index))])

(defmethod make [:to-change :to-within]
  [_ _ to-within index]
  (let [[to-radius _ to-origin] to-within]
    [`(>= ~to-radius (Math/abs (- ~(checkpoint/after index) ~to-origin)))]))

(defmethod make [:to-change :by]
  [_ _ by index]
  [`(= ~by (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make [:to-change :by-fn]
  [_ _ by-fn index]
  [`(~by-fn (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make [:to-change :by-lt]
  [_ _ by-lt index]
  [`(> ~by-lt (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make [:to-change :by-lte]
  [_ _ by-lte index]
  [`(>= ~by-lte (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make [:to-change :by-gt]
  [_ _ by-gt index]
  [`(< ~by-gt (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make [:to-change :by-gte]
  [_ _ by-gte index]
  [`(<= ~by-gte (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make [:to-change :by-not]
  [_ _ by-not index]
  [`(not= ~by-not (- ~(checkpoint/after index) ~(checkpoint/before index)))])

(defmethod make [:to-change :by-within]
  [_ _ by-within index]
  (let [[by-radius _ by-origin] by-within]
    [`(>= ~by-radius
          (-> (- ~(checkpoint/after index) ~(checkpoint/before index))
              (- ~by-origin)
              (Math/abs)))]))

(defmethod make [:to-change :to-change]
  [entry k _ index]
  (cond-> []
    (solitary? entry k) (conj `(not= ~(checkpoint/after index)
                                     ~(checkpoint/before index)))))

(defmethod make [:to-not-change :to-not-change]
  [entry k _ index]
  (cond-> []
   (solitary? entry k) (conj `(= ~(checkpoint/after index)
                                 ~(checkpoint/before index)))))

(defmethod make [:to-not-change :and-be]
  [_ _ and-be index]
  [`(= ~and-be
       ~(checkpoint/after index)
       ~(checkpoint/before index))])

(defn- pop-times
  "Builds a form which `pop`s `coll` `n` times."
  [coll n]
  (loop [form coll
         i n]
    (if (zero? i)
      form
      (recur `(pop ~form)
             (dec i)))))

(defmethod make [:to-conjoin :with]
  [_ _ with index]
  [`(= ~(checkpoint/after index) (conj ~(checkpoint/before index) ~@with))])

(defmethod make [:to-conjoin :with-fn]
  [_ _ with-fn index]
  (let [before (checkpoint/before index)
        after (checkpoint/after index)
        tally (count with-fn)
        pick (fn [i]
               `(peek ~(pop-times after (- tally i 1))))]
    (cons `(= ~before ~(pop-times after tally))
          (->> with-fn
               (map-indexed vector)
               (map (fn [[i x]]
                      `(~x ~(pick i))))))))

(defmethod make [:to-pop :times]
  [_ _ times index]
  [`(= ~(checkpoint/after index)
       ~(pop-times (checkpoint/before index) times))])
