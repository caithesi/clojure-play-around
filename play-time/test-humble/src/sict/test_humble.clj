(ns sict.test-humble
  (:gen-class) 
  (:require
    [sict.simple-ui :as ui]))


(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  
  (greet {:name (first args)})
  (ui/start-app nil))
