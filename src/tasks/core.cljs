(ns tasks.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [tasks.tasks :refer [task-lists]]
            [tasks.tasklists :refer [task-list-view]]
            [tasks.controls :refer [controls-view tasks go-next task-list active-task activate-task-by-index]]
            [tasks.timer :refer [start-timer-callback]]
            [tasks.login.login :refer [get-user-or-die]]
            [alandipert.storage-atom :refer [local-storage]]
            ))


(enable-console-print!)


(def first-task-list (:tasks (get task-list 1)))


(def state
  {
    :active               -1
    :running              true
    :active-task-list     1
    :timeUntilTheNextTask 0
  })

(def app-state (local-storage (atom {:task-lists task-lists}) :tasks-atom ))
(swap! app-state assoc :state state)








(defn pad-left [time] (if (< time 10) (str "0" time) time))
(defn get-seconds [seconds] (pad-left (rem seconds 60)))
(defn get-minutes [seconds] (.floor js/Math (/ seconds 60)))
(defn format-time [seconds] (str (get-minutes seconds) ":" (get-seconds seconds)))



(defn do-tick [app]
  (if (:running (:state @app))
    (do
      (om/transact! app [:state :timeUntilTheNextTask] dec)
      (if (< (:timeUntilTheNextTask (:state @app)) 0) (go-next app))
      ))
  )



(defn set-up-timer [app]
  (start-timer-callback #(do-tick app)))






(defn indicator-progress [time left]
  (str (* 100 (- 1 (/ left time))) "%"))

(defn progress-indicator [task app]
  (dom/div #js
               {:className "indicator"}
           (dom/div #js
                        {:className "progress" :style #js {
                                                            :width (indicator-progress (:time task) (:timeUntilTheNextTask (:state app)))}} "")))


(defn task-view [task, i, active, app]
  (dom/li #js {:className (if (= i active) "active")}

          (dom/div #js {:className "task"}
                   (dom/div #js {:className "time"} (format-time (:time task)))
                   (dom/div #js {:className "name"} (:name task))
                   (dom/img #js {
                                  :onClick   #(activate-task-by-index app i)
                                  :className "play" :src "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABPElEQVQ4T62Uy0qDMRBGrbd1H7FIwYUbF4J0p1ClS0HF1/CyclcpdO0jVKxauhB1I3g/H8xAGP/8puoPh5AmOZmZJG3M/fPXCL4R/XU4/u0+UfiJSPRhBbTBTF8Uvprwwyxd2gN4KLVG4TMLJXOhPNewCWcl0ih8SoRKXeNiAYawCld14ii8Z/K7SV04T18sGj3a/VwZonBqQklToSKUcMnaW9oN+HYbovCOSW9JlJ6uy1y4bPIL2haMvAxReMOATtqlGpck4kJfv82cPZUhCscmlPTF0vPFLk37qu2jybYU5V8jPMWhl5VNubSGEyRt6HvtcjX86ZR18Y9gJ4pywrp7eM6iDsx0sateig5qDQa5qNLf696y5IewWyLKpez/NidM0EuoTa9qozTCJhMuoVV1eqVRxpRL12XnfQEAbl0VRaAF8AAAAABJRU5ErkJggg=="})
                   )))




(defn current-task-view [app]

  (let [task (active-task app)]
    (dom/div #js {:className (str "current-task" (if (:comment task) " with-comment"))}
             (dom/h1 nil (:name task))
             (dom/div #js {:className "time"}
                      (dom/span #js {:className "time-left"} (format-time (:timeUntilTheNextTask (:state app)))))
             (progress-indicator task app)
             (dom/div #js {:className "comment"} (:comment task)))

    ))


(defn tasks-view [app]
  (apply dom/ul #js {:className "tasks"}
         (map task-view (:tasks (task-list app)) (range) (repeat (:active (:state app))) (repeat app))))






(defn login-prompt-view [app owner]
  (reify om/IRender
    (render [_]
      (dom/h1 nil "Log in"))))



(defn prepare-state [state ref] state)

(def conf {:target (. js/document (getElementById "app"))})
(let [data 1]



  (defn app-view [app owner]
    (reify
      om/IDidMount
      (did-mount [_] (js/setTimeout #(set-up-timer app) 300))

      om/IRender
      (render [_]
        (dom/div #js {:className "app"}
                 (task-list-view app task-list)
                 (dom/h2 nil "Tasks")
                 (dom/h2 nil (:uid (:user data)))
                 (controls-view app)
                 (dom/div #js {:className "main"}
                          (tasks-view app)
                          (if (> (:active (:state app)) -1) (current-task-view app)))
                 )))))



(om/root app-view app-state conf)
;  (if (:user data) (om/root app-view (prepare-state app-state (:ref data)) conf) (om/root login-prompt-view)))

