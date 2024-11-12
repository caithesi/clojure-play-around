(ns ring-app.core
  (:require
   [muuntaja.middleware :as muuntaja]
   [ring.adapter.jetty :as jetty]
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

(defn html-handler [request-map]
  (response/ok
   (resp-content request-map)))

(defn json-handler [request]
  (println request)
  (println "get some thing")
  (println (get-in request [:body-params :id]))
  (response/ok
   {:result (get-in request [:body-params :id])}))

(def handler json-handler)

(defn wrap-formats [handler]
  (-> handler
      (muuntaja/wrap-format)))

(defn -main [& args]
  (jetty/run-jetty
   (-> #'handler
       wrap-nocache
       wrap-formats
       wrap-reload)
   {:port 3000
    :join? false}))


