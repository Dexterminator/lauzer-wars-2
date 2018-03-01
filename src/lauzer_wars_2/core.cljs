(ns lauzer-wars-2.core
  (:require [play-cljs.core :as p]
            [reagent.core :as r]
            [lauzer-wars-2.cofx :as cofx]
            [lauzer-wars-2.constants :as constants]
            [lauzer-wars-2.render :as render]
            [lauzer-wars-2.system.movement :as movement]
            [lauzer-wars-2.system.action :as action]
            [lauzer-wars-2.dev-panels :as dev]))

(enable-console-print!)

(defonce game (p/create-game 500 300))
(defonce game-state (r/atom constants/initial-state))
(defonce latest-events (r/atom {}))

(defn scroll [text-x dt]
  (if (> text-x 500)
    -50
    (+ text-x (* 100 dt))))

(defn scroll-system [state events {:keys [dt]}]
  {:state  (update state :text-x scroll (* dt 0.001))
   :events (assoc events :scrolled true)})

(defn add-visible-input-system [state events {:keys [input]}]
  {:state  (assoc state :pressed-keys input)
   :events (assoc events :scolled true)})

(def systems
  [scroll-system add-visible-input-system action/apply-system movement/apply-system])

(defn apply-systems
  "Applies every game system to the game state in order.
  Each system has the signature [state events cofx], where state is the current game state, events is a map
  of \"atomic\" events that have occurred in the current frame, i.e :player/died.
  Each system returns a map with the keys :state and :events, containing the updated state and events."
  [cofx state]
  (reduce (fn [{:keys [state events]} system]
            (system state events cofx))
          {:state state :events {}}
          systems))

(defn do-side-effects! [events]
  (when (contains? events ::action/shot)
    (js/console.log "shot")))

(defn merge-latest-events [events new-events]
  (let [date (js/Date.)
        time (.toLocaleTimeString (js/Date.) "sv-SWE")
        millis (.getMilliseconds date)
        ts (str time ":" millis)]
    (reduce (fn [events [k v]]
              (assoc events k {:ts     ts
                               :params v}))
            events
            new-events)))

(def main-screen
  (reify p/Screen
    (on-show [this])
    (on-hide [this])

    (on-render [this]
      (let [cofx (cofx/get-cofx! game)
            {:keys [state events]} (apply-systems cofx @game-state)]
        (do-side-effects! events)
        (render/render-frame! state game)
        (reset! game-state state)
        (when dev/debug? (swap! latest-events merge-latest-events events))))))

(when dev/debug?
  (r/render [dev/dev-panel game-state latest-events constants/initial-state]
            (.getElementById js/document "app")))

(doto game
  (p/start)
  (p/set-screen main-screen))

(defn on-js-reload [])

(defn ^:export init []
  (when dev/debug? (dev/set-monitor-interval game-state latest-events)))
