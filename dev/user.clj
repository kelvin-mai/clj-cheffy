(ns user
  (:require [cheffy.core]
            [integrant.core :as ig]
            [integrant.repl :as ig-repl :refer [go
                                                halt
                                                reset
                                                reset-all]]))

(ig-repl/set-prep!
  (fn [] (-> "resources/config.edn"
             slurp
             ig/read-string)))

(comment
  (go)
  (halt)
  (reset)
  (reset-all))
