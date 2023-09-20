(ns effective.monitor
  "A monitor is a function which takes one argument (another monitor)
   and returns a vector of predicates (see `effective.predicate`).
  
   Predicates are generated on the basis of whether the specified modifications
   happened with respect to the effect and the expression.")

(def ^:dynamic *debug*
  "Prints useful information to the console when `true`."
  false)

(defn- dump [before after]
  (let [max-length (->> [before after]
                        (map (comp count str))
                        (apply max))
        spec (str "[%6s] value: %" max-length "s, type: %s")]
    (prn (format spec "before" before (type before)))
    (prn (format spec "after" after (type after)))))

(defn make
  "Builds a monitor out of a monitor configuration.
   Multiple monitors need to be composed (see `effective.monitor/compose`)."
  [config]
  (fn [monitor-fn]
    (let [{:keys [watch-fn from to by]} config
          before (watch-fn)
          predicates (monitor-fn)
          after (watch-fn)]
      (when *debug*
        (dump before after))
      (cond-> predicates
        from (conj [from before           ":from check failed"])
        to   (conj [to   after              ":to check failed"])
        by   (conj [by   (- after before)   ":by check failed"])))))

(defn compose
  "Composes monitors `x` and `y` so that:
    * `y` runs before `x`
    * both call their watcher function before and after the effect thunk."
  [x y]
  (fn [z]
    (x (fn []
         (y z)))))
