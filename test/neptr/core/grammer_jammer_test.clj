(ns neptr.core.grammer-jammer-test
  (:use midje.sweet
        neptr.core.grammer-jammer)
  (:require [neptr.core.talker :as talker]
            [cheshire.core :as cheshire]))


(def good-message
  (cheshire/parse-string
    "{\"message\": 
       {\"message\": \"neptr: deploy neptr to qa branch witty-remarks\",
        \"mentions\": [{\"name\": \"neptr\"}],
        \"from\": \"Johnny Bravo\"}
     }" true))

(def non-sense
  (cheshire/parse-string
    "{\"message\": 
       {\"message\": \"neptr: Punch a duck\",
        \"mentions\": [{\"name\": \"neptr\"}],
        \"from\": \"Johnny Bravo\"}
     }" true))


(facts grammer
       (fact "discern will return true if given a deploy verb"
             (discern good-message) => {:cmd-type :clear
                                        :owner "Johnny Bravo"
                                        :verb "deploy"
                                        :valid true})
       (fact "discern will return true if given a help verb"
             (discern 
               (assoc-in good-message [:message :message] "help")) 
             => {:cmd-type :clear
                 :owner "Johnny Bravo"
                 :verb "help"
                 :valid true})
       (fact "discern will return true if given a abort verb"
             (discern (assoc-in good-message [:message :message] "abort")) 
             => {:cmd-type :clear
                 :owner "Johnny Bravo"
                 :verb "abort"
                 :valid true})
       (fact "Given a message, discern will return false if it can't parse the request"
             (discern non-sense) => {:cmd-type :unclear
                                     :owner "Johnny Bravo"
                                     :valid false})
       (fact "Given a misspelled verb, it will match if it's close enough"
             (fuzzy-match-verb ["dpeloy"]) => "deploy"))

