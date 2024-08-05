(ns effective.config.schema
  "Malli schemas."
  (:require [clojure.set :as cljset]
            [clojure.string :as string]
            [effective.config.constants :as const]))

(def ^:private ^:const OPT {:optional true})

(def observable
  "Valid if input represents an expression."
  [:or symbol? list? seq?])

(def value-range
  "Describes ranges used for e.g. `:from-within`."
  [:tuple number? [:= :of] number?])

(def assertion
  "Describes parts common to all entries."
  [:fn
   {:error/message "must have exactly one observable"}
   (fn [m]
     (->> m
          (keys)
          (set)
          (cljset/intersection const/OPERATIONS)
          (count)
          (= 1)))])

(defn- minimum-one-constraint
  "Asserts that the incoming value contains at least one of `options`.
   Reports errors under `error-path`."
  [options error-path]
  [:fn
   {:error/message (->> options
                        (string/join ", ")
                        (str "one of the following must be present: "))
    :error/path error-path}
   (fn [m]
     ((apply some-fn options) m))])

(def to-change
  "Describes `:to-change` entries."
  [:and
   [:map
    [:to-change observable]
    [:from OPT :any]
    [:from-fn OPT :any]
    [:from-lt OPT number?]
    [:from-lte OPT number?]
    [:from-gt OPT number?]
    [:from-gte OPT number?]
    [:from-within OPT value-range]
    [:from-not OPT :any]
    [:to OPT :any]
    [:to-fn OPT :any]
    [:to-lt OPT number?]
    [:to-lte OPT number?]
    [:to-gt OPT number?]
    [:to-gte OPT number?]
    [:to-within OPT value-range]
    [:to-not OPT :any]
    [:by OPT number?]
    [:by-fn OPT :any]
    [:by-lt OPT number?]
    [:by-lte OPT number?]
    [:by-gt OPT number?]
    [:by-gte OPT number?]
    [:by-within OPT value-range]
    [:by-not OPT :any]]
   (minimum-one-constraint const/TO_CHANGE_FLAGS [:to-change])
   assertion])

(def to-not-change
  "Describes `:to-not-change` entries."
  [:and
   [:map [:to-not-change observable]]
   assertion])

(def ^:private non-empty-vector
  [:and vector? [:fn seq]])

(def to-conjoin
  "Describes `:to-conjoin` entries."
  [:and
   [:map
    [:to-conjoin observable]
    [:with OPT non-empty-vector]
    [:with-fn OPT non-empty-vector]]
   (minimum-one-constraint const/TO_CONJOIN_FLAGS [:to-conjoin])
   assertion])

(def to-pop
  "Describes `:to-pop` entries."
  [:and
   [:map
    [:to-pop observable]
    [:times [integer? {:default 1}]]]
   assertion])

(def root
  "Top-level schema intended for use against the config passed to `expect`."
  [:+ [:cat [:alt to-change to-not-change to-conjoin to-pop]]])
