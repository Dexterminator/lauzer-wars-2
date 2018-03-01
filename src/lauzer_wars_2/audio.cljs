(ns lauzer-wars-2.audio
  (:require [cljsjs.howler]))

(def pew (js/Howl. #js {:src "audio/pew.ogg"}))

(defn play-pew! []
  (.play pew))
