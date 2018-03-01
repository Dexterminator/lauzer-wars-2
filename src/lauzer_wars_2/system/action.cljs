(ns lauzer-wars-2.system.action)

(def shoot-cooldown 1000)

(defn apply-system [{:keys [player] :as state}
                    events
                    {:keys [input now]}]
  (let [shot-input? (contains? input :shoot)
        can-shoot? (> (- now (:last-shot-ts player)) shoot-cooldown)
        shot? (and shot-input? can-shoot?)]
    {:state  (if shot?
               (assoc-in state [:player :last-shot-ts] now)
               state)
     :events (if shot?
               (assoc events ::shot [])
               events)}))
