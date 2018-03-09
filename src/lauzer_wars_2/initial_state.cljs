(ns lauzer-wars-2.initial-state
  (:require [play-cljs.core :as p]
            [lauzer-wars-2.constants :as c]
            [lauzer-wars-2.utils.tiled-map :as tiled-map-utils]))

(defn initial-state [game]
  {:id->entity
   (merge
     {"player" {:component/position   {:x 400 :y 200 :old-x 400 :old-y 200}
                :component/dimensions {:width 15 :height 15}
                :component/collision  {:type :player}
                :component/weapon     {:last-shot-ts 0}
                :id                   "player"}}
     (tiled-map-utils/tiled-map->walls (p/load-tiled-map game c/map-name)))})
