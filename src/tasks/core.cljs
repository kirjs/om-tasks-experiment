(ns tasks.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)


(def app-state (atom {
                      :active -1
                      :timeUntilTheNextTask 0
                      :running true
                      :schedule { :repeat true
                                  :tasks [{
                                           :name "Get ready"
                                           :time 180
                                           },{
                                           :name "Silence"
                                           :time 660
                                           },
                                          {
                                           :name "Bin Alum"
                                           :time 150
                                           },
                                          {
                                           :name "Bin Alum 2"
                                           :time 30
                                           },
                                          {
                                           :name "Visualization"
                                           :time 210
                                           },
                                          {
                                           :name "Little Finger Training"
                                           :time 120
                                           },
                                          {
                                           :name "Exercise"
                                           :time 720
                                           },
                                          {
                                           :name "Keyboard practice"
                                           :time 720
                                           },
                                          {
                                           :name "Juggling"
                                           :time 300
                                           },
                                          {
                                           :name "Guitar"
                                           :time 720
                                           },
                                          {
                                           :name "Affirmations"
                                           :time 300
                                           }
                                          ]}}))



(defn play-sound [] (
                     . (js/Audio. "250083__tmenar__ring-hitting-sink.mp3") play  ))


(defn activate-task [app index-task]
  (do
    (play-sound)
    (def index (index-task (:active @app)))
    (om/transact! app :active #(identity index))
    (def timeLeft (:time (get (:tasks (:schedule @app)) index)))
    (om/transact! app :timeUntilTheNextTask #(identity timeLeft))
    ))


(defn go-next [app]
  ( activate-task app
    (if (< (:active @app) (count (:tasks (:schedule @app) )) )
      inc
      #(identity 0))

    ))
(defn go-prev [app] ( activate-task app
                      (if (> (:active @app) 0)
                        dec
                        #(identity 0)) ))




(defn pause-timer [app]
  ( om/transact! app :running #(identity false)))

(defn resume-timer [app]
  ( om/transact! app :running #(identity true)))


(defn do-tick [app]
  (do
    (om/transact! app :timeUntilTheNextTask dec)
    (if (< (:timeUntilTheNextTask @app) 0) (go-next app))
    ))


(defn tick [app]
  (do
    (if (:running @app) (do-tick app))
    (.log js/console (pr-str (:running @app)))
    (js/setTimeout #(tick app) 1000)
    ))


(defn indicator-height [time left]
  (+ 1 (* 74(/ (- time left) time))))

(defn progress-indicator [task app]
  (dom/div #js
           {:className "progress" :style #js {
                                              :height (indicator-height (:time task) (:timeUntilTheNextTask app))}} ""  ))


(defn task-view [task, i, active, app]
  ( dom/li #js { :className (if (= i active) "active")}
    (dom/div #js {:className (str "indicator " (if (< i active) "completed"))}
             (if (= i active ) (progress-indicator task app)))
    (dom/div #js {:className "task"}
             (dom/div #js {:className "time"} (:time task))
             (dom/div #js {:className "name"} (:name task))
             )))


(defn tasks-view [app owner]
  (reify
    om/IDidMount
    (did-mount [_] (js/setTimeout #(tick app) 300))
    om/IRender
    (render [_]
            (dom/div #js {:className "tasks"}
                     (dom/h2 nil "Tasks")
                     (dom/h2 nil (:timeUntilTheNextTask app))
                     (dom/h2 nil (count (:schedule app)))

                     (dom/button #js { :onClick #(resume-timer app) } "Resume")
                     (dom/button #js { :onClick #(pause-timer app) } "Pause")
                     (dom/button #js { :onClick #(go-next app) } "Next")
                     (dom/button #js { :onClick #(go-prev app) } "Back")
                     (apply dom/ul nil
                            (map  task-view (:tasks (:schedule app)) (range) (repeat (:active app)) (repeat app))
                            )))))


(om/root tasks-view
         app-state
         {:target (. js/document (getElementById "app"))})
