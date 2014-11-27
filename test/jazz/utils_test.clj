(ns jazz.utils-test
  (:use midje.sweet
        jazz.utils))

(facts utils 
  (fact "Given 'deploy' and 'dpeloy' string-distance should return 83%"
        (string-distance "deploy" "deloy") => (roughly 83.33)))
