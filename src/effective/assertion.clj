(ns effective.assertion
  (:require [clojure.set :as cljset]
            [clojure.test :refer [is]]
            [effective.predicate :as predicate]))

(defn- message
  "Error message for the assertion generated for `option`."
  [option]
  (str option " check failed"))

(def ^:private ^:const CONFIG_KEY_ABBREVIATION_MAP
  {:from-less-than             :from-lt
   :from-less-than-or-equal    :from-lte
   :from-greater-than          :from-gt
   :from-greater-than-or-equal :from-gte
   :to-less-than               :to-lt
   :to-less-than-or-equal      :to-lte
   :to-greater-than            :to-gt
   :to-greater-than-or-equal   :to-gte
   :by-less-than               :by-lt
   :by-less-than-or-equal      :by-lte
   :by-greater-than            :by-gt
   :by-greater-than-or-equal   :by-gte})

(def ^:private ^:const CONFIG_KEYS
  [:from :from-lt :from-lte :from-gt :from-gte :from-not :from-within
   :from-less-than :from-less-than-or-equal
   :from-greater-than :from-greater-than-or-equal
   :to :to-lt :to-lte :to-gt :to-gte :to-not :to-within
   :to-less-than :to-less-than-or-equal
   :to-greater-than :to-greater-than-or-equal
   :by :by-lt :by-lte :by-gt :by-gte :by-not :by-within
   :by-less-than :by-less-than-or-equal
   :by-greater-than :by-greater-than-or-equal
   :to-not-change])

(defn- normalize [config]
  (merge (cljset/rename-keys config CONFIG_KEY_ABBREVIATION_MAP)
         (select-keys config (vals CONFIG_KEY_ABBREVIATION_MAP))))

(defn make
  [config index]
  (->> (select-keys (normalize config) CONFIG_KEYS)
       (map (fn [[k v]] `(is ~(predicate/make k v index) ~(message k))))
       (reduce conj [])))
