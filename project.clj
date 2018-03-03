(defproject lauzer-wars-2 "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [play-cljs "1.1.0"]
                 [cljsjs/howler "2.0.5-0"]]

  :plugins [[lein-figwheel "0.5.14"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src"]

  :cljsbuild {:builds
              [{:id           "dev"
                :source-paths ["src"]
                :figwheel     {:on-jsload "lauzer-wars-2.core/on-js-reload"}

                :compiler     {:main                 lauzer-wars-2.core
                               :asset-path           "js/compiled/out"
                               :output-to            "resources/public/js/compiled/lauzer_wars_2.js"
                               :output-dir           "resources/public/js/compiled/out"
                               :source-map-timestamp true
                               :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true}
                               :preloads             [devtools.preload day8.re-frame-10x.preload]}}
               {:id           "min"
                :source-paths ["src"]
                :compiler     {:main            lauzer-wars-2.core
                               :output-to       "resources/public/js/compiled/lauzer_wars_2.js"
                               :closure-defines {goog.DEBUG false}
                               :optimizations   :advanced
                               :infer-externs   true
                               :pretty-print    false}}]}

  :figwheel {:server-port 3556
             :css-dirs    ["resources/public/css"]}

  :profiles {:dev {:dependencies  [[binaryage/devtools "0.9.9"]
                                   [figwheel-sidecar "0.5.14"]
                                   [com.cemerick/piggieback "0.2.2"]
                                   [day8.re-frame/re-frame-10x "0.2.0"]]
                   :source-paths  ["src" "dev"]
                   :repl-options  {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                     :target-path]}})
