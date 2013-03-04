(ns ^{:doc "Pairwords entry point"}
  pairwords.core
  (:require-macros [tailrecursion.javelin.macros :refer [cell]])
  (:require [tailrecursion.javelin]
            [pairwords.util :refer [log]]
            [pairwords.game :refer [init-game]]))

(def world (cell '{}))

(defn ^:export run []
  (init-game world)
  (log "I'm running!"))

(defn qwe []
  (swap! world assoc-in [:players 0] "harhar"))
