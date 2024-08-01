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
  [index operation]
  (fn [[k v]]
    (map
      (fn [x]
        {:flag k
         :value v
         :operation operation
         :predicate x
         :message (message k)})
      (predicate/make operation k v index))))

(defn make
  "Sequence of assertions which correspond to `entry`.
   Generates checkpoint references for position `index`."
  [entry index]
  (mapcat
    (inflate index (config/operation entry))
    entry))
