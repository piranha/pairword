(ns pairwords.templates
  (:require-macros [enfocus.macros :as em])
  (:require [enfocus.core :as ef]))


(defn list2li [v]
  (if (empty? v)
    ""
    (apply + (map #(+ "<li>" % "</li>") v))))

(defn msec2str [msec]
  (format "%.1f" (/ msec 1000.)))

;; ;; ну это не ем/дефтемплейт, конечно
;; (deftemplate user-template [user]
;;   "<li></li>"
;;   "li" [:content (user :name)])

;; (deftemplate user-list-template [users]
;;   "<ul></ul>"
;;   "ul" [:list users #{:id}])


(em/deftemplate player-template :compiled "templates/player.html"
  [player]
  ["li"] (em/content player))

(em/deftemplate player-list-template :compiled "templates/player-list.html"
  [players]
  ["ul"] (apply ef/en-content (map player-template players)))
