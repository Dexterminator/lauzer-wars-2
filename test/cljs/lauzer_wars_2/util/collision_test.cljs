(ns lauzer-wars-2.util.collision-test
  (:require [lauzer-wars-2.test-utils :refer-macros [facts fact is=]]
            [cljs.test :refer [run-tests deftest is]]
            [pjstadig.humane-test-output]))

(facts "math"
  (fact "doesn't work"
    (is= 1 2)))
