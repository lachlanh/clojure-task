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
  (->> (iterate #(quot % 10) x)
       (take-while pos?)
       (mapv #(mod % 10))))

(defn- tailv
  [v n]
  (subvec v (- (count v) n) (count v)))

(defn- headv
  [v n]
  (subvec v 0 n))

(defn- ones
  [d]
  (get (:ones num-names) (get d 0)))

(defn- tens
  [d]
  (str
   ((:tens num-names) (d 1))
   (if (pos? (d 0))
     (str " " (ones (headv d 1))))))

(defn- tens-ones
  [d]
  (let [has-tens (and (= (count d) 2) (pos? (last d)))
        is-teen (and has-tens (= (last d) 1))]
    (if has-tens
      (if is-teen
        ((:teens num-names) (d 0))
        (tens d))
      (ones d))))

(defn- tens-ones-prefix
  [d prefix]
  (let [tens (tens-ones d)
        has-tens (pos? (reduce + d))]
    (if has-tens
      (if prefix
        (str " and " tens)
        tens))))

(defn- hundreds
  [d prefix]
  (let [has-hundreds (and (= (count d) 3) (pos? (last d)))
        pre-space (if prefix " " "")
        hundreds (str pre-space (ones (tailv d 1)) " " "hundred") ;; fix this
        tens-pos (if (= (count d) 3) (pop d) d)]
    (if has-hundreds
      (str hundreds (tens-ones-prefix tens-pos true))
      (tens-ones-prefix tens-pos prefix))))

(defn- thousands
  [d]
  (let [t-pos (subvec d 3 (count d))
        t-str (hundreds t-pos false)
        thousands (if (str/blank? t-str) "" (str t-str " " "thousand"))
        hundreds (hundreds (headv d 3) true)]
    (str thousands hundreds)))

(defn- millions
  [d]
  (let [m-pos (subvec d 6 (count d))
        m-str (hundreds m-pos false)
        millions (if (str/blank? m-str) "" (str m-str " " "million"))
        thousands (thousands (headv d 6))
        t-space (if (str/blank? thousands) "" " ")]
    (str millions t-space thousands)))

(defn num-word
  ""
  [x]
  {:pre [(valid-number? x)]}
  (let [d (digits x)
        count (count d)]
    (cond
      (= x 0) (ones [x])
      (<= 1 count 3) (hundreds d false)
      (<= 4 count 6) (thousands d)
      (<= 6 count 9) (millions d))))
