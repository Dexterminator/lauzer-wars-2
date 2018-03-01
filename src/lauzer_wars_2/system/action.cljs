(ns lauzer-wars-2.system.action)

(def shoot-cooldown 1000)
(def speed 200)

(defn direction->movement [dt]
  (let [movement (* speed dt)]
    {:up    #(update % :y - movement)
     :down  #(update % :y + movement)
     :left  #(update % :x - movement)
     :right #(update % :x + movement)}))

(defn update-events [events shot?]
  (if shot?
    (assoc events ::shot [])
    events))

(defn move-player [player input dt]
  (reduce (fn [player [key action]]
            (cond-> player (contains? input key) action))
          player
          (direction->movement dt)))

(defn update-player [player shot? now input dt]
  (cond-> (move-player player input dt)
          shot? (assoc :last-shot-ts now)))

(defn apply-system [{:keys [player] :as state}
                    events
                    {:keys [input now dt]}]
  (let [shot-input? (contains? input :shoot)
        can-shoot? (> (- now (:last-shot-ts player)) shoot-cooldown)
        shot? (and shot-input? can-shoot?)]
    {:state  (update state :player update-player shot? now input dt)
     :events (update-events events shot?)}))
