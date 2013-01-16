(ns pairwords.macros)

(defmacro defproc [name args & body]
  `(defn ~name ~(vec (rest args))
     (fn [~'nodes]
       (doseq [~(first args) (domina/nodes ~'nodes)]
         ~@body))))
