(ns ^{:doc "Pairwords entry point"}
  pairwords.core
  (:require [flapjax :as fj]
            [pairwords.util :refer [log]]))

(defn prevent-default [es]
  (fj/mapE #(.preventDefault %) es))

(defn bind-timer []
  (fj/mapE log (fj/clicksE "start-timer")))

(defn bind-timer2 []
  (-> "start-timer"
      fj/clicksE
      (.constantE 42)
      (fj/insertValueE "timer" "innerHTML")
      ))

;; event stream here works and behavior runs with initial value,
;; but afterwards any events do not affect result. Why?
(defn text-show-hide []
  (-> (fj/clicksE "show")
      (prevent-default)
      (fj/collectE false (fn [_ val] (not val)))
      (.mapE log)
      (fj/startsWith false)
      (fj/ifB "block" "none")
      ;; could be "text" "style" "display" to change visibility
      (fj/insertValueB "text" "data-display")))

(defn ^:export run []
  (bind-timer)
  (bind-timer2)
  (text-show-hide)
  (log "I'm running!"))
