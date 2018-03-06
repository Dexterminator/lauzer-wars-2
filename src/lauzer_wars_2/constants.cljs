(ns lauzer-wars-2.constants
  (:require [play-cljs.core :as p]
            [lauzer-wars-2.utils.tiled-map :as tiled-map-utils]))

(def width 640)
(def height 480)
(def map-name "map1")

(defn initial-state [game]
  {:player {:x 400 :y 200 :width 15 :height 15 :type :player}
   :walls    (tiled-map-utils/tiled-map->walls (p/load-tiled-map game map-name))})
