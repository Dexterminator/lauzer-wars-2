(ns lauzer-wars-2.render
  (:require [play-cljs.core :as p]))

(defn render-player [player]
  [:image (merge {:name "images/player1.png" :width 40 :height 40}
                 (select-keys player [:x :y]))])

(defn render-frame! [state game]
  (p/render
    game
    [[:fill {:color "lightgrey"}
      [:rect {:x 0 :y 0 :width 499 :height 299}]]
     (render-player (:player state))]))
