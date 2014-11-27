(ns jazz.core.talker-test
  (:use midje.sweet
        jazz.core.talker))

(def good-message
  "{\"item\":
    {\"message\": 
      {\"message\": \"Jazz: Deploy jazz to qa branch witty-remarks\",
       \"mentions\": [{\"name\": \"Jazz\"}],
       \"from\": \"Johnny Bravo\"}
   }
  }")

(facts talker
  (fact "When receiving a message pertaining to us"
        (process-message {:body good-message}) => {:cmd-type :clear
                                                   :verb "deploy"
                                                   :valid true
                                                   :owner "johnny bravo"})
  (fact "When receiving a message not pertaining to us"
        (process-message {:body "{\"item\": {\"message\": {\"message\": \"What's up yo?\"}}}"}) => nil))
