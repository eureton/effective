(ns effective.config
  (:require [clojure.set :as cljset]
            [malli.core :as m]
            [malli.error :as me]
            [malli.transform :as mt]
            [effective.config.constants :as const]
            [effective.config.schema :as schema]))

(defn operation
  "Operation declared on `entry`."
  [entry]
  (->> entry (keys) (set) (cljset/intersection const/OPERATIONS) (first)))

(defn observables
  "Sequence of declared observables."
  [config]
  (map (apply some-fn const/OPERATIONS) config))

(def groom
  "Normalizes, initializes and sanitizes."
  (m/decoder
    schema/root
    (mt/transformer
      (mt/key-transformer {:decode #(get const/ABBREVIATION_MAP % %)})
      mt/default-value-transformer
      mt/strip-extra-keys-transformer)))

(def validate
  (m/validator schema/root))

(def valid?
  (comp validate groom))

(def errors
  (comp me/humanize
        (m/explainer schema/root)
        groom))
