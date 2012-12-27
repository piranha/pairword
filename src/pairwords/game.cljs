(ns pairwords.game
  (:require [solovyov.mesto :as me]
            [flapjax :as fj]
            [pairwords.util :refer [storageB removeValueOnEventE]]
            [pairwords.templates :refer [list2li]]))

(defn init-game [world]
  (me/assoc-in world [:game :players] [])

  (-> (fj/clicksE "add-player")
      (removeValueOnEventE "name-input")
      (fj/filterE #(not= % "")) ; filters empty input
      (.mapE #(me/update-in world [:game :players] conj %)))

  (let [players (storageB world [:game :players])
        li-s (fj/liftB list2li players)]
    (fj/insertValueB li-s "names-list" "innerHTML")))

