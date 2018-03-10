(ns lauzer-wars-2.core
  (:require [play-cljs.core :as p]
            [lauzer-wars-2.cofx :as cofx]
            [lauzer-wars-2.constants :as constants]
            [lauzer-wars-2.initial-state :as initial-state]
            [lauzer-wars-2.render :as render]
            [lauzer-wars-2.dev-panels :as dev]
            [lauzer-wars-2.system.core :as system]
            [lauzer-wars-2.side-effects :as side-effects]
            [reagent.core :as r]))

(enable-console-print!)

(defonce game (p/create-game constants/width constants/height))
(defonce ^:private game-state (atom {}))
(defonce ^:private fps (r/atom nil))
(defonce ^:private frame-count-accumulator (atom 0))
(defonce ^:private frame-count-interval-id (atom nil))

(defn set-fps-interval []
  (js/clearInterval @frame-count-interval-id)
  (let [new-interval-id (js/setInterval (fn []
                                          (reset! fps @frame-count-accumulator)
                                          (reset! frame-count-accumulator 0))
                                        1000)]
    (reset! frame-count-interval-id new-interval-id)))

(defn update-frame-count! []
  (swap! frame-count-accumulator inc))

(defn process-frame! []
  (let [cofx (cofx/get-cofx! game)
        {:keys [id->entity events]} (system/apply-systems cofx (:id->entity @game-state))
        updated-state (assoc @game-state :id->entity id->entity)]
    (side-effects/do-side-effects! events)
    (render/render-frame! updated-state game)
    (reset! game-state updated-state)
    (when dev/debug? (dev/update-latest-events! events)
                     (update-frame-count!))))

(def main-screen
  (reify p/Screen
    (on-show [this]
      (reset! game-state (initial-state/initial-state game)))
    (on-hide [this])

    (on-render [this]
      (process-frame!))))

(defonce started (doto game
                   (p/start)
                   (p/set-screen main-screen)))

(when dev/debug?
  (dev/render-dev-panel game-state game fps))

(defn on-js-reload [])

(defn ^:export init []
  (when dev/debug? (dev/set-monitor-interval game-state)
                   (set-fps-interval)))
