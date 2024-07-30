(ns effective.config.schema
  (:require [clojure.string :as string]))

(def ^:private ^:const OPT {:optional true})

(def hook
  "True if input represents a function, false otherwise."
  [:or
   [:and symbol? [:fn (comp fn? var-get resolve)]]
   [:and list? [:fn (comp symbol? first)]]])

(def observable
  "Valid if input represents an expression."
  [:or symbol? list?])

(def value-range
  [:tuple number? [:= :of] number?])

(def ^:private ^:const TO_CHANGE_FLAGS
  [:from        :to        :by
   :from-lt     :to-lt     :by-lt
   :from-lte    :to-lte    :by-lte
   :from-gt     :to-gt     :by-gt
   :from-gte    :to-gte    :by-gte
   :from-within :to-within :by-within
   :from-not    :to-not    :by-not])

(def to-change
  "Valid if input represents a `:to-change` operation on an observable."
  [:and
   [:map
    [:to-change observable]
    [:from OPT :any]
    [:from-lt OPT number?]
    [:from-lte OPT number?]
    [:from-gt OPT number?]
    [:from-gte OPT number?]
    [:from-within OPT value-range]
    [:from-not OPT :any]
    [:to OPT :any]
    [:to-lt OPT number?]
    [:to-lte OPT number?]
    [:to-gt OPT number?]
    [:to-gte OPT number?]
    [:to-within OPT value-range]
    [:to-not OPT :any]
    [:by OPT [:or number? hook]]
    [:by-lt OPT number?]
    [:by-lte OPT number?]
    [:by-gt OPT number?]
    [:by-gte OPT number?]
    [:by-within OPT value-range]
    [:by-not OPT :any]]
   [:fn
    {:error/message (->> TO_CHANGE_FLAGS
                         (string/join ", ")
                         (str "one of the following must be present: "))
     :error/path [:to-change]}
    (fn [m]
      ((apply some-fn TO_CHANGE_FLAGS) m))]])

(def to-not-change
  "Valid if input represents a `:to-not-change` operation on an observable."
  [:map [:to-not-change observable]])

(def to-conjoin
  "Valid if input represents a `:to-conjoin` operation on an observable."
  [:map
   [:to-conjoin observable]
   [:with [:and
           vector?
           [:fn seq]]]])
