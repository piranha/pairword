(ns pairwords.util
  (:require [solovyov.mesto :as me]
            [flapjax :as fj]))


(defn- to-string [obj]
  (if (and (satisfies? cljs.core.ISeqable obj)
           (not (instance? js/String obj)))
    (pr-str obj)
    obj))

(defn log [& args]
  (.apply (.-log js/console) js/console
          (into-array (map to-string args))))

(defn logE [e]
  (fj/mapE log e)
  e)

(defn storageB [world path]
  (let [init (me/get-in @world path)
        es (fj/receiverE)]
    (me/on world path
           (fn [data path]
             (.sendEvent es data)))
    (fj/startsWith es init)))


(defn storeB [b world path]
  (let [current (fj/valueNow b)
        es (fj/changes b)]
    (me/assoc-in world path current)
    (.mapE es #(me/assoc-in world path %))))


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
