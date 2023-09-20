(ns effective.predicate
  "Predicates represent the information which is required to produce a
   `clojure.test` assertion."
  (:require [clojure.test :refer [is]]
            [effective.monitor :as monitor]))

(defn assert!
  "Turns the predicate into a `clojure.test` assertion."
  [predicate]
  (let [[expected actual message] predicate]
    (is (= expected actual) message)))

(defmacro collect
  "Processes the multi-monitor configuration (see `effective.core/effect`)
   and the effect form. Produces a vector of predicates."
  [form config]
  `(let [make-watcher# #(fn []
                          (get-in ~config [% :changes]))
         unimonitor# (->> ~config
                          (map-indexed #(-> %2
                                            (assoc :watch-fn (make-watcher# %1))
                                            (dissoc :changes)
                                            (monitor/make)))
                          (reduce monitor/compose))
         seed# (fn [] ~form [])]
     (unimonitor# seed#)))
