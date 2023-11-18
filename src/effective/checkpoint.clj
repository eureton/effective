(ns effective.checkpoint
  "Symbolic names for referencing the state of the monitored expressions.")

(defn- prefixed-symbol [prefix index]
  (symbol (str prefix "-" index)))

(def before
  "Symbol generator for the before-state of the monitored expression."
  (partial prefixed-symbol "before"))

(def after
  "Symbol generator for the after-state of the monitored expression."
  (partial prefixed-symbol "after"))
