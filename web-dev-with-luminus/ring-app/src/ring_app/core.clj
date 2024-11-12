(ns ring-app.core
  (:require [ring.adapter.jetty :as jetty]
            [clojure.data.json :as json]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.util.http-response :as response]))


(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn resp-content [request-map]
  (str "<html><body> your IP is: "
       (:remote-addr request-map)
       "</body></html>"))

(defn test-json []
  (json/write-str
   {:name "yiyang"}))

(defn handler [request-map]
  (response/ok
   (test-json)))


(defn -main [& args]
  (jetty/run-jetty
   (-> #'handler
       wrap-nocache
       wrap-reload)
   {:port 3000
    :join? false}))


