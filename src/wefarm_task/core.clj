(ns wefarm-task.core
  (:require [clojure.core :refer :all]))

(def num-names {:ones ["zero" "one" "two" "three" "four" "five" "six" "seven" "eight" "nine"]
                :teens ["ten" "eleven" "twelve" "thirteen" "fourteen" "fifteen" "sixteen" "seventeen" "eighteen" "nineteen"]
                :tens [nil nil "twenty" "thirty" "forty" "fifty" "sixty" "seventy" "eighty" "ninety"]
                })

(defn- valid-number?
  [x]
  (and (integer? x) (>= x 0) (<= x 999999999)))

(defn num-word
  ""
  [input]
  {:pre [(valid-number? input)]}
  (if (= (quot input 10) 0)
    (get (:ones num-names) input)
    (if (= (quot input 10) 1)
      (get (:teens num-names) (rem input 10))
      (if (<= 2 (quot input 10) 9)
        (str (get (:tens num-names) (quot input 10)) (get (:ones num-names) (rem input 10)))))))
