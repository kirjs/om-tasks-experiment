(ns tasks.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)


(def app-state (atom {
                      :active 0
                      :timeUntilTheNextTask 0
                      :running true
                      :schedule { :repeat true
                                  :tasks [{
                                           :name "Task1"
                                           :time 15
                                           },
                                          {
                                           :name "Task2"
                                           :time 3
                                           },
                                          {
                                           :name "Task3"
                                           :time 25
                                           },
                                          {
                                           :name "Task4"
                                           :time 5
                                           },
                                          {
                                           :name "Task5"
                                           :time 3
                                           }
                                          ]}}))




(defn go-next [app]
  ( om/transact! app :active (fn [s] (inc s))))

(defn go-prev [app]
  ( om/transact! app :active (fn [s] (dec s))))


(defn tick [app]

    (debugger)
    (if (:running app) (do
      (go-next app)
      (js/setTimeout #(tick app) 300)
     ))

  )


(defn start-timer [app]
  (tick app)
  )


(defn task-view [task, i, active]
  ( dom/li #js { :className (if (= i active) "active")}
    (dom/div #js {:className "task"} (:name task))
    (dom/div #js {:className "time"} (:time task))
  )
)


(defn tasks-view [app owner]
  (reify
    om/IRender
    (render [_]
            (dom/div #js {:className "tasks"}
                     (dom/h2 nil "Tasks")
                     (dom/button #js { :onClick #(start-timer app) } "Start")
                     (dom/button #js { :onClick #(go-next app) } "Next")
                     (dom/button #js { :onClick #(go-prev app) } "Back")
                     (apply dom/ul nil
                            (map  task-view (:tasks (:schedule app)) (range) (repeat (:active app)))
                            )))))


(om/root tasks-view
         app-state
         {:target (. js/document (getElementById "app"))})
