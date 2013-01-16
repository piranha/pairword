(ns pairwords.game
  (:require [solovyov.mesto :as me]
            [flapjax :as fj]
            [pairwords.util :refer [logE storageB popValueOnEventE finiteTimerB
                                    appendTo]]
            [pairwords.templates :refer [list2li msec2str player-list-template
                                         player-list-templateB]
             :as t]))

(def game-length 2000) ; milliseconds

(defn next-players [world]
  (take 2 (me/get-in @world [:game :players])))

(defn init-game [world]
  (me/assoc-in world [:game :players] [])
  (me/assoc-in world [:game :state] :entering-players)

  ;;; INFO
  
  ;;; SETUP

  ;; (-> (fj/mergeE
  ;;      (fj/clicksE "add-player")
  ;;      (-> (fj/extractEventE "name-input" "keyup")
  ;;          (fj/filterE #(= (.-keyCode %) 13))))
  ;;     (popValueOnEventE "name-input")
  ;;     (fj/filterE #(not= % "")) ; filters empty inputs
  ;;     (.mapE #(me/update-in world [:game :players] conj %)))

  ;; (-> (fj/clicksE "start-game")
  ;;     (fj/filterE #(<= 3 (count (me/get-in @world [:game :players]))))
  ;;     (.mapE #(me/assoc-in world [:game :state] :starting-round)))

  (appendTo "game"
            (t/game (storageB world [:game])))
  
  ;; (-> (storageB world [:game :state])
  ;;     (fj/changes)
  ;;     (fj/filterE #(= :starting-round %))
  ;;     (.mapE #(list2li (next-players world)))
  ;;     (fj/insertValueE "current-players" "innerHTML"))

  ;; (-> (fj/clicksE "start-round")
  ;;     (fj/filterE #(= :starting-round (me/get-in @world [:game :state])))
  ;;     (.mapE #(do (me/assoc-in world [:game :state] :round)
  ;;                 (-> (finiteTimerB 2000)
  ;;                     (.liftB msec2str)
  ;;                     (fj/insertValueB "to-the-end" "innerHTML"))
  ;;                 %))
  ;;     (fj/delayE game-length)
  ;;     (.mapE #(me/assoc-in world [:game :state] :end-round)))

  )
