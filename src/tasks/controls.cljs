(ns tasks.controls (:require
                    [om.core :as om :include-macros true]
                    [om.dom :as dom :include-macros true]
                    ))




(defn play-sound [] (. (js/Audio. "sounds/sink.mp3") play  ))





(defn task-list [app]
  ( get (:task-lists app) (:active-task-list (:state app))))


(defn tasks [app] (:tasks (task-list app)))

(defn task-by-index [app index]
  (get (tasks app) index))

(defn active-task [app]
  (task-by-index app (:active (:state app))))





(defn amount-of-tasks [app] (count (tasks  app)))

(defn reset-time [app]
  (let [timeLeft (:time (active-task @app))]
    (om/transact! app [:state :timeUntilTheNextTask] #(identity timeLeft))
    ))


(defn activate-task [app index-task]
  (let
    [index (index-task (:active (:state @app)))]

    (play-sound)
    (om/transact! app [:state :active] #(identity index))
    (reset-time app)
    ))

(defn activate-task-by-index [app index] (activate-task app #(identity index)))

(defn go-next [app]
  (activate-task app #(mod (inc %) (amount-of-tasks @app))))

(defn go-prev [app]
  (activate-task app #(mod (dec %) (amount-of-tasks @app))))

(defn reset-task [app]
  (reset-time app))




(defn pause-resume-timer [app]
  ( om/transact! app  [:state :running] not))



(defn controls-view [app]
  (dom/div #js {:className "controls"}
           (dom/button #js { :onClick #(pause-resume-timer app) } (if (:running (:state app)) "Pause" "Resume") )
           (dom/button #js { :onClick #(go-next app) } "Next")

           (dom/button #js { :onClick #(go-prev app) } "Back")
           (dom/button #js { :onClick #(reset-task app) } "Reset")
           )

  )
