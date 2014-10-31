(ns tasks.timer
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require   [cljs.core.async :refer [put! chan <!]]))


(enable-console-print!)


(defn get-seconds [seconds] (.floor js/Math (/ seconds 1000)))

(defn get-current-time [] (get-seconds (.now js/Date)))


(defn start-timer-channel
  ([interval] (start-timer-channel interval 500))
  ([interval tick-interval]
   (let [initial-time (- (get-current-time) 2)
         last-tick-time (atom initial-time)
         timer-chan (chan)
         tick (fn []
                (let [
                      now (get-current-time)
                      diff (- now @last-tick-time)
                      ]
                  (if (> diff 0)
                    (do

                      (doseq [x (range diff)] (put! timer-chan 1))
                      (swap! last-tick-time #(identity now))
                      )
                    )))
         ]
     (js/setInterval tick tick-interval)
     timer-chan
     )))

(defn start-timer-callback [callback]
  (let [timer (start-timer-channel 1000)]
    (go (loop []
          (let [tick (<! timer)]
            (callback)
            (recur)
            )))))
