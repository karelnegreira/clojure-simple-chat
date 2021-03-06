(ns clojure-simple-chat.core
    (:require [reagent.core :as reagent :refer [atom]]
              [wscljs.client :as ws]
              [wscljs.format :as fmt]))

(enable-console-print!)

(def messages (atom []))

; WEBSOCKET CODE AFTER This
(defn handle-onOpen []
    (js/alert "Connection Opened"))
(defn handle-onMessage [e]
    (swap! messages conj (.-data e)))
(defn handle-onClose [])

(def handlers {:on-message (fn [e] (handle-onMessage e))
               :on-open    #(handle-onOpen)
               :on-close   #(handle-onClose)})

(defn open-ws-connection []
    (def socket (ws/create "ws://localhost:8080" handlers)))

(defn send-message []
    (ws/send socket (.-value (.getElementById js/document "input")) fmt/json))

(defn close-connection []
    (ws/close socket)
    (js/alert "Connection Closed"))

(defonce app-state (atom {:text "Hello world!"}))

(defn hello-world []

    [:div
     [:div#messages
      (for [message @messages]
          [:p message])]
     [:div  {:style  {:display "flex" :flex-direction "column" :bottom 0 :position "absolute" :width "75%" }}

      [:input#input {:type "text" :placeholder "Enter Your Message"}]
      [:button {:type "submit" :class "button-primary" :onClick #(send-message)} "Send"]
      ]
     ])

(open-ws-connection)
(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))