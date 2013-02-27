(ns pairwords.tempjave
  (:refer-clojure :exclude [list])
  (:require-macros [tailrecursion.javelin.macros :refer [cell]]
                   [enfocus.macros :as em]
                   [pairwords.macros :refer [defproc]])
  (:require [tailrecursion.javelin]
            [domina.css :as css]
            [pairwords.jave :refer [insert! input-to form-cell]]))


(defn sel
  ([expr]
     (domina/single-node (css/sel expr)))
  ([base expr]
     (domina/single-node (css/sel base expr))))

(defproc append [el fragment]
  (.appendChild el fragment))

(defproc attr [el data field]
  (cell (insert! el data field)))


;;; state

(em/deftemplate game-state :compiled "templates/game-state.html"
  [gameC]
  ["strong"] (attr (cell (gameC :state)) "innerHTML"))


;;; setup

(em/deftemplate setup-form :compiled "templates/setup-form.html"
  [gameC]
  ["[name=name]"] (attr (cell (gameC :name "")) "value"))

(defn setup-form-c
  [frag]
  (let [form (cell '{:name ""})]
    (input-to form [:name] (sel frag "[name=name]"))
    (input-to form [:add-player] (sel frag "[name=add]") :triggers #{"click"})
    (input-to form [:start-game] (sel frag "[name=start]") :triggers #{"click"})

    form))

(em/deftemplate game-setup :compiled "templates/game-setup.html"
  [gameC form]
  [".game-setup"] (append form))

(em/deftemplate game :compiled "templates/game.html"
  [gameC form]
  [".state"] (append (game-state gameC))
  [".tabs"] (append (game-setup gameC form)))
