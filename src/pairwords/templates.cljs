(ns pairwords.templates)


(defn list2li [v]
  (if (empty? v)
    ""
    (apply + (map #(+ "<li>" % "</li>") v))))

