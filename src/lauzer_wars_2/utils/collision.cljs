(ns lauzer-wars-2.utils.collision)

(defn collides? [e1 e2]
  (let [e1x1 (:x e1)
        e1x2 (+ e1x1 (:width e1))
        e1y1 (:y e1)
        e1y2 (+ e1y1 (:height e1))

        e2x1 (:x e2)
        e2x2 (+ e2x1 (:width e2))
        e2y1 (:y e2)
        e2y2 (+ e2y1 (:height e2))]
    (not (or
           (< e1x2 e2x1)
           (> e1x1 e2x2)
           (> e1y1 e2y2)
           (> e2y1 e1y2)))))

(defn both-walls? [e1 e2]
  (and
    (= :wall (:type e1))
    (= :wall (:type e2))))

(defn collisions [entities]
  (set (for [e1 entities
             e2 entities
             :when (and (not (both-walls? e1 e2))
                        (not= e1 e2)
                        (collides? e1 e2))]
         #{e1 e2})))
