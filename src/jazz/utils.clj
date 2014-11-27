(ns jazz.utils
  (:require [clojure.string :as s]))

(declare string-cleaner)
(declare levenshtein)

(defn to-int [s]
  (Integer. (re-find #"\d+" s)))

(defn round
  [number digits]
  (Float. (format (str "%." digits "f") number)))

(defn string-distance
  "Uses Levenshtein distance to get a string distance as a percentage"
  [str-a str-b]
  (let [a (string-cleaner str-a)
        b (string-cleaner str-b)
        distance (levenshtein a b)]
    (round
      (* (- 1 (/ distance (max (count a) (count b)))) 100.0) 2)))

(defn- string-cleaner
  [s]
  (-> s
      clojure.string/lower-case 
      (clojure.string/replace #"(^the |^an |^a |, the$|, an$|, a$|\[|\])" "")
      (clojure.string/replace #"&" "and")
      (clojure.string/replace  #"\s+" "")))

(defn- levenshtein [w1 w2]
  "This method calculates the edit distance as an absolute number, not a % match


  Code from RosettaCode (http://rosettacode.org/wiki/Levenshtein_distance#Clojure)"
  (letfn [(cell-value [ch1 ch2 prev-row acc col-idx]
            (min  (inc (nth prev-row col-idx))
                 (inc (last acc))
                 (+ (nth prev-row (dec col-idx)) (if (= ch1 ch2) 0 1))))]
    (loop [row-idx 1, max-rows (inc (count w2)), prev (range (inc (count w1)))]
      (if (= row-idx max-rows)
        (last prev)
        (let [next-prev (reduce (fn [acc i]
                                  (conj acc (cell-value (nth w1 (dec i)) (nth w2 (dec row-idx)) prev acc i)))
                                [row-idx]
                                (range 1 (count prev)))]
          (recur (inc row-idx) max-rows, next-prev))))))


