(ns lauzer-wars-2.dev-panels
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(def debug? ^boolean js/goog.DEBUG)

(when debug?
  (rf/reg-event-db
    :game-state-update
    (fn [db [_ arg]]
      arg)))

(defonce saved-state (atom {}))

(defn initial-state-button [state initial-state]
  [:button {:on-click #(reset! state initial-state)} "Initial state"])

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
   (let [new-interval-id (js/setInterval #(rf/dispatch-sync [:game-state-update @state]) ms)]
     (reset! current-interval-id new-interval-id))))

(defn set-monitor-interval-panel [state]
  [:div.set-interval-panel
   [:span
    "Monitor game state every "
    [:input {:value     (or @current-interval-ms default-monitor-interval-ms)
             :on-change #(reset! current-interval-ms (-> % .-target .-value))
             :on-blur   #(set-monitor-interval state @current-interval-ms)}]
    " ms"]])

(defn dev-panel []
  (let [saved-state-ts (r/atom nil)]
    (fn [state initial-state]
      [:div.main-panel
       [set-monitor-interval-panel state]
       [:div.state-buttons
        [save-state-button state saved-state-ts]
        [restore-state-button state]
        [initial-state-button state initial-state]
        [saved-state-ts-panel @saved-state-ts]]])))
