(ns lauzer-wars-2.system.core
  (:require [lauzer-wars-2.system.movement :as movement]
            [lauzer-wars-2.system.action :as action]))

(def systems
  [action/apply-system movement/apply-system])

(defn apply-systems
  "Applies every game system to the game state in order.
  Each system has the signature [state events cofx], where state is the current game state, events is a map
  of \"atomic\" events that have occurred in the current frame, i.e :player/died.
  Each system returns a map with the keys :state and :events, containing the updated state and events."
  [cofx state]
  (reduce (fn [{:keys [state events]} system]
            (let [applied (system state events cofx)]
              {:state  (if (contains? applied :state)
                         (:state applied)
                         state)
               :events (if (contains? applied :events)
                         (:events applied)
                         events)}))
          {:state state :events {}}
          systems))
