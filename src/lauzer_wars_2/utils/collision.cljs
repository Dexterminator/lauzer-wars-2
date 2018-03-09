(ns lauzer-wars-2.utils.collision)

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

(defn collisions [entities]
  (set (for [e1 entities
             e2 entities
             :when (and (not (both-walls? e1 e2))
                        (not= e1 e2)
                        (collides? e1 e2))]
         #{e1 e2})))
