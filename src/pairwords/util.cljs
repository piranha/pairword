(ns pairwords.util)


(defn log [& args]
  (.apply (.-log js/console) js/console
          (into-array (map clj->js args))))


(defn appendTo [id frag]
  (let [parent (.getElementById js/document id)]
    (.appendChild parent frag)))
