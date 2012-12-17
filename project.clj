(defproject pairwords "0.0.1"
  :description "Explaining words in pairs"
  :source-paths ["deps/clojurescript/src/clj"
                 "deps/clojurescript/src/cljs"]
  :plugins [[lein-cljsbuild "0.2.9" :hooks false]]
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :cljsbuild {:builds
              {
               :main {
                      :source-path "src"
                      :compiler {
                                 :output-to "build/pairwords.js"
                                 :optimizations :whitespace
                                 :pretty-print true
                                 :foreign-libs [{:file "http://www.flapjax-lang.org/download/flapjax-2.1.js"
                                                 :provides "flapjax"}]                                 }
                      }
               :mini {
                      :source-path "src"
                      :compiler {
                                 :output-to "build/mini.js"
                                 :optimizations :advanced
                                 :foreign-libs [{:file "http://www.flapjax-lang.org/download/flapjax-2.1.js"
                                                 :provides "flapjax"}]                                 }
                      }
               }}
)
