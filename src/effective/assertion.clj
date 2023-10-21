(ns effective.assertion
  (:require [clojure.test :refer [is]]
            [effective.checkpoint :as checkpoint]))

(defn- message
  "Error message for the assertion generated for `option`."
  [option]
  (str option " check failed"))

(defn make
  [config index]
  (let [{:keys [from from-lt from-lte from-gt from-gte from-not
                from-less-than from-less-than-or-equal
                from-greater-than from-greater-than-or-equal
                to to-lt to-lte to-gt to-gte to-not to-within
                to-less-than to-less-than-or-equal
                to-greater-than to-greater-than-or-equal
                by by-lt by-lte by-gt by-gte by-not
                by-less-than by-less-than-or-equal
                by-greater-than by-greater-than-or-equal]} config
        [to-radius _ to-origin] to-within
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
      from      (conj `(is (=    ~from      ~before                         )      ~(message :from)))
      from-lt   (conj `(is (>    ~from-lt   ~before                         )   ~(message :from-lt)))
      from-lte  (conj `(is (>=   ~from-lte  ~before                         )  ~(message :from-lte)))
      from-gt   (conj `(is (<    ~from-gt   ~before                         )   ~(message :from-gt)))
      from-gte  (conj `(is (<=   ~from-gte  ~before                         )  ~(message :from-gte)))
      from-not  (conj `(is (not= ~from-not  ~before                         )  ~(message :from-not)))
      to        (conj `(is (=    ~to        ~after                          )        ~(message :to)))
      to-lt     (conj `(is (>    ~to-lt     ~after                          )     ~(message :to-lt)))
      to-lte    (conj `(is (>=   ~to-lte    ~after                          )    ~(message :to-lte)))
      to-gt     (conj `(is (<    ~to-gt     ~after                          )     ~(message :to-gt)))
      to-gte    (conj `(is (<=   ~to-gte    ~after                          )    ~(message :to-gte)))
      to-not    (conj `(is (not= ~to-not    ~after                          )    ~(message :to-not)))
      to-within (conj `(is (>=   ~to-radius (Math/abs (- ~after ~to-origin))) ~(message :to-within)))
      by        (conj `(is (=    ~by        (- ~after ~before)              )        ~(message :by)))
      by-lt     (conj `(is (>    ~by-lt     (- ~after ~before)              )     ~(message :by-lt)))
      by-lte    (conj `(is (>=   ~by-lte    (- ~after ~before)              )    ~(message :by-lte)))
      by-gt     (conj `(is (<    ~by-gt     (- ~after ~before)              )     ~(message :by-gt)))
      by-gte    (conj `(is (<=   ~by-gte    (- ~after ~before)              )    ~(message :by-gte)))
      by-not    (conj `(is (not= ~by-not    (- ~after ~before)              )    ~(message :by-not))))))
