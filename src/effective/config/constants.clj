(ns effective.config.constants)

(def ^:const OPERATIONS
  "Comprehensive set of all operations supported on the observables."
  #{:to-change :to-not-change :to-conjoin :to-pop})

(def ^:const ABBREVIATION_MAP
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

(def ^:const TO_CHANGE_FLAGS
  "Vector of all flags supported by the `:to-change` operation."
  [:from        :to        :by
   :from-lt     :to-lt     :by-lt
   :from-lte    :to-lte    :by-lte
   :from-gt     :to-gt     :by-gt
   :from-gte    :to-gte    :by-gte
   :from-within :to-within :by-within
   :from-not    :to-not    :by-not])
