(ns lauzer-wars-2.doo-runner
  (:require [cljs.test :refer [run-tests]]
            [doo.runner :refer-macros [doo-tests]]
            [lauzer-wars-2.util.collision-test]
            [lauzer-wars-2.system.core-test]))

(doo-tests 'lauzer-wars-2.util.collision-test 'lauzer-wars-2.system.core-test)
