(ns wefarm-task.core
  (:require [clojure.core :refer :all]))

(def num-names {:ones ["zero" "one" "two" "three" "four" "five" "six" "seven" "eight" "nine"]})

(defn- valid-number?
  [x]
  (and (integer? x) (>= x 0) (<= x 999999999)))

(defn num-word
  ""
  [input]
  {:pre [(valid-number? input)]}
  (if (= (quot input 10) 0)
    (get (:ones num-names) input)
    nil))
