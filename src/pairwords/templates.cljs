(ns pairwords.templates
  (:require-macros [enfocus.macros :as em])
  (:require [enfocus.core :as ef]
            [domina]
            [flapjax :as fj]
            [pairwords.util :refer [log]]))


(defn list2li [v]
  (if (empty? v)
    ""
    (apply + (map #(+ "<li>" % "</li>") v))))

(defn msec2str [msec]
  (format "%.1f" (/ msec 1000.)))

;; improvement needed:
;;   (defproc name [el b ....] & body)
;; ->
;;   (defn name [b ..] (fn [nodes] (doseq [el (domina/nodes nodes)] & body)))

(defn content [b]
  (fn [nodes]
    (doseq [el (domina/nodes nodes)]
      (fj/insertValueB b el "innerHTML"))))

;; idea for the future: supply [optional] set of keys to identify items in list,
;; so that it's possible to track re-ordering to minimize reflow/rendering/etc
(defn list [b item-template]
  (fn [nodes]
    (let [countB (fj/liftB #(count %) b)]
      (doseq [el (domina/nodes nodes)]
        (fj/liftB (fn [n]
                    ;; TODO: this is of course not fun at all, at the very
                    ;; minimum it should not delete elements which are still
                    ;; used
                    (aset el "innerHTML" "")
                    (doseq [i (range n)
                            :let [itemB (fj/liftB #(nth % i) b)]]
                      (.appendChild el (item-template itemB))))
                  countB)))))


(em/deftemplate player-templateB :compiled "templates/player.html"
  [playerB]
  ["li"] (content playerB))


(em/deftemplate player-list-templateB :compiled "templates/player-list.html"
  [playersB]
  [".count"] (content (fj/liftB #(count %) playersB))
  ["ul"] (list playersB player-templateB))
