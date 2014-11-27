; Analyzes verbs to disect their meaning
(ns jazz.core.grammer-jammer
  (:require [jazz.utils :as utils]
            [clojure.string :as s]))

(def verbs ["deploy" "help" "abort"])

(declare disect)
(declare execute)
(declare find-verb)
(declare fuzzy-match-verb)

(defn fuzzy-disect
  [words]
  (let
    [verb (fuzzy-match-verb words)]
    (if verb
      (execute words verb :fuzzy)
      {:cmd-type :unclear
       :valid false})))

(defn exact-disect
  [words]
  (let [verb (first (clojure.set/intersection (set words) (set verbs)))]
    (if verb
      (execute words verb :clear)
      (fuzzy-disect words))))

(defn disect
  [message]
  (let [words (into #{} (s/split message #"\s+"))]
    (or (exact-disect words)
        (fuzzy-disect words))))

; See if it's a simple misspelling
(defn fuzzy-match-verb
  [words]
  (loop [words words]
    (if (empty? words)
      false
      (let [match (first (filter  #(> (utils/string-distance % (first words)) 65) verbs))]
        (if match
          match
          (recur (rest words)))))))

(defn execute
  [words verb cmd-type]
  {:cmd-type cmd-type
   :valid true
   :verb verb})
