(ns lauzer-wars-2.system.collision)

(defn collides? [e1 e2]
  (let [e1x1 (get-in e1 [:component/position :x])
        e1x2 (+ e1x1 (get-in e1 [:component/dimensions :width]))
        e1y1 (get-in e1 [:component/position :y])
        e1y2 (+ e1y1 (get-in e1 [:component/dimensions :height]))

        e2x1 (get-in e2 [:component/position :x])
        e2x2 (+ e2x1 (get-in e2 [:component/dimensions :width]))
        e2y1 (get-in e2 [:component/position :y])
        e2y2 (+ e2y1 (get-in e2 [:component/dimensions :height]))]
    (not (or
           (< e1x2 e2x1)
           (> e1x1 e2x2)
           (> e1y1 e2y2)
           (> e2y1 e1y2)))))

(defn both-walls? [e1 e2]
  (and
    (= :wall (get-in e1 [:component/collision :type]))
    (= :wall (get-in e2 [:component/collision :type]))))

(defn resolve-wall-player-collision [wall player]
  (let [{:keys [old-x old-y] :as player-position} (:component/position player)
        x-change-only (assoc player-position :y old-y)
        y-change-only (assoc player-position :x old-x)
        no-change (assoc player-position :x old-x :y old-y)
        x-change-only-player (assoc player :component/position x-change-only)
        y-change-only-player (assoc player :component/position y-change-only)]
    (when (collides? wall player)
      (if-not (collides? wall x-change-only-player)
        x-change-only-player
        (if-not (collides? wall y-change-only-player)
          y-change-only-player
          (assoc player :component/position no-change))))))

(defn resolve-collision [e1 e2]
  (condp = [(get-in e1 [:component/collision :type]) (get-in e2 [:component/collision :type])]
    [:wall :player] (resolve-wall-player-collision e1 e2)
    [:player :wall] (resolve-wall-player-collision e2 e1)))

(defn resolve-collisions [entities]
  (remove nil? (for [e1 entities
                     e2 entities
                     :when (and (not (both-walls? e1 e2))
                                (not= e1 e2)
                                (collides? e1 e2))]
                 (resolve-collision e1 e2))))

(defn apply-system [entities events cofx]
  {:entities (resolve-collisions entities)})

(def system {:system-fn     apply-system
             :components    [:component/position :component/dimensions :component/collision]
             :subscriptions []})
