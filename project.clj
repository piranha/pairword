(defproject pairwords "0.0.1"
  :description "Explaining words in pairs"
  :plugins [[lein-cljsbuild "0.2.10" :hooks false]]
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :cljsbuild {:builds
              {
               :main {
                :source-path "src"
                :compiler {
                 :output-to "build/pairwords.js"
                 :optimizations :whitespace
                 :pretty-print true
                 :foreign-libs [{:file "http://www.flapjax-lang.org/download/flapjax-2.1.js"
                                 :provides "flapjax"}]}
                }
               :mini {
                :source-path "src"
                :compiler {
                 :output-to "build/mini.js"
                 :optimizations :advanced
                 :foreign-libs [{:file "http://www.flapjax-lang.org/download/flapjax-2.1.js"
                                 :provides "flapjax"}]}
                }
               }}
)
