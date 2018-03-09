(ns lauzer-wars-2.system.core-test
  (:require [lauzer-wars-2.test-utils :refer-macros [facts fact is=]]
            [cljs.test :refer [run-tests deftest is]]
            [lauzer-wars-2.system.core :as system]
            [pjstadig.humane-test-output]))

(def test-system {:system-fn     (fn [entities events cofx]
                                   {:entities (map #(-> % (update :x inc) (assoc :system-applied true)) entities)
                                    :events   {:bar [(first (:foo events)) "bar-param"]}})
                  :components    [:x]
                  :subscriptions [:foo]})

(facts "apply-system"
  (fact "returns stuff"
    (let [id->entity {100 {:x 10 :id 100}
                    200 {:y 10 :id 200}
                    300 {:x 20 :id 300}}
          events {:foo ["foo-param"]}
          cofx {}]
      (is= {:id->entity {100 {:x 11 :system-applied true :id 100}
                         200 {:y 10 :id 200}
                         300 {:x 21 :system-applied true :id 300}}
            :events     {:foo ["foo-param"]
                         :bar ["foo-param" "bar-param"]}}
           (system/apply-system id->entity events cofx test-system)))))
