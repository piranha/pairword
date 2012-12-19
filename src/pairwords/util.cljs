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


(defn finiteTimerE
  ([delay] (finiteTimerE delay 100))
  ([delay interval]
     (let [timer (fj/timerE interval)]
       (-> timer
           (fj/delayE delay)
           (.mapE #(fj/disableTimer timer)))
       timer)))
