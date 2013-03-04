(ns ^{:doc "Pairwords entry point"}
  pairwords.core
  (:require-macros [tailrecursion.javelin.macros :refer [cell]])
  (:require [flapjax :as fj]
            [tailrecursion.javelin]
            [pairwords.util :refer [log logE finiteTimerB]]
            [pairwords.game :refer [init-game init-jave]]))

(def world (atom {}))
(def qorld (cell '{}))

(defn prevent-default [es]
  (fj/mapE #(do (.preventDefault %) %) es))

;; event stream here works and behavior runs with initial value,
;; but afterwards any events do not affect result. Why?
(defn text-show-hide []
  (-> (fj/clicksE "show")
      (prevent-default)
      (fj/collectE false (fn [_ val] (not val)))
      (logE)
      (fj/startsWith false)
      (fj/ifB "block" "none")
      (fj/insertValueB "text" "style" "display")))

(defn ^:export run-timer [delay]
  (-> (finiteTimerB delay)
      (fj/changes)
      (logE)))

(defn ^:export run []
  (add-watch world :all
             (fn [a k o n]
               #_ (.trace js/console)
               (.log js/console (pr-str n))))

  (text-show-hide)
  #_ (init-game world)
  (init-jave qorld)
  (log "I'm running!"))

(defn qwe []
  (swap! qorld assoc-in [:players 0] "harhar"))
