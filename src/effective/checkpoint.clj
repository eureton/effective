(ns effective.checkpoint
  "Symbolic names for referencing the state of the observables.")

(defn- prefixed-symbol [prefix index]
  (symbol (str prefix "-" index)))

(def before
  "Symbol generator for the before-state of the observable."
  (partial prefixed-symbol "before"))

(def after
  "Symbol generator for the after-state of the observable."
  (partial prefixed-symbol "after"))
