(ns effective.assertion
  (:require [clojure.test :refer [is]]
            [effective.checkpoint :as checkpoint]))

(defn make
  [config index]
  (let [{:keys [from to by]} config
        before (checkpoint/before index)
        after (checkpoint/after index)]
    (cond-> []
      from (conj `(is (= ~from ~before           ) ":from check failed"))
      to   (conj `(is (= ~to   ~after            )   ":to check failed"))
      by   (conj `(is (= ~by   (- ~after ~before))   ":by check failed")))))
