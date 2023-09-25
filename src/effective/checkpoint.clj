(ns effective.checkpoint)

(defn prefixed-symbol [prefix index]
  (symbol (str prefix "-" index)))

(def before
  (partial prefixed-symbol "before"))

(def after
  (partial prefixed-symbol "after"))
