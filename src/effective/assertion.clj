(ns effective.assertion
  (:require [clojure.test :refer [is]]
            [effective.checkpoint :as checkpoint]))

(defn make
  [config index]
  (let [{:keys [from from-lt from-less-than from-lte from-less-than-or-equal
                from-gt from-greater-than to to-lt to-gt to-less-than to-greater-than
                to-lte to-less-than-or-equal to-gte to-greater-than-or-equal
                by]} config
        from-lt (or from-lt from-less-than)
        from-lte (or from-lte from-less-than-or-equal)
        from-gt (or from-gt from-greater-than)
        to-lt (or to-lt to-less-than)
        to-lte (or to-lte to-less-than-or-equal)
        to-gt (or to-gt to-greater-than)
        to-gte (or to-gte to-greater-than-or-equal)
        before (checkpoint/before index)
        after (checkpoint/after index)]
    (cond-> []
      from     (conj `(is (=  ~from     ~before           )     ":from check failed"))
      from-lt  (conj `(is (>  ~from-lt  ~before           )  ":from-lt check failed"))
      from-lte (conj `(is (>= ~from-lte ~before           ) ":from-lte check failed"))
      from-gt  (conj `(is (<  ~from-gt  ~before           )  ":from-gt check failed"))
      to       (conj `(is (=  ~to       ~after            )       ":to check failed"))
      to-lt    (conj `(is (>  ~to-lt    ~after            )    ":to-lt check failed"))
      to-lte   (conj `(is (>= ~to-lte   ~after            )   ":to-lte check failed"))
      to-gt    (conj `(is (<  ~to-gt    ~after            )    ":to-gt check failed"))
      to-gte   (conj `(is (<= ~to-gte   ~after            )   ":to-gte check failed"))
      by       (conj `(is (=  ~by       (- ~after ~before))       ":by check failed")))))
