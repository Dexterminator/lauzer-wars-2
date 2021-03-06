(ns lauzer-wars-2.utils.tiled-map)

(def collision-layer 1)

(defn tiled-map->walls [^js/p5.TiledMap tiled-map]
  (let [map-size (.getMapSize tiled-map)
        map-width (.-x map-size)
        map-height (.-y map-size)
        tile-size (.getTileSize tiled-map)
        tile-width (.-x tile-size)
        tile-height (.-y tile-size)]
    (into {}
          (for [x (range map-width)
                y (range map-height)
                :when (pos? (.getTileIndex tiled-map collision-layer x y))]
            [(str "wall-" x y) {:component/position   {:x (* tile-width x) :y (* tile-height y)}
                                :component/dimensions {:width tile-width :height tile-height}
                                :component/collision  {:type :wall}}]))))
