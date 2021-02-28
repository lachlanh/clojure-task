(ns wefarm-task.core
  (:require [clojure.core :refer :all]
            [clojure.string :as str]))

(def num-names {:ones ["zero" "one" "two" "three" "four"
                       "five" "six" "seven" "eight" "nine"]
                :teens ["ten" "eleven" "twelve" "thirteen" "fourteen"
                        "fifteen" "sixteen" "seventeen" "eighteen" "nineteen"]
                :tens [nil nil "twenty" "thirty" "forty"
                       "fifty" "sixty" "seventy" "eighty" "ninety"]
                })

(defn- valid-number?
  [x]
  (and (integer? x) (<= 0 x 999999999)))

(defn- digits
  [x]
  (->> x
       (iterate #(quot % 10))
       (take-while pos?)
       (mapv #(mod % 10))))

(defn- tailv
  [v n]
  (subvec v (- (count v) n) (count v)))

(defn- headv
  [v n]
  (subvec v 0 n))

(defn- ones
  [ds]
  ((:ones num-names) (ds 0)))

(defn- tens
  [ds]
  (str
   ((:tens num-names) (ds 1))
   (if (pos? (ds 0))
     (str " " (ones (headv ds 1))))))

(defn- tens-ones
  [ds]
  (let [has-tens (and (= (count ds) 2) (pos? (last ds)))
        is-teen (and has-tens (= (last ds) 1))]
    (if has-tens
      (if is-teen
        ((:teens num-names) (ds 0))
        (tens ds))
      (ones ds))))

(defn- tens-ones-prefix
  [ds prefix]
  (let [tens (tens-ones ds)
        has-tens (pos? (reduce + ds))]
    (if has-tens
      (if prefix
        (str " and " tens)
        tens))))

(defn- hundreds
  [ds prefix]
  (let [has-hundreds (and (= (count ds) 3) (pos? (last ds)))
        pre-space (if prefix " " "")
        hundreds (str pre-space (ones (tailv ds 1)) " " "hundred") ;; fix this
        tens-pos (if (= (count ds) 3) (pop ds) ds)]
    (if has-hundreds
      (str hundreds (tens-ones-prefix tens-pos true))
      (tens-ones-prefix tens-pos prefix))))

(defn- period
  [ds pre order]
  (let [comp (hundreds ds false)]
    (if-not (str/blank? comp)
      (str pre comp " " order))))

(defn- thousands
  [ds]
  (let [t-pos (subvec ds 3 (count ds))
        thousands (period t-pos "" "thousand")
        hundreds (hundreds (headv ds 3) true)]
    (str thousands hundreds)))

(defn- millions
  [ds]
  (let [m-pos (subvec ds 6 (count ds))
        millions (period m-pos "" "million")
        thousands (period (subvec ds 3 6) " " "thousand")
        hundreds (hundreds (headv ds 3) true)]
    (str millions thousands hundreds)))

(defn num->word
  ""
  [x]
  {:pre [(valid-number? x)]}
  (let [ds (digits x)
        count (count ds)]
    (cond
      (= x 0) (ones [x])
      (<= 1 count 3) (hundreds ds false)
      (<= 4 count 6) (thousands ds)
      (<= 6 count 9) (millions ds))))
