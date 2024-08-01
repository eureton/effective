(ns effective.config
  (:require [clojure.set :as cljset]
            [malli.core :as m]
            [malli.error :as me]
            [malli.transform :as mt]
            [effective.config.constants :as const]
            [effective.config.schema :as schema]))

(defn operation [entry]
  (->> entry (keys) (set) (cljset/intersection const/OPERATIONS) (first)))

(defn observables
  "Sequence of declared observables."
  [config]
  (map (apply some-fn const/OPERATIONS) config))

(def decode
  (m/decoder
    schema/root
    (mt/transformer
      mt/strip-extra-keys-transformer
      mt/default-value-transformer)))

(def valid?
  (comp (m/validator schema/root) decode))

(def errors
  (comp me/humanize
        (m/explainer schema/root)
        decode))
