(ns neptr.core.grammer-jammer-test
  (:use midje.sweet
        neptr.core.grammer-jammer))

(facts grammer
  (fact "Disect will return true if given a deploy verb"
                (disect "deploy all kinds of junk") => {:cmd-type :clear
                                                        :verb "deploy"
                                                        :valid true})
  (fact "Disect will return true if given a help verb"
                (disect "help") => {:cmd-type :clear
                                    :verb "help"
                                    :valid true})
  (fact "Disect will return true if given a abort verb"
                (disect "abort") => {:cmd-type :clear
                                    :verb "abort"
                                    :valid true})
  (fact "Given a message, disect will return false if it can't parse the request"
                (disect "Floop the pig") => {:cmd-type :unclear
                                             :valid false})
  (fact "Given a misspelled verb, it will match if it's close enough"
                (fuzzy-match-verb ["dpeloy"]) => "deploy"))

