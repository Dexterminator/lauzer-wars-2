(ns lauzer-wars-2.cofx
  (:require [play-cljs.core :as p]
            [goog.events.KeyCodes :as keycodes]))

(def keymap
  {keycodes/W :up
   keycodes/A :left
   keycodes/S :down
   keycodes/D :right
   keycodes/F :shoot
   keycodes/J :shoot})

(defn get-input! [game]
  (->> game
       (p/get-pressed-keys)
       (map keymap)
       (remove nil?)
       set))

(defn get-cofx! [game]
  {:dt    (* 0.001 (p/get-delta-time game))
   :input (get-input! game)
   :now   (.getTime (js/Date.))})
