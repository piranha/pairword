(ns pairwords.game
  (:require [flapjax :as fj]
            [pairwords.util :refer [logE popValueOnEventE finiteTimerB
                                    appendTo log narrowB atomB]]
            [pairwords.templates :refer [list2li msec2str player-list-template
                                         player-list-templateB]
             :as t]))

(def game-length 2000) ; milliseconds

(defn next-players [world]
  (take 2 (me/get-in @world [:players])))

(defn init-game [world]
  (reset! world {:players []
                 :state :entering-players})
  ;; (me/assoc-in world [] {:players []
  ;;                        :state :entering-players})
  ;; (me/assoc-in world [:players] [])
  ;; (me/assoc-in world [:state] :entering-players)

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

  (-> (atomB world [:form :add-player])
      (.liftB (fn [data]
                (log data)
                (when data
                  (swap! world update-in [:players] conj
                         (get-in @world [:form :name]))
                  #_ (swap! world assoc-in [:form] {})
                  ))))

  (let [gameB (atomB world)
        form (t/setup-form (narrowB gameB [:form] {}))
        formB (t/setup-form-b form)
        frag (t/game gameB form)]

    (fj/liftB #(swap! world assoc-in [:form] %) formB)
    (appendTo "game" frag))

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
