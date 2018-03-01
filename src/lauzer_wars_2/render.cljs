(ns lauzer-wars-2.render
  (:require [play-cljs.core :as p]))

(defn render-frame! [state game]
  (p/render
    game
    [[:fill {:color "lightgrey"}
      [:rect {:x 0 :y 0 :width 499 :height 299}]]
     [:fill {:color "black"}
      [:text {:value (str "Pressed: " (:pressed-keys state)) :x (:text-x state) :y (:text-y state) :size 20 :font "Helvetica"}]]]))
