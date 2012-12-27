(ns pairwords.game
  (:require [solovyov.mesto :as me]
            [flapjax :as fj]
            [pairwords.util :refer [storageB]]
            [pairwords.templates :refer [list2li]]))

(defn init-game [world]
  (me/assoc-in world [:game :players] [])

  (-> (fj/clicksE "add-player")
      (fj/extractValueOnEventE "name-input")
      (fj/filterE #(not= % "")) ; filters empty input
      (.mapE #(me/update-in world [:game :players] conj %))
      (fj/constantE "") ; clear input after adding name
      (fj/insertValueE "name-input" "value"))

  (let [players (storageB world [:game :players])
        li-s (fj/liftB list2li players)]
    (fj/insertValueB li-s "names-list" "innerHTML")))

