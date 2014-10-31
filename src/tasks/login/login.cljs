(ns tasks.login.login
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require   [cljs.core.async :refer [put! chan <!]]))

(defn firebase-url [url] (str "https://tasktimer.firebaseio.com" url))

(defn init-firebase [] (js/Firebase. (firebase-url "")))


(defn on-error [] (print error));
(defn get-user-logged-in [ref]
  (.authWithOAuthRedirect  ref "twitter" on-error))




(defn good []
  (print "good"))


(defn get-user-or-die []
     (let [
           ref (init-firebase)
           login-chan (chan)
           authData (.getAuth ref)]
           (if authData
             {:user authData
              :ref ref}
             (get-user-logged-in ref))))



