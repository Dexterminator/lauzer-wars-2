(ns lauzer-wars-2.render
  (:require [play-cljs.core :as p]
            [lauzer-wars-2.constants :as constants]))

(defn render-player [player]
  [:image (merge {:name "images/player1.png"}
                 (select-keys player [:x :y :width :height]))])

(defn render-frame! [state game]
  (p/render
    game
    [[:tiled-map {:name constants/map-name :x 0}]
     [:fill {:color "red"}
      [:ellipse (merge {:width 5 :height 4} (select-keys (:player state) [:x :y]))]]
     (render-player (:player state))]))
