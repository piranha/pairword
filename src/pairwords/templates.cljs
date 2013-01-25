(ns pairwords.templates
  (:refer-clojure :exclude [list])
  (:require-macros [enfocus.macros :as em]
                   [pairwords.macros :refer [defproc]])
  (:require [enfocus.core :as ef]
            [domina.css :as css]
            [flapjax :as fj]
            [pairwords.util :refer [log narrowB extractEventB]]))


(defn list2li [v]
  (if (empty? v)
    ""
    (apply + (map #(+ "<li>" % "</li>") v))))

(defn msec2str [msec]
  (format "%.1f" (/ msec 1000.)))

(defn sel
  ([expr]
     (domina/single-node (css/sel expr)))
  ([base expr]
     (domina/single-node (css/sel base expr))))

;;; template processors

(defproc append [el fragment]
  (.appendChild el fragment))

(defproc content [el b]
  (fj/insertValueB b el "innerHTML"))

(defproc attr [el b name-path value]
  (apply fj/insertValueB (fj/liftB #(if % value "") b)
         el (.split name-path ".")))

;; idea for the future: supply [optional] set of keys to identify items in list,
;; so that it's possible to track re-ordering to minimize reflow/rendering/etc
(defproc list [el b item-template]
  (let [countB (fj/liftB count b)]
    (fj/liftB
     (fn [n]
       ;; TODO: this is of course not fun at all, at the very
       ;; minimum it should not delete elements which are still
       ;; used
       (aset el "innerHTML" "")
       (doseq [i (range n)
               :let [itemB (fj/liftB #(nth % i) b)]]
         (.appendChild el (item-template itemB))))
     countB)))

;;; state

(em/deftemplate game-state :compiled "templates/game-state.html"
  [gameB]
  ["strong"] (content (narrowB gameB [:state])))

;;; setup

(em/deftemplate player :compiled "templates/player.html"
  [playerB]
  ["li"] (content playerB))


(em/deftemplate player-list :compiled "templates/player-list.html"
  [playersB]
  [".count"] (content (fj/liftB count playersB))
  ["ul"] (list playersB player))


(em/deftemplate setup-form :compiled "templates/setup-form.html"
  [gameB])

(em/deftemplate game-setup :compiled "templates/game-setup.html"
  [gameB form]
  [":first-child"] (attr
                    (fj/liftB #(not= :entering-players (% :state)) gameB)
                    "style.display" "none")
  [".game-setup"] (append (player-list (narrowB gameB [:players])))
  [".game-setup"] (append form))

(defn setup-form-b
  [frag]
  (fj/liftB
   (fn [& args] (apply hash-map args))

   :name (fj/extractValueB (sel frag "[name=name]"))
   :add-player (extractEventB (sel frag "[name=add]") "click")
   :start-game (extractEventB (sel frag "[name=start]") "click")))

;;; round

(em/deftemplate game-round :compiled "templates/round.html"
  [gameB]
  [":first-child"] (attr
                    (fj/liftB #(= :entering-players (% :state)) gameB)
                    "style.display" "none"))

;;; game

(em/deftemplate game :compiled "templates/game.html"
  [gameB form]
  [".state"] (append (game-state gameB))
  [".tabs"] (append (game-setup gameB form))
  [".tabs"] (append (game-round gameB)))
