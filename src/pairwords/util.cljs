(ns pairwords.util
  (:require [flapjax :as fj]))


(defn log [& args]
  (.apply (.-log js/console) js/console
          (into-array (map clj->js args))))


(defn logE [e]
  (fj/mapE log e)
  e)

(let [atom-id (atom 0)]
  (defn atomB
    ([atom path]
       (let [init (get-in @atom path)
             es (fj/receiverE)]
         (add-watch atom (swap! atom-id inc)
                    (fn [key ref old new]
                      (log "update"
                           (if (= new @ref) "expected" "NOT expected"))
                      (log "old" (pr-str old))
                      (log "new" (pr-str new))
                      (log "ref" (pr-str @ref))
                      (let [old (get-in old path)
                            new (get-in new path)]
                        ;; flapjax has no notion of 'identical data' :(
                        (if (not= old new)
                          (.sendEvent es new)))))
         (fj/startsWith es init)))
    ([atom]
       (atomB atom []))))

(defn narrowB
  ([b path]
     (fj/liftB #(get-in % path) b))
  ([b path not-found]
     (fj/liftB #(get-in % path not-found) b)))


;; идея - бехавиор всë время фолс, после ивента и еще 1 мс (минимальный
;; промежуток времени) - тру
(defn extractEventB [el event-name]
  (let [es (fj/receiverE)]
    (.addEventListener el event-name
                       (fn [e]
                         (.sendEvent es true)
                         (js/requestAnimationFrame #(.sendEvent es false))))
    (fj/startsWith es false)))


(defn finiteTimerB
  ([delay] (finiteTimerB delay 100))
  ([delay interval]
     (let [timer (fj/timerB interval)
           end (+ delay (.valueNow timer))]
       (-> timer
           (fj/changes)
           (fj/onceE)
           (fj/delayE delay)
           (.mapE #(fj/disableTimer timer)))
       (fj/liftB #(- end %) timer))))


(defn popValueOnEventE [e id]
  (let [valueE (fj/snapshotE e (fj/extractValueB id))]
    (-> valueE
     (fj/constantE "")
     (fj/insertValueE id "value"))
    valueE))


(defn appendTo [id frag]
  (let [parent (.getElementById js/document id)]
    (.appendChild parent frag)))
