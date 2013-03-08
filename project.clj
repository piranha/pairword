(defproject pairwords "0.0.1"
  :description "Explaining words in pairs"
  :plugins [[lein-cljsbuild "0.3.0" :hooks false]]
  :dependencies [[enfocus "1.0.0"]
                 [tailrecursion/javelin "1.0.0-SNAPSHOT"]]
  :cljsbuild
  {:builds
   [{:id "main"
     :source-paths ["src"]
     :compiler {:pretty-print true,
                :output-to "build/pairwords.js",
                :foreign-libs [{:provides "flapjax",
                                :file "http://www.flapjax-lang.org/download/flapjax-2.1.js"}],
                :optimizations :whitespace}}
    {:id "mini"
     :source-paths ["src"]
     :compiler {:output-to "build/mini.js",
                :foreign-libs [{:provides "flapjax",
                                :file "http://www.flapjax-lang.org/download/flapjax-2.1.js"}],
                :externs ["externs/flapjax.js"],
                :optimizations :advanced}}]}
)
