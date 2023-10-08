(ns effective.assertion
  (:require [clojure.test :refer [is]]
            [effective.checkpoint :as checkpoint]))

(defn make
  [config index]
  (let [{:keys [from to to-lt to-less-than
                to-lte to-less-than-or-equal by]} config
        to-lt (or to-lt to-less-than)
        to-lte (or to-lte to-less-than-or-equal)
        before (checkpoint/before index)
        after (checkpoint/after index)]
    (cond-> []
      from   (conj `(is (=  ~from   ~before           )  ":from check failed"))
      to     (conj `(is (=  ~to     ~after            )    ":to check failed"))
      to-lt  (conj `(is (>  ~to-lt  ~after            ) ":to-lt check failed"))
      to-lte (conj `(is (>= ~to-lte ~after            ) ":to-lt check failed"))
      by     (conj `(is (=  ~by     (- ~after ~before))    ":by check failed")))))
