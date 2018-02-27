(ns lauzer-wars-2.core
  (:require [play-cljs.core :as p]
            [reagent.core :as r]
            [goog.events.KeyCodes :as keycodes]
            [lauzer-wars-2.dev-panels :as dev]))

(enable-console-print!)

(def initial-state {:text-x 20 :text-y 30})
(defonce game (p/create-game 500 300))
(defonce game-state (r/atom {:text-x 20 :text-y 30}))

(defn render [state]
  (p/render
    game
    [[:fill {:color "lightgrey"}
      [:rect {:x 0 :y 0 :width 499 :height 299}]]
     [:fill {:color "black"}
      [:text {:value (str "Pressed: " (:pressed-keys state)) :x (:text-x state) :y (:text-y state) :size 20 :font "Helvetica"}]]]))

(defn scroll [text-x dt]
  (if (> text-x 500)
    -50
    (+ text-x (* 100 dt))))

(def keymap
  {keycodes/W :up
   keycodes/A :left
   keycodes/S :down
   keycodes/D :right
   keycodes/F :shoot})

(def main-screen
  (reify p/Screen
    (on-show [this])
    (on-hide [this])

    (on-render [this]
      (let [dt (* (p/get-delta-time game) 0.001)
            pressed-keys (p/get-pressed-keys game)]
        (render @game-state)
        (swap! game-state update :text-x scroll dt)
        (swap! game-state assoc :pressed-keys (remove nil? (map keymap pressed-keys)))))))

(when dev/debug?
  (r/render [dev/dev-panel game-state initial-state]
            (.getElementById js/document "app")))

(doto game
  (p/start)
  (p/set-screen main-screen))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

(defn ^:export init []
  (when dev/debug? (dev/set-monitor-interval game-state)))
