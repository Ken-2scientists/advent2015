(ns advent2015.day06
  (:require [clojure.set :as set]
            [advent-utils.core :as u]))

(def pattern #"(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)")
(def command {"turn on" :on
              "turn off" :off
              "toggle" :toggle})

(defn parse
  [line]
  (let [[a b c d e] (rest (first (re-seq pattern line)))]
    {:cmd (command a)
     :start [(read-string b) (read-string c)]
     :end   [(read-string d) (read-string e)]}))

(def day06-input (map parse (u/puzzle-input "day06-input.txt")))

(defn rect-range
  [[sx sy] [ex ey]]
  (for [y (range sy (inc ey))
        x (range sx (inc ex))] [x y]))

(defn toggle
  [grid locs]
  (let [current-ons (set/intersection locs grid)
        current-offs (set/difference locs grid)]
    (->> (set/difference grid current-ons)
         (set/union current-offs))))

(defn update-grid
  [grid {:keys [cmd start end]}]
  (let [locs (set (rect-range start end))]
    (case cmd
      :on (set/union grid locs)
      :off (set/difference grid locs)
      :toggle (toggle grid locs))))

(defn day06-part1-soln
  []
  (count (reduce update-grid #{} day06-input)))