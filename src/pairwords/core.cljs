(ns ^{:doc "Pairwords entry point"}
  pairwords.core
  (:require [flapjax :as fj]
            [solovyov.mesto :as me]
            [pairwords.util :refer [log logE storageB storeB finiteTimerE]]
            [pairwords.game :refer [init-game]]))

(def world (atom {}))

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
  (-> (finiteTimerE delay)
      (logE)))

(defn ^:export run []
  (me/on world []
         (fn [d p] (.log js/console
                         (pr-str (dissoc d :handlers))
                         (pr-str p))))

  (text-show-hide)
  (init-game world)
  (log "I'm running!"))
