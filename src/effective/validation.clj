(ns effective.validation)

(def ^:private within-valid?
  (every-pred vector?
              (comp number? #(nth % 0))
              (comp #{:of} #(nth % 1))
              (comp number? #(nth % 2))))

(def ^:private assertion-config-valid?
  (every-pred map?
              (comp some? :to-change)
              (some-fn #(not (contains? % :from-within))
                       (comp within-valid? :from-within))
              (some-fn #(not (contains? % :to-within))
                       (comp within-valid? :to-within))
              (some-fn #(not (contains? % :by-within))
                       (comp within-valid? :by-within))))

(def config-valid?
  (every-pred coll?
              #(every? assertion-config-valid? %)))
