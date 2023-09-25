(ns effective.assertion
  (:require [clojure.test :refer [is]]))

(defn- prefixed-symbol [prefix index]
  (symbol (str prefix "-" index)))

(defn make
  [config index]
  (let [{:keys [from to by]} config
        before (prefixed-symbol "before" index)
        after (prefixed-symbol "after" index)]
    (cond-> []
      from (conj `(is (= ~from ~before           ) ":from check failed"))
      to   (conj `(is (= ~to   ~after            )   ":to check failed"))
      by   (conj `(is (= ~by   (- ~after ~before))   ":by check failed")))))
