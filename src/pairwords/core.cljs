(ns ^{:doc "Pairwords entry point"}
  pairwords.core
  (:require [flapjax :as fj]
            [pairwords.util :refer [log]]))

(defn bind-timer []
  (fj/mapE log (fj/clicksE "start-timer")))

(defn bind-timer2 []
  (-> "start-timer"
      fj/clicksE
      (.constantE 42)
      (fj/insertValueE "timer" "innerHTML")
      ))

(defn ^:export run []
  (bind-timer)
  (bind-timer2)
  (log "I'm running!"))
