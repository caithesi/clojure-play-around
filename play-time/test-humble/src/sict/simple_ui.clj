(ns sict.simple-ui)

(require '[io.github.humbleui.ui :as ui])

(def ui
  (ui/default-theme {}
                    (ui/center
                     (ui/label "Hello from Humble UI! 👋"))))
(defn start-app [args]
  (ui/start-app!
   (ui/window
    {:title "Humble 🐝 UI"
     :width 1}
    #'ui)))
  
