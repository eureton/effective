(ns effective.assertion
  "Data representations of assertions."
  (:require [effective.config :as config]
            [effective.predicate :as predicate]))

(defn- message
  "Error message for the assertion generated for `option`."
  [option]
  (str option " check failed"))

(defn- inflate
  "Builds a function which turns a flag-value pair into a collection
   of data structures."
  [index entry]
  (fn [[k v]]
    (map
      (fn [x]
        {:flag k
         :value v
         :operation (config/operation entry)
         :predicate x
         :message (message k)})
      (predicate/make entry k v index))))

(defn make
  "Sequence of assertions which correspond to `entry`.
   Generates checkpoint references for position `index`."
  [entry index]
  (mapcat
    (inflate index entry)
    entry))
