(ns lauzer-wars-2.render
  (:require [play-cljs.core :as p]
            [lauzer-wars-2.constants :as constants]))

(defn render-player [player]
  [:image (merge {:name "images/player1.png"}
                 (:component/position player)
                 (:component/dimensions player))])

(defn render-frame! [state game]
  (p/render
    game
    [[:tiled-map {:name constants/map-name :x 0}]
     (render-player (get-in state [:id->entity "player"]))]))
