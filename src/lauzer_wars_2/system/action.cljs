(ns lauzer-wars-2.system.action)

(def shoot-cooldown 1000)
(def speed 140)

(defn direction->movement [dt]
  (let [movement (* speed dt)]
    {:up    #(update % :y - movement)
     :down  #(update % :y + movement)
     :left  #(update % :x - movement)
     :right #(update % :x + movement)}))

(defn update-position [position input dt]
  (let [position (assoc position :old-x (:x position) :old-y (:y position))]
    (reduce (fn [player [key action]]
              (cond-> player (contains? input key) action))
            position
            (direction->movement dt))))

(defn update-player [player shot? now input dt]
  (cond-> (update player :component/position update-position input dt)
          shot? (assoc-in [:component/weapon :last-shot-ts] now)))

(defn apply-system [entities
                    events
                    {:keys [input now dt]}]
  (let [player (first entities)
        shot-input? (contains? input :shoot)
        can-shoot? (> (- now (get-in player [:component/weapon :last-shot-ts])) shoot-cooldown)
        shot? (and shot-input? can-shoot?)]
    {:entities [(update-player player shot? now input dt)]
     :events   (when shot? {::shot []})}))

(def system {:system-fn     apply-system
             :components    [:component/weapon :component/position]
             :subscriptions []})
