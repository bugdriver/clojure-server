(ns templates
  (:require [hiccup.core :as h]))

(defn show-todos [todos]
  (h/html [:html
           [:body
            [:table {:style {:width "100%" :text-align "left" :font-size "20px"}}
             [:tr
              [:th "ID"]
              [:th "NAME"]]
             (for [todo todos]
               [:tr
                [:td (:id todo)]
                [:td (:name todo)]])]]]))
