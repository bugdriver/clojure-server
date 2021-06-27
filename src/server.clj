(ns server
  (:require [ring.adapter.jetty :as jetty]
            [clojure.data.json :as json]
            [templates :as tpl]))

(def todos (atom '({:id 101 :name "Buy Milk"})))

(defn to-map [json-str]
  (json/read-str json-str :key-fn keyword))

(defn to-json [map-str]
  (json/write-str map-str))

(defn conj-atom [todo]
  (swap! todos conj todo))

(defn get-todos [request]
  {:status 200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body (tpl/show-todos @todos)})

(defn add-todo [request]
  (conj-atom (to-map (:body request)))
  {:status 200
   :headers {"Content-Type" "application/json; charset=UTF-8"}
   :body (to-json @todos)})

(defn not-found [request]
  {:status 404
   :headers {"Content-Type" "application/json; charset=UTF-8"}
   :body (str "Not found " (:uri request))})

(defn route-handler [{:keys [uri request-method]}]
  (case [uri request-method]
    ["/getTodos" :get] get-todos
    ["/addTodos" :post] add-todo
    not-found))

(defn routed-handler [request]
  (let [handler (route-handler request)]
    (handler (update request :body slurp))))


(defn main [& args]
  (jetty/run-jetty routed-handler {:port 3000}))