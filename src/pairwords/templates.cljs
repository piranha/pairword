(ns pairwords.tempjave
  (:refer-clojure :exclude [list])
  (:require-macros [tailrecursion.javelin.macros :refer [cell]]
                   [enfocus.macros :as em]
                   [pairwords.macros :refer [defproc]])
  (:require [tailrecursion.javelin]
            [domina.css :as css]
            [pairwords.util :refer [log]]
            [pairwords.jave :refer [insert! input-to click-to form-cell]]))


(defn sel
  ([expr]
     (domina/single-node (css/sel expr)))
  ([base expr]
     (domina/single-node (css/sel base expr))))

(defproc append [el fragment]
  (.appendChild el fragment))

(defproc attr [el data & fields]
  (cell (apply insert! el data fields)))

(defproc list [el data item-template]
  (cell ((fn [cnt]
           ;; TODO: maybe do not erase here and instead track elements
           ;; but no ideas how to do that right now
           (insert! el "" "innerHTML")
           (doseq [i (range cnt)
                   :let [itemC (cell (nth data i))]]
             (.appendChild el (item-template itemC))))
         (count data))))

;;; state

(em/deftemplate game-state :compiled "templates/game-state.html"
  [gameC]
  ["strong"] (attr (cell (gameC :state)) "innerHTML"))


;;; setup

(em/deftemplate player :compiled "templates/player.html"
  [playerC]
  ["li"] (attr playerC "innerHTML"))

(em/deftemplate player-list :compiled "templates/player-list.html"
  [playersC]
  [".count"] (attr (cell (count playersC)))
  ["ul"] (list playersC player)
  )

(em/deftemplate setup-form :compiled "templates/setup-form.html"
  [gameC]
  ["[name=name]"] (attr (cell (gameC :name "")) "value"))

(defn setup-form-c
  [frag]
  (let [form (cell '{:name ""})]
    (input-to form [:name] (sel frag "[name=name]"))
    (click-to form [:add-player] (sel frag "[name=add]"))
    (click-to form [:start-game] (sel frag "[name=start]"))

    form))

(em/deftemplate game-setup :compiled "templates/game-setup.html"
  [gameC form]
  [":first-child"] (attr
                    (cell (if (= (gameC :state) :entering-players) "block" "none"))
                    "style" "display")
  [".game-setup"] (append (player-list (cell (gameC :players))))
  [".game-setup"] (append form))


;;; round

(em/deftemplate game-round :compiled "templates/round.html"
  [gameC]
  [":first-child"] (attr
                    (cell (if (= (gameC :state) :round) "block" "none"))
                    "style" "display"))


;;; game

(em/deftemplate game :compiled "templates/game.html"
  [gameC form]
  [".state"] (append (game-state gameC))
  [".tabs"] (append (game-setup gameC form))
  [".tabs"] (append (game-round gameC)))
