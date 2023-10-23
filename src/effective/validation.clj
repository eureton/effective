(ns effective.validation)

(def ^:private within-valid?
  (every-pred vector?
              (comp number? #(nth % 0))
              (comp #{:of} #(nth % 1))
              (comp number? #(nth % 2))))

(def ^:private assertion-config-valid?
  (let [change? (comp some? :to-change)
        no-change? (comp some? :to-not-change)]
    (every-pred map?
                (some-fn (every-pred change? (complement no-change?))
                         (every-pred (complement change?) no-change?))
                (some-fn #(not (contains? % :from-within))
                         (comp within-valid? :from-within))
                (some-fn #(not (contains? % :to-within))
                         (comp within-valid? :to-within))
                (some-fn #(not (contains? % :by-within))
                         (comp within-valid? :by-within)))))

(def config-valid?
  (every-pred coll?
              #(every? assertion-config-valid? %)))
