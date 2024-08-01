(ns effective.assertion
  "Generates data representations of assertions."
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
    (map (fn [x]
           {:flag k
            :value v
            :operation operation
            :predicate x
            :message (message k)})
         (predicate/make operation k v index))))

(defn make
  "Vector of the assertions which correspond to `config`.
   Generates checkpoint references for position `index`."
  [config index]
  (mapcat
    (inflate index (config/operation config))
    config))
