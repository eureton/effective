(ns effective.assertion
  "Generates data representations of assertions."
  (:require [clojure.set :as cljset]
            [effective.config :as config]
            [effective.predicate :as predicate]))

(defn- message
  "Error message for the assertion generated for `option`."
  [option]
  (str option " check failed"))

(def ^:private ^:const CONFIG_KEY_ABBREVIATION_MAP
  "Associates config keys which resolve to the same predicate.
   Left-hand-side keys are lower precedence than right-hand-side keys.

   Helps avoid inconsistency in case of bad input, such as:

   ``` clojure
   {:from-lt 10 :from-less-than 11}
   ```"
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
  "Authoritative source of valid config keys.
   Ordered, so as to generate assertions predictably."
  [:from :from-lt :from-lte :from-gt :from-gte :from-not :from-within
   :from-less-than :from-less-than-or-equal
   :from-greater-than :from-greater-than-or-equal
   :to :to-lt :to-lte :to-gt :to-gte :to-not :to-within
   :to-less-than :to-less-than-or-equal
   :to-greater-than :to-greater-than-or-equal
   :by :by-lt :by-lte :by-gt :by-gte :by-not :by-within
   :by-less-than :by-less-than-or-equal
   :by-greater-than :by-greater-than-or-equal
   :to-not-change :with :times])

(defn- normalize
  "Transforms user input to a consistent, unambiguous format."
  [config]
  (merge (cljset/rename-keys config CONFIG_KEY_ABBREVIATION_MAP)
         (select-keys config (vals CONFIG_KEY_ABBREVIATION_MAP))))

(defn- sanitize
  "Culls unknown keys in user input."
  [config]
  (select-keys config CONFIG_KEYS))

(defn- initialize
  "Applies default values, where necessary."
  [config]
  (cond->> config
    (:to-pop config) (merge {:times 1})))

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
  (->> config
       (normalize)
       (sanitize)
       (initialize)
       (mapcat (inflate index (config/operation config)))))
