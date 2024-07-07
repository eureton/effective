(ns effective.config)

(defn observables
  "Sequence of declared observables."
  [config]
  (map (some-fn :to-change :to-not-change :to-conjoin) config))
