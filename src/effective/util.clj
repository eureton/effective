(ns effective.util
  (:require [clojure.set :as cljset]))

(defn contains-map? [h1]
  (fn [h2]
    (cljset/subset? (set h1) (set h2))))
