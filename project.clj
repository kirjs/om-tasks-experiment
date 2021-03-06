(defproject tasks "0.1.0-SNAPSHOT"
  :description "Tasks timer"
  :url "http://example.com/FIXME"

  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.7.1"]
                 [alandipert/storage-atom "1.2.3"]
                 ]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "tasks"
              :source-paths ["src"]
              :compiler {
                :output-to "out/tasks.js"
                :output-dir "out"
                :optimizations :none
}}]})
