(ns ^{:doc "Pairwords entry point"}
  pairwords.core
  (:require [flapjax :as fj]
            [solovyov.mesto :as me]
            [pairwords.util :refer [log storageB]]))

(def world (atom {}))

(defn prevent-default [es]
  (fj/mapE #(do (.preventDefault %) %) es))

(defn log-e [e]
  (log e)
  e)

;; event stream here works and behavior runs with initial value,
;; but afterwards any events do not affect result. Why?
(defn text-show-hide []
  (-> (fj/clicksE "show")
      (prevent-default)
      (fj/collectE false (fn [_ val] (not val)))
      (.mapE log-e)
      (fj/startsWith false)
      (fj/ifB "block" "none")
      (fj/insertValueB "text" "style" "display")))

(defn ^:export run []
  (me/on world []
         (fn [d p] (.log js/console
                         (pr-str (dissoc d :handlers))
                         (pr-str p))))

  (me/assoc-in world [:value] "ololoe")
  (-> (fj/extractValueE "input")
      (.mapE #(me/assoc-in world [:value] %)))
  (let [b (storageB world [:value])]
    (fj/insertValueB b "display-value" "innerHTML")
    (fj/insertValueB b "input" "value"))

  (text-show-hide)
  (log "I'm running!"))
