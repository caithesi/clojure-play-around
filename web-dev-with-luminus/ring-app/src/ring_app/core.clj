(ns ring-app.core
  (:require
   [muuntaja.middleware :as muuntaja]
   [ring.adapter.jetty :as jetty]
   [reitit.ring :as reitit]
   [ring.middleware.reload :refer [wrap-reload]]
   [ring.util.http-response :as response]))

(defn wrap-formats [handler]
  (-> handler
      (muuntaja/wrap-format)))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn resp-content [request-map]
  (str "<html><body> your IP is: "
       (:remote-addr request-map)
       "</body></html>"))


(defn html-handler [request-map]
  (response/ok
   (resp-content request-map)))

(defn json-handler [request]
  (println request)
  (println "get some thing")
  (println (get-in request [:body-params :id]))
  (response/ok
   {:result (get-in request [:body-params :id])}))

(defn api-handler [middleware]
  ["/api" {:middleware middleware}
   ["/multiply"
    {:post
     (fn [{{:keys [a b]} :body-params}]
       (response/ok {:result (* a b)}))}]])

(defn default-handler []
  {:not-found
   (constantly (response/not-found "404 - Page not found"))
   :method-not-allowed
   (constantly (response/method-not-allowed "405 - Not allowed"))
   :not-acceptable
   (constantly (response/not-acceptable "406 - Not acceptable"))})

(def routes
  [["/" {:get html-handler}]
   ["/echo/:id"
    {:get
     (fn [{{:keys [id]} :path-params}]
       (response/ok (str "<p>the value is: " id "</p>")))}]
   ["/api" {:middleware [wrap-formats]}
    ["/multiply"
     {:post
      (fn [{{:keys [a b]} :body-params}]
        (response/ok {:result (* a b)}))}]]])

(def routes-test
  (let [api (api-handler [wrap-formats])]
    [["/" {:get html-handler}]
     ["/echo/:id"
      {:get
       (fn [{{:keys [id]} :path-params}]
         (response/ok (str "<p>the value is: " id "</p>")))}]
     api]))

(def handler
  (reitit/ring-handler
   (reitit/router routes-test)
   (reitit/create-default-handler (default-handler))))



(defn -main [& args]
  (jetty/run-jetty
   (-> #'handler
       wrap-nocache
      ;;  wrap-formats
       wrap-reload)
   {:port 3000
    :join? false}))


