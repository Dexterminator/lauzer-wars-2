(ns lauzer-wars-2.system.collision
  (:require [lauzer-wars-2.utils.collision :as u]))

;; TODO Use map of collision-type e.g {:player :wall, :player :laser}
;; TODO Handle x and y separately

(defn apply-system [state events cofx]
  (let [collisions (u/collisions (conj (:walls state) (:player state)))]
    {:state  (if (seq collisions)
               (update state :player assoc :x (:old-x (:player state)) :y (:old-y (:player state)))
               state)
     :events (assoc events :collisions collisions)}))
