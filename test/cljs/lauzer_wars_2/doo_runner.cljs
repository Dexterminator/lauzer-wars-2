(ns lauzer-wars-2.doo-runner
  (:require [cljs.test :refer [run-tests]]
            [doo.runner :refer-macros [doo-tests]]
            [lauzer-wars-2.util.collision-test]))

(doo-tests 'lauzer-wars-2.util.collision-test)

(defn run-all-tests []
  (run-tests 'lauzer-wars-2.util.collision-test))
