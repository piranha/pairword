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
            #(do (swap! world update-in [:players] conj
                        (get-in @world [:form :name]))
                 ;; FIXME: this is not working
                 ;; probably because we're in a middle of cell action
                 ;; argh
                 (swap! world assoc-in [:form :name] "zxc"))))

  (let [form (t/setup-form (cell (world :form {})))
        formC (t/setup-form-c form)
        frag (t/game world form)]
    (cell (swap! '(cell world) assoc-in [:form] formC))
    (appendTo "game" frag)))


(defn init-old [world]
  (reset! world {:players []
                 :state :entering-players})

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
