(ns advent2015.day18-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent2015.day18 :as t]))

(deftest day18-part1-soln
  (testing "Reproduces the answer for day18, part1"
    (is (= 1061 (t/day18-part1-soln)))))

(deftest day18-part2-soln
  (testing "Reproduces the answer for day18, part2"
    (is (= 1006 (t/day18-part2-soln)))))