(ns jazz.core.talker
  (:require [cheshire.core :as cheshire]
            [clojure.string :as s]
            [jazz.core.grammer-jammer :as jammer]
            [jazz.utils :as utils]))


(declare respond)

(def help "I am sorry creator.  The help file is still on your TODO list.")

(defn process-message
  [request]
  (let [message (->
                  request
                  :body
                  (s/lower-case)
                  (cheshire/parse-string true)
                  :item)]
    (when (some #(= (get % :name) "jazz") ((message :message) :mentions))
      (jammer/discern message))))

(defn respond
  [message]
  (let [meaning (jammer/disect message)]
    (if (meaning :clear)
      (condp = (meaning :verb)
        "execute" (send "Yes creator.  I will let you know how it works out")
        "help" (send help)
        "abort" (if (meaning :valid)
                  (send (str "Aborting \"" (meaning :command) "\"."))
                  (send "I'm not sure which job you're asking me abort.")))

      (cond
        (meaning :fuzzy) (send (str "I think you meant " (meaning :interpretation)
                                    ".  I'm going to hold on for about 45 seconds and then execute."
                                    "  If this isn't what you meant, just tell me to abort"))
        (meaning :unclear) (send "I have no idea what you're talking about.  Ask me for help if you need")))
    meaning))
