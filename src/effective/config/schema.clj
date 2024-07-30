(ns effective.config.schema)

(def ^:private ^:const OPT {:optional true})

(def hook
  "True if input represents a function, false otherwise."
  [:or
   [:and symbol? [:fn (comp fn? var-get resolve)]]
   [:and list? [:fn (comp symbol? first)]]])
