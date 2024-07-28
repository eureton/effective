(ns effective.config
  (:require [clojure.set :as cljset]))

(def ^:const OPERATIONS #{:to-change :to-not-change :to-conjoin})

(defn operation [entry]
  (->> entry (keys) (set) (cljset/intersection OPERATIONS) (first)))

(defn observables
  "Sequence of declared observables."
  [config]
  (map (apply some-fn OPERATIONS) config))
