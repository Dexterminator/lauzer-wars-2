(ns lauzer-wars-2.dev-panels
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [lauzer-wars-2.initial-state :as initial-state]))

(def debug? ^boolean js/goog.DEBUG)
(defonce ^:private latest-events (atom {}))

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

(defn update-latest-events! [events]
  (swap! latest-events merge-latest-events events))

(when debug?
  (rf/reg-event-db
    :game-state-update
    (fn [db [_ arg]]
      arg)))

(defonce saved-state (atom {}))

(defn initial-state-button [state game]
  [:button {:on-click #(reset! state (initial-state/initial-state game))} "Initial state"])

(defn save-state-button [state saved-state-ts]
  [:button {:on-click (fn []
                        (reset! saved-state @state)
                        (reset! saved-state-ts (.toLocaleTimeString (js/Date.) "sv-SWE")))}
   "Save state"])

(defn restore-state-button [state]
  [:button {:on-click #(when (seq @saved-state) (reset! state @saved-state))} "Restore state"])

(defn saved-state-ts-panel [saved-state-ts]
  [:div.saved-state-ts (when saved-state-ts (str "Saved state at " saved-state-ts))])

(def default-monitor-interval-ms 100)
(defonce current-interval-id (atom nil))
(defonce current-interval-ms (r/atom default-monitor-interval-ms))

(defn set-monitor-interval
  ([state]
   (set-monitor-interval state default-monitor-interval-ms))
  ([state ms]
   (js/clearInterval @current-interval-id)
   (let [new-interval-id (js/setInterval #(rf/dispatch-sync [:game-state-update (assoc @state :events @latest-events)]) ms)]
     (reset! current-interval-id new-interval-id))))

(defn set-monitor-interval-panel [state events]
  [:div.set-interval-panel
   [:span
    "Monitor game state every "
    [:input {:value     (or @current-interval-ms default-monitor-interval-ms)
             :on-change #(reset! current-interval-ms (-> % .-target .-value))
             :on-blur   #(set-monitor-interval state @current-interval-ms)}]
    " ms"]])

(defn dev-panel []
  (let [saved-state-ts (r/atom nil)]
    (fn [state game events fps]
      [:div.main-panel
       [:div (str "FPS: " @fps)]
       [set-monitor-interval-panel state events]
       [:div.state-buttons
        [save-state-button state saved-state-ts]
        [restore-state-button state]
        [initial-state-button state game]
        [saved-state-ts-panel @saved-state-ts]]])))

(defn render-dev-panel [game-state game fps]
  (r/render [dev-panel game-state game latest-events fps]
            (.getElementById js/document "app")))
