(ns pairwords.game
  (:require [solovyov.mesto :as me]
            [flapjax :as fj]
            [pairwords.util :refer [logE storageB popValueOnEventE]]
            [pairwords.templates :refer [list2li]]))

(defn next-players [world]
  (take 2 (me/get-in @world [:game :players])))

(defn init-game [world]
  (me/assoc-in world [:game :players] [])
  (me/assoc-in world [:game :state] :entering-players)

  (-> (fj/clicksE "add-player")
      (popValueOnEventE "name-input")
      (fj/filterE #(not= % "")) ; filters empty input
      (.mapE #(me/update-in world [:game :players] conj %)))

  (-> (fj/clicksE "start-game")
      (fj/filterE #(<= 3 (count (me/get-in @world [:game :players]))))
      (.mapE #(me/assoc-in world [:game :state] :starting-round)))

  (-> (storageB world [:game :state])
      (fj/changes)
      (fj/filterE #(= % :starting-round))
      (.mapE #(next-players world))
      (.mapE list2li)
      (fj/insertValueE "current-players" "innerHTML"))

  (let [players (storageB world [:game :players])
        li-s (fj/liftB list2li players)]
    (fj/insertValueB li-s "names-list" "innerHTML"))

  (fj/insertValueB (storageB world [:game :state])
                   "game-state" "innerHTML"))

