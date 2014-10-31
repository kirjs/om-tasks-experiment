(ns tasks.tasklists
  (:require
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]
    ))


(defn next-task-list [app]
  (om/transact! app [:state :active-task-list] #(identity 1)))
(defn prev-task-list [app]
  (om/transact! app [:state :active-task-list] #(identity 0)))


(defn task-list-view [app task-list]
  (let [task (get task-list (:active-task-list app))]
    (dom/div #js {:className "task-list"}
             (dom/button #js {:onClick #(prev-task-list app)} "prev")
             (dom/span nil (:name task))
             (dom/span nil (:active-task-list app))
             (dom/button #js {:onClick #(next-task-list app)} "next"))
    ))
