(ns pairwords.templates
  (:require-macros [enfocus.macros :as em])
  (:require [enfocus.core :as ef]
            [domina]
            [flapjax :as fj]
            [pairwords.util :refer [log]]))


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

;; https://github.com/robert-stuttaford/demo-enfocus-pubsub-remote/blob/master/src-cljs/depr/view.cljs

(defn content [b]
  (fn [nodes]
    (doseq [el (domina/nodes nodes)]
      (fj/insertValueB b el "innerHTML"))))

(em/deftemplate player-template :compiled "templates/player.html"
  [player]
  ["li"] (ef/en-content player))

(em/deftemplate player-list-template :compiled "templates/player-list.html"
  [players]
  ["ul"] (apply ef/en-content (map player-template players))
  )

(em/deftemplate player-list-templateB :compiled "templates/player-list-count.html"
  [playersB]
  [".count"] (content (fj/liftB #(count %) playersB)))
