(ns tasks.tasks)


(def task-lists [
                  {
                    :id    "pomodoro"
                    :name  "Pomodoro",
                    :tasks [{
                              :name "Work"
                              :time (* 25 60)
                            },
                            {
                              :name "Rest"
                              :time (* 5 60)
                            }
                            ]
                  },
		 {
                    :id    "tasks"
                    :name  "tasks",
                    :tasks [{
                              :name "Work"
                              :time (* 25 60)
                            },
                            {
                              :name "Rest"
                              :time (* 5 60)
                            }
                            ]
                  }
                  ]

  )

