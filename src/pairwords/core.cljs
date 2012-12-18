(ns ^{:doc "Pairwords entry point"}
  pairwords.core
  (:require [flapjax :as fj]
            [pairwords.util :refer [log]]))

(defn prevent-default [es]
  (fj/mapE #(do (.preventDefault %) %) es))


;; event stream here works and behavior runs with initial value,
;; but afterwards any events do not affect result. Why?
(defn text-show-hide []
  (-> (fj/clicksE "show")
      (prevent-default)
      (fj/collectE false (fn [_ val] (not val)))
      (.mapE #(do (log %) %))
      (fj/startsWith false)
      (fj/ifB "block" "none")
      ;; should be "text" "style" "display" to change visibility
      (fj/insertValueB "text" "style" "display")))

(defn ^:export run []
  (text-show-hide)
  (log "I'm running!"))
