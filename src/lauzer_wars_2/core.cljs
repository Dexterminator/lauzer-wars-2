(ns lauzer-wars-2.core
  (:require [play-cljs.core :as p]
            [reagent.core :as r]
            [lauzer-wars-2.cofx :as cofx]
            [lauzer-wars-2.constants :as constants]
            [lauzer-wars-2.render :as render]
            [lauzer-wars-2.dev-panels :as dev]
            [lauzer-wars-2.system.core :as system]
            [lauzer-wars-2.side-effects :as side-effects]))

(enable-console-print!)

(defonce game (p/create-game 500 300))
(defonce ^:private game-state (atom constants/initial-state))

(defn process-frame! []
  (let [cofx (cofx/get-cofx! game)
        {:keys [state events]} (system/apply-systems cofx @game-state)]
    (side-effects/do-side-effects! events)
    (render/render-frame! state game)
    (reset! game-state state)
    (when dev/debug? (dev/update-latest-events! events))))

(def main-screen
  (reify p/Screen
    (on-show [this])
    (on-hide [this])

    (on-render [this]
      (process-frame!))))

(doto game
  (p/start)
  (p/set-screen main-screen))

(when dev/debug?
  (dev/render-dev-panel game-state))

(defn on-js-reload [])

(defn ^:export init []
  (when dev/debug? (dev/set-monitor-interval game-state)))
