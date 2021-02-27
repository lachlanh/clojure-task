(ns wefarm-task.core
  (:require [clojure.core :refer :all]
            [clojure.string :as str]))

(def num-names {:ones ["zero" "one" "two" "three" "four" "five" "six" "seven" "eight" "nine"]
                :teens ["ten" "eleven" "twelve" "thirteen" "fourteen" "fifteen" "sixteen" "seventeen" "eighteen" "nineteen"]
                :tens [nil nil "twenty" "thirty" "forty" "fifty" "sixty" "seventy" "eighty" "ninety"]
                })

(defn- valid-number?
  [x]
  (and (integer? x) (>= x 0) (<= x 999999999)))

(defn- digits
  [x]
  ;; special case of zero
  (if (= x 0)
    [x]
    (->> (iterate #(quot % 10) x)
         (take-while pos?)
         (mapv #(mod % 10)))))

(defn- tailv
  [v n]
  (subvec v (- (count v) n) (count v)))

(defn- headv
  [v n]
  (subvec v 0 n))

(defn- ones
  [d]
  (get (:ones num-names) (get d 0)))

(defn- tens-ones
  [d]
  (let [has-tens (and (= (count d) 2) (> (last d) 0))
        is-teen (and has-tens (= (last d) 1))]
    (if (not has-tens)
      (ones d)
      (if is-teen
        (get (:teens num-names) (get d 0))
        (str
         (get (:tens num-names) (get d 1))
         (if (> (get d 0) 0 )
           (str " " (ones (headv d 1)))))))))

(defn- tens-ones-prefix
  [d prefix]
  (let [tens (tens-ones d)]
    (if prefix
      (str " and " tens)
      tens)))

(defn- hundreds
  [d prefix]
  (let [has-hundreds (and (= (count d) 3) (> (last d) 0))
        pre-space (if prefix " " "")
        hundreds (str pre-space (ones (tailv d 1)) " " "hundred") ;; fix this
        tens-pos (if (= (count d) 3) (pop d) d)
        sum-of-tens (reduce + tens-pos)]
    (if has-hundreds
      (if (> sum-of-tens 0)
        (str hundreds (tens-ones-prefix (pop d) true))
        hundreds)
      (if (> sum-of-tens 0)
        (tens-ones-prefix tens-pos prefix)))))

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
  [input]
  {:pre [(valid-number? input)]}
  (let [d (digits input)
        count (count d)]
    (case count
      1 (ones d) ;; TODO LH need to figure this out better zero case handling in hundreds ?
      2 (hundreds d false)
      3 (hundreds d false)
      4 (thousands d)
      5 (thousands d)
      6 (thousands d)
      7 (millions d)
      8 (millions d)
      9 (millions d)
      )))
