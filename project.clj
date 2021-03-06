(defproject neptr "0.1.0-SNAPSHOT"
  :description "NEPTR is a bot for interacting with Go Continuous Delivery via Hipchat"
  :url "https://github.com/rhowell/neptr"
  :min-lein-version "2.0.0"
  :dependencies [[cheshire "5.3.1"]
                 [circleci/clj-yaml "0.5.3"]
                 [clj-http "1.0.1"]
                 [clj-time "0.8.0"]
                 [compojure "1.2.0"]
                 [http-kit "2.1.19"]
                 [lein-ring "0.8.13"]
                 [org.clojure/clojure "1.6.0"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-devel "1.1.8"]
                 [ring/ring-json "0.3.1"]]
  :plugins [[cider/cider-nrepl "0.7.0"]
            [lein-midje "3.0.0"]
            [lein-ring "0.8.13"]
            [lein-swank "1.4.5"]]
  :ring {:handler neptr.core.handler/app}
  :profiles {:dev {:dependencies [[com.gfredericks/vcr-clj "0.4.0"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [midje "1.6.3"]
                                  [ring-mock "0.1.5"]]}})

