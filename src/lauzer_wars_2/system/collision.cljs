(ns lauzer-wars-2.system.collision
  (:require [lauzer-wars-2.utils.collision :as u]))

(defn revert-position [{:keys [old-x old-y] :as position}]
  (assoc position :x old-x
                  :y old-y))

(defn resolve-collisions [collisions]
  (for [collision collisions]
    (let [[e1 e2] (sort-by #(get-in % [:component/collision :type]) (seq collision))]
      (when (and
              (= (get-in e1 [:component/collision :type]) :player)
              (= (get-in e2 [:component/collision :type]) :wall))
        (update e1 :component/position revert-position)))))

(defn apply-system [entities events cofx]
  (let [collisions (u/collisions entities)]
    {:entities (resolve-collisions collisions)
     :events   {:collisions (resolve-collisions collisions)}}))

(def system {:system-fn     apply-system
             :components    [:component/position :component/dimensions :component/collision]
             :subscriptions []})
