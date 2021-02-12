(ns advent2015.day20
  (:require [clojure.math.combinatorics :as combo]))

(def day20-input 33100000)

; From https://codereview.stackexchange.com/questions/114619/number-factorization-in-clojure
(defn lazy-primes
  ([] (cons 2 (lazy-primes 3 [])))
  ([current known-primes]
   (if (not-any? #(zero? (mod current %))
                 (take-while #(<= (*' % %) current) known-primes))
     (lazy-seq (cons current
                     (lazy-primes
                      (+' current 2)
                      (conj known-primes current))))
     (recur (+' current 2) known-primes))))

; From https://codereview.stackexchange.com/questions/114619/number-factorization-in-clojure
(defn factorize [num]
  (loop [num num, acc [1], primes (lazy-primes)]
    (if (= num 1)
      acc
      (let [factor (first primes)]
        (if (= 0 (mod num factor))
          (recur (quot num factor) (conj acc factor) primes)
          (recur num acc (rest primes)))))))

(defn house-presents
  [house]
  (let [factors (->> (factorize house)
                     combo/subsets
                     (map (partial reduce *))
                     distinct)]
    (* 10 (reduce + factors))))

(defn first-house-with-n-presents
  [present-logic start input]
  (first (keep-indexed #(when (>= %2 input) %1) (pmap present-logic (range start)))))

(defn house-presents-part2
  [house]
  (let [min     (/ house 50)
        factors (->> (factorize house)
                     combo/subsets
                     (map (partial reduce *))
                     distinct
                     (filter #(>= % min)))]
    (* 11 (reduce + factors))))

(defn day20-part1-soln
  []
  (first-house-with-n-presents house-presents (/ day20-input 5) day20-input))

(defn day20-part2-soln
  []
  (first-house-with-n-presents house-presents-part2 (/ day20-input 5) day20-input))