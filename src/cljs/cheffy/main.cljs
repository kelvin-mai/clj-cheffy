(ns cheffy.main)

(println "Hello, world! - this is from the module static code - Open your Console in DevTools")

(defn init []
  (println "This is from the init function")
  (.appendChild (.-body js/document) (.createTextNode js/document "It works!")))
