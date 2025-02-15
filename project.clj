(defproject com.eureton/effective "0.11.0"
  :description "RSpec-style expect-to-change assertions for Clojure."
  :url "https://github.com/eureton/effective"
  :license {:name "MIT"
            :url "https://github.com/eureton/effective/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.flatland/useful "0.11.6"
                  :exclusions [org.clojure/tools.macro
                               org.clojure/tools.reader]]
                 [metosin/malli "0.16.2"]]
  :repl-options {:init-ns effective.core})
