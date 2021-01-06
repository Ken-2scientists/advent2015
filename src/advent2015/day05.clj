(ns advent2015.day05
  (:require [advent-utils.core :as u]))

(def day05-input (u/puzzle-input "day05-input.txt"))

(defn three-vowels?
  [s]
  (>= (count (re-seq #"[aeiou]" s)) 3))

(defn repeated-char?
  [s]
  (some? (re-find #"(\w)\1" s)))

(defn no-invalid-pairs?
  [s]
  (nil? (re-find #"ab|cd|pq|xy" s)))

(def nice?
  (every-pred
   three-vowels?
   repeated-char?
   no-invalid-pairs?))

(defn day05-part1-soln
  []
  (count (filter nice? day05-input)))