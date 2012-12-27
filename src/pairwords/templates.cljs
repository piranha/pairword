(ns pairwords.templates)


(defn list2li [v]
  (if (empty? v)
    ""
    (apply + (map #(+ "<li>" % "</li>") v))))

(defn msec2str [msec]
  (format "%.1f" (/ msec 1000.)))

