(ns advent2015.day24
  (:require [clojure.math.combinatorics :as combo]
            [advent-utils.core :as u]))

(def day24-input (map read-string (u/puzzle-input "day24-input.txt")))

(def day24-sample (concat (range 1 6) (range 7 12)))

(defn partition-weight
  [input]
  (/ (reduce + input) 3))

(defn valid-partition?
  [weight parts]
  (every? #(= weight %) (map (partial reduce +) parts)))

(defn valid-partitions
  [input]
  (let [weight (partition-weight input)]
    (filter (partial valid-partition? weight) (combo/partitions input :min 3 :max 3))))

(defn best-partitions
  [input]
  (let [partitions (valid-partitions input)
        min-size (apply min (map #(apply min (map count %)) partitions))]
    (filter #(= min-size (apply min (map count %))) partitions)))

(defn quantum-entanglement
  [partition]
  (let [group1 (apply min-key count partition)]
    (reduce * group1)))

(defn ideal-quantum-entaglement
  [input]
  (let [partitions (best-partitions input)]
    (apply min (map quantum-entanglement partitions))))

(defn day24-part1-soln
  []
  (ideal-quantum-entaglement day24-input))
