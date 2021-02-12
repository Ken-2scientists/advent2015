(ns advent2015.day22
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn parse-line
  [line]
  (let [[attr qty] (str/split line #": ")]
    [(keyword (str/join "-" (str/split (str/lower-case attr) #" "))) (read-string qty)]))

(defn parse
  [input]
  (into {} (map parse-line input)))

(def day22-input (parse (u/puzzle-input "day22-input.txt")))
(def player {:hit-points 50 :mana 500 :armor 0})
(def spell-cost
  {:magic-missile 53
   :drain         73
   :shield        113
   :poison        173
   :recharge      229})

(defn poison-effect
  [state]
  (update-in state [:boss :hit-points] - 3))

(defn recharge-effect
  [state]
  (update-in state [:player :mana] + 101))

(defn update-effect-timer
  [state effect]
  (let [timer (get-in state [:effects effect])]
    (if (= 1 timer)
      (update  (if (= effect :shield)
                 (update-in state [:player :armor] - 7)
                 state)
               :effects dissoc effect)
      (update-in state [:effects effect] dec))))

(defn apply-effect
  [state effect]
  (-> (case effect
        :poison   (poison-effect state)
        :recharge (recharge-effect state)
        state)
      (update-effect-timer effect)))

(defn apply-effects
  [{:keys [effects] :as state}]
  (reduce apply-effect state (keys effects)))

(defn cast-spell
  [state spell]
  (let [newstate (update-in state [:player :mana] - (spell-cost spell))]
    (case spell
      :magic-missile (update-in newstate [:boss :hit-points] - 4)
      :poison        (assoc-in newstate [:effects :poison] 6)
      :recharge      (assoc-in newstate [:effects :recharge] 5)
      :drain         (-> newstate
                         (update-in [:player :hit-points] + 2)
                         (update-in [:boss   :hit-points] - 2))
      :shield        (-> newstate
                         (update-in [:player :armor] + 7)
                         (assoc-in  [:effects :shield] 6)))))

(defn boss-attack
  [{:keys [player boss] :as state}]
  (let [{:keys [damage]} boss
        {:keys [armor]}  player]
    (update-in state [:player :hit-points] - (max 1 (- damage armor)))))

(defn player-round
  ([state spell]
   (player-round false state spell))
  ([hard? state spell]
   (let [newstate (if hard?
                    (update-in state [:player :hit-points] dec)
                    state)]
     (if (pos? (get-in newstate [:player :hit-points]))
       (-> newstate
           apply-effects
           (cast-spell spell))
       newstate))))

(defn boss-round
  [state]
  (let [newstate (apply-effects state)
        boss-points (get-in newstate [:boss :hit-points])]
    (if (pos? boss-points)
      (boss-attack newstate)
      newstate)))

(defn combat-round
  ([state spell]
   (combat-round false state spell))
  ([hard? state spell]
   (-> (player-round hard? state spell)
       boss-round)))

(reduce combat-round {:player {:hit-points 10 :mana 250 :armor 0}
                      :boss   {:hit-points 13 :damage 8}
                      :effects {}} (take 2 [:poison :magic-missile]))

(reduce combat-round {:player {:hit-points 10 :mana 250 :armor 0}
                      :boss   {:hit-points 14 :damage 8}
                      :effects {}} (take 5 [:recharge :shield :drain :poison :magic-missile]))

(defn player-wins?
  [{:keys [player boss]}]
  (and
   (not (pos? (:hit-points boss)))
   (pos? (:hit-points player))))

(defn available-spells
  [{:keys [player effects]}]
  (let [active-effects (map first (filter #(> 1 (val %)) effects))]
    (->> (u/without-keys spell-cost active-effects)
         (filter #(>= (:mana player) (val %)))
         (map first))))

(available-spells {:player player :effects {}})


(def winning-plays [:poison :recharge :shield :poison :recharge :magic-missile :poison :drain :magic-missile])
(defn day22-part1-soln
  []
  (reduce + (map spell-cost winning-plays)))

; 2252
(def plays [:shield :recharge :poison :shield :drain :recharge :shield :poison :recharge :shield :drain :recharge :shield :poison :magic-missile :magic-missile])
(reduce (partial combat-round true) {:player player :boss day22-input :effects {}} plays)
(reduce + (map spell-cost plays))