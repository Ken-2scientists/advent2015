(ns advent2015.day01
  (:require [advent-utils.core :as u]))

(def day01-input (first (u/puzzle-input "day01-input.txt")))

(defn final-floor
  [input]
  (let [{ups \( downs \) :or {ups 0 downs 0}}
        (frequencies input)]
    (- ups downs)))

(defn first-pos-in-basement
  [input]
  (let [all-steps (map {\( 1 \) -1} input)]
    (->> (reductions + 0 all-steps)
         (map-indexed vector)
         (filter #(= -1 (second %)))
         ffirst)))

(defn day01-part1-soln
  []
  (final-floor day01-input))

(defn day01-part2-soln
  []
  (first-pos-in-basement day01-input))

