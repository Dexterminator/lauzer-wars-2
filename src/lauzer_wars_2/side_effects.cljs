(ns lauzer-wars-2.side-effects
  (:require [lauzer-wars-2.audio :as audio]
            [lauzer-wars-2.system.action :as action]))

(defn do-side-effects! [events]
  (when (contains? events ::action/shot)
    (audio/play-pew!)))
