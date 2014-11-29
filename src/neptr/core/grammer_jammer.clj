; Analyzes verbs to disect their meaning
(ns neptr.core.grammer-jammer
  (:require [neptr.utils :as utils]
            [neptr.core.jobs :as jobs]
            [clj-http.client :as http]
            [clojure.string :as s]))

(def verbs ["deploy" "help" "abort"])

(declare abort)
(declare disect)
(declare dispatch-command)
(declare execute)
(declare find-verb)
(declare execute-deploy)
(declare fuzzy-match-verb)

(defn discern
  [message]
  (let [match-result (assoc (disect ((message :message) :message))
                            :owner ((message :message) :from))]
    (when (match-result :valid)
      (dispatch-command match-result))
    match-result))

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
      {:cmd-type :unclear
       :valid false})))

(defn- disect
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

(defn dispatch-command
  [match-result]
  (condp = (:verb match-result)
    :deploy (execute-deploy match-result)
    :help   {:cmd-type :clear
             :verb "help"}
    :abort  (abort match-result)
    nil))

(defn- get-noun
  [words verb]
  (loop [[word & words] words]
    (if (= word verb)
      (first words)
      (recur words))))

(defn- send-command
  [match]
  (let [url (s/join (map #(str (first %) "=" (second %)) (match :params)) "&")
        response (http/post url)]
    (if (< 400 (:status response))
      (assoc match :response (:body response))
      (merge match {:cmd-type :failed 
                    :reason (:body response)}))))
      
      
(defn execute-deploy
  [{:keys [params] :as match-result}]
  (if (and (params :project) (params :env))
    (send-command match-result)
    {:cmd-type :unclear
     :verb "deploy"
     :valid false}))

(defn abort
  [match-result]
  (let [job (jobs/find match-result)
        match-result (assoc match-result :params [])]
    (if job
      (send-command match-result))))

(defn execute
  [words verb cmd-type]
  {:cmd-type cmd-type
   :valid true
   :verb verb})
