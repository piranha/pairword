(ns pairwords.game
  (:require [solovyov.mesto :as me]
            [flapjax :as fj]
            [pairwords.util :refer [storageB list2li]]))

(defn init-game [world]
  (me/assoc-in world [:game :players] [])

  (-> (fj/clicksE "add-player")
      (fj/extractValueOnEventE "name-input")
      (.mapE #(me/update-in world [:game :players] conj %)))

  (let [players (storageB world [:game :players])
        li-s (fj/liftB list2li players)]
    (fj/insertValueB li-s "names-list" "innerHTML")))

