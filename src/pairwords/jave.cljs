(ns pairwords.jave
  (:require-macros [tailrecursion.javelin.macros :refer [cell with-let]])
  (:require [tailrecursion.javelin]
            [clojure.browser.event :as event]
            [goog.dom.forms :as form]
            [goog.dom.classes :as classes]))


(defn- aset-in
  "Sets the value in a nested JavaScript array, where ks is a sequence
  of fields."
  [arr ks value]
  (aset (reduce aget arr (butlast ks)) (last ks) value))


(defn insert!
  "Inserts string 's' as the value of the attribute 'field' in an
  element. 'field' defaults to 'value'. Provided additional 'fields', inserts
  the value at the specified path a-la assoc-in."
  ([elem s]
     (insert! elem s "value"))
  ([elem s field & fields]
     (aset-in elem (list* field fields) s)))


(defn parse-float
  "Attempts to parse s with js/parseFloat.  If parsing fails, returns
  default."
  [default s]
  (let [n (js/parseFloat s)]
    (if (js/isNaN n) default n)))


(defn parse-int
  "Attempts to parse s with js/parseInt.  If parsing fails, returns
  default."
  [default s]
  (let [n (js/parseInt s)]
    (if (js/isNaN n) default n)))


(defn input-to
  "Attaches form input element to the existing input cell backbone.

  If ks is empty, resets backbone to the input value.  If ks is
  non-empty, swaps backbone with assoc-in using ks as the path.

  Understands the following additional options:

  :insert-default? - If true, immediately insert the default value
  into the backing input.  Defaults to true.

  :type - The type to parse the form input's value string as.  Known
  types are: :string, :int, :float.  Defaults to :string.

  :triggers - A set of strings or keywords of event types that should
  trigger backbone mutation.  Event types are those known to
  clojure.browser.events.  For a comprehensive list, see
  http://closure-library.googlecode.com/svn/docs/closure_goog_events_eventtype.js.source.html.
  Defaults to #{\"input\"}.

  :validator - Function of a single argument that is applied to every
  new input value.  If it returns true, the new input value is added
  to the backbone.  The validator is applied to the existing backbone
  value once when this function is called, but its return value is
  discarded."
  [backbone ks elem
   & {:keys [triggers type insert-default? validator]
      :or {triggers #{"input"}
           type :string
           insert-default? true
           validator (constantly true)}}]

  (let [default (if (seq ks) (get-in @backbone ks) @backbone)
        parsers {:string identity
                 :float (partial parse-float default)
                 :int (partial parse-int default)}
        parser (parsers type)
        update! #(if (seq ks)
                   (swap! backbone assoc-in ks %)
                   (reset! backbone %))]

    (validator default)

    (if insert-default?
      (insert! elem (str default)))

    (doseq [t triggers]
      (event/listen elem
                    t
                    #(let [value (parser (form/getValue elem))]
                       (update! (if (validator value) value default)))))))


(defn form-cell
  "Creates and returns a cell backed by the form input element.
  Takes the same options as input-to plus:

  :default - The initial value of the created cell. Defaults to the
  empty string."
  [elem & opts]
  (let [default ((apply hash-map opts) :default "")]
    (with-let [in-cell (cell 'default)]
      (apply input-to in-cell [] elem opts))))
