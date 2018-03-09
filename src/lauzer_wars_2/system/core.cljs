(ns lauzer-wars-2.system.core
  (:require [lauzer-wars-2.system.action :as action]
            [lauzer-wars-2.system.collision :as collision]))

(def systems
  [action/system collision/system])

(defn contains-all? [m keys]
  (every? #(contains? m %) keys))

(defn apply-system [id->entity events cofx {:keys [system-fn components subscriptions]}]
  (let [needed-components components
        applicable-entities (filter #(contains-all? % needed-components) (vals id->entity))
        subscribed-events (select-keys events subscriptions)
        {updated-entities :entities
         new-events       :events} (system-fn applicable-entities subscribed-events cofx)]
    {:id->entity (reduce (fn [id->entity {:keys [id] :as entity}]
                           (assoc id->entity id entity))
                         id->entity
                         updated-entities)
     :events     (merge events new-events)}))

(defn apply-systems
  "Applies every game system to the game's entities in order.
  Each system has the signature [entities events cofx], where entities is a seq of entities that have the set
  of component that the system works on, events is a map of \"atomic\" events that have occurred in the current frame,
  i.e :player/died. Each system returns a map with the keys :state and :events, containing the updated state and events."
  [cofx id->entity]
  (reduce (fn [{:keys [id->entity events]} system]
            (apply-system id->entity events cofx system))
          {:id->entity id->entity
           :events     {}}
          systems))
