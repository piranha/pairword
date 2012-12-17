(ns ^{:doc "Pairwords entry point"}
  pairwords.core
  (:require [flapjax :as fj]))

(defn- to-string [obj]
  (if (and (satisfies? cljs.core.ISeqable obj)
           (not (instance? js/String obj)))
    (pr-str obj)
    obj))

(defn log [& args]
  (.apply (.-log js/console) js/console
          (into-array (map to-string args))))

(defn bind-timer []
  (fj/mapE log (fj/clicksE "start-timer")))

(defn bind-timer2 []
  (-> "start-timer"
      fj/clicksE
      (.constantE 42)
      (.insertValueE "timer" "innerHTML")
      ))

(defn ^:export run []
  (bind-timer)
  (bind-timer2)
  (log "I'm running!"))
