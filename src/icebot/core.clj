(ns icebot.core
  (:gen-class)
  (:require [irclj.core :as irc]
            [clojure.stacktrace :as stacktrace]))

(defn doval
  [irc type & s]
  (try
    (if (not (nil? (re-matches #"^icebot: .*" (-> type :text))))
      (let [txt (clojure.string/replace (-> type :text) #"^icebot: " "")
            ans (load-string txt)
            resp (if (seq? ans) (pr-str ans) ans)]
        (irc/reply irc type  resp)))
    (catch Exception e (str "Whelp" (stacktrace/root-cause e)))))

(defn -main
  [& args]
  (def con (irc/connect "irc.example.com" 6667 "replbot" :callbacks {:privmsg doval}))
  (irc/join con "#iamamoron"))
