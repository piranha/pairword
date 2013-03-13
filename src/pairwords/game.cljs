(ns pairwords.game
  (:require-macros [tailrecursion.javelin.macros :refer [cell]])
  (:require [tailrecursion.javelin]
            [pairwords.util :refer [appendTo log]]
            [pairwords.templates :as t]))

(def game-length 2000) ; milliseconds

(defn next-players [world]
  (take 2 (me/get-in @world [:players])))

(defn on [value action]
  (when value
    (action)))

(defn init-game [world]
  (reset! world {:players []
                 :state :entering-players})

  (cell (log world))

  (cell (on (get-in world [:form :add-player])
            #(let [player (get-in @world [:form :name])]
               (if player
                 (swap! world update-in [:players] conj player))
               ;; FIXME: this is not working
               ;; probably because we're in a middle of cell action
               (swap! world assoc-in [:form :name] "zxc"))))

  (cell (on (get-in world [:form :start-game])
            #(let [players (get-in @world [:players])]
               (if (>= (count players) 3)
                 (swap! world assoc-in [:state] :starting-round)))))

  (let [form (t/setup-form (cell (world :form {})))
        formC (t/setup-form-c form)
        frag (t/game world form)]
    (cell (swap! '(cell world) assoc-in [:form] formC))
    (appendTo "game" frag)))


(defn init-old [world]
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
