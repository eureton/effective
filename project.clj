(defproject com.eureton/effective "0.10.0"
  :description "RSpec-style expect-to-change assertions for Clojure."
  :url "https://github.com/eureton/effective"
  :license {:name "MIT"
            :url "https://github.com/eureton/effective/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.flatland/useful "0.11.6"]
                 [metosin/malli "0.16.2"]]
  :repl-options {:init-ns effective.core})
