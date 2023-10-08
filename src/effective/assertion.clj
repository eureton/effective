(ns effective.assertion
  (:require [clojure.test :refer [is]]
            [effective.checkpoint :as checkpoint]))

(defn make
  [config index]
  (let [{:keys [from from-lt from-lte from-gt from-gte from-not
                from-less-than from-less-than-or-equal
                from-greater-than from-greater-than-or-equal
                to to-lt to-lte to-gt to-gte to-not
                to-less-than to-less-than-or-equal
                to-greater-than to-greater-than-or-equal
                by by-lt by-lte by-gt by-gte by-not
                by-less-than by-less-than-or-equal
                by-greater-than by-greater-than-or-equal]} config
        from-lt (or from-lt from-less-than)
        from-lte (or from-lte from-less-than-or-equal)
        from-gt (or from-gt from-greater-than)
        from-gte (or from-gte from-greater-than-or-equal)
        to-lt (or to-lt to-less-than)
        to-lte (or to-lte to-less-than-or-equal)
        to-gt (or to-gt to-greater-than)
        to-gte (or to-gte to-greater-than-or-equal)
        by-lt (or by-lt by-less-than)
        by-lte (or by-lte by-less-than-or-equal)
        by-gt (or by-gt by-greater-than)
        by-gte (or by-gte by-greater-than-or-equal)
        before (checkpoint/before index)
        after (checkpoint/after index)]
    (cond-> []
      from     (conj `(is (=    ~from     ~before           )     ":from check failed"))
      from-lt  (conj `(is (>    ~from-lt  ~before           )  ":from-lt check failed"))
      from-lte (conj `(is (>=   ~from-lte ~before           ) ":from-lte check failed"))
      from-gt  (conj `(is (<    ~from-gt  ~before           )  ":from-gt check failed"))
      from-gte (conj `(is (<=   ~from-gte ~before           ) ":from-gte check failed"))
      from-not (conj `(is (not= ~from-not ~before           ) ":from-not check failed"))
      to       (conj `(is (=    ~to       ~after            )       ":to check failed"))
      to-lt    (conj `(is (>    ~to-lt    ~after            )    ":to-lt check failed"))
      to-lte   (conj `(is (>=   ~to-lte   ~after            )   ":to-lte check failed"))
      to-gt    (conj `(is (<    ~to-gt    ~after            )    ":to-gt check failed"))
      to-gte   (conj `(is (<=   ~to-gte   ~after            )   ":to-gte check failed"))
      to-not   (conj `(is (not= ~to-not   ~after            )   ":to-not check failed"))
      by       (conj `(is (=    ~by       (- ~after ~before))       ":by check failed"))
      by-lt    (conj `(is (>    ~by-lt    (- ~after ~before))    ":by-lt check failed"))
      by-lte   (conj `(is (>=   ~by-lte   (- ~after ~before))   ":by-lte check failed"))
      by-gt    (conj `(is (<    ~by-gt    (- ~after ~before))    ":by-gt check failed"))
      by-gte   (conj `(is (<=   ~by-gte   (- ~after ~before))   ":by-gte check failed"))
      by-not   (conj `(is (not= ~by-not   (- ~after ~before))   ":by-not check failed")))))
