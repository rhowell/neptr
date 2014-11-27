(ns neptr.core.handler
  (:require [neptr.core.talker :as talker]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/notify" request (talker/process-message request))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
