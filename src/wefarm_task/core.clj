(ns wefarm-task.core
  (:require [clojure.core :refer :all]))

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

(defn- ones
  [d]
  (get (:ones num-names) (get d 0)))

(defn- tens-ones
  [d]
  ;; TODO LH could refactor this to case ? 
  (if (= (get d 1) 0)
    (ones d)
    (if (= (get d 1) 1)
      (get (:teens num-names) (get d 0))
      (if (> (get d 1) 1)
        (str
         (get (:tens num-names) (get d 1))
         (if (> (get d 0) 0 ) (str " " (ones (vector (first d)))))
         )))))

(defn- hundreds
  [d]
  (let [hundreds (str (get (:ones num-names) (get d 2)) " " "hundred")
        sum-of-tens (reduce + (pop d))]
    (if (> sum-of-tens 0)
      (str hundreds " and " (tens-ones (pop d)))
      hundreds)))

(defn- thousands
  [d]
  (let [thousands (str (get (:ones num-names) (get d 3)) " " "thousand")
        has-hundreds (> (last (pop d)) 0)
        sum-of-rest (reduce + (pop d))]
    (if (> sum-of-rest 0)
      (if has-hundreds
        (str thousands " " (hundreds (pop d)))
        (str thousands " and " (tens-ones (pop (pop d)))))
      thousands)))

(defn- ten-thousands
  [d]
  (let [thousands (str (tens-ones (subvec d (- (count d) 2) (count d))) " " "thousand")
        has-hundreds (> (last (pop (pop d))) 0)
        sum-of-rest (reduce + (pop (pop d)))]
    (if (> sum-of-rest 0)
      (if has-hundreds
        (str thousands " " (hundreds (pop d))) ;; TODO LH feel like this should be pop pop ?
        (str thousands " and " (tens-ones (pop (pop d)))))
      thousands)))

;; TODO LH refactor subvecs to this function
(defn- tailv
  [v n]
  (subvec v (- (count v) n) (count v)))

(defn- headv
  [v n]
  (subvec v 0 n))

(defn- hundred-thousands
  [d]
  (let [thousands (str (hundreds (tailv d 3)) " " "thousand")
        hunds (headv d 3) ;; TODO LH need to think about naming here
        has-hundreds (> (last hunds) 0)
        sum-of-rest (reduce + hunds)]
    (if (> sum-of-rest 0)
      (if has-hundreds
        (str thousands " " (hundreds hunds))
        (str thousands " and " (tens-ones (pop hunds))))
      thousands)))

(defn- millions
  [d]
  (let [millions (str (ones (tailv d 1)) " " "million")
        h-ts (headv d 6)
        has-hundred-thousands (> (last h-ts) 0)
        sum-of-rest (reduce + h-ts)]
    (if (> sum-of-rest 0)
      (if has-hundred-thousands
        (str millions " " (hundred-thousands h-ts))
        (str millions " " (ten-thousands (pop h-ts))))
      millions)))

(defn- ten-millions
  [d]
  (let [millions (str (tens-ones (tailv d 2)) " " "million")
        h-ts (headv d 6)
        has-hundred-thousands (> (last h-ts) 0)
        sum-of-rest (reduce + h-ts)]
    (if (> sum-of-rest 0)
      (if has-hundred-thousands
        (str millions " " (hundred-thousands h-ts))
        (str millions " " (ten-thousands (pop h-ts))))
      millions)))

(defn- hundred-millions
  [d]
  (let [millions (str (hundreds (tailv d 3)) " " "million")
        h-ts (headv d 6)
        has-hundred-thousands (> (last h-ts) 0)
        sum-of-rest (reduce + h-ts)]
    (if (> sum-of-rest 0)
      (if has-hundred-thousands
        (str millions " " (hundred-thousands h-ts))
        (str millions " " (ten-thousands (pop h-ts))))
      millions)))

(defn num-word
  ""
  [input]
  {:pre [(valid-number? input)]}
  (let [d (digits input)
        count (count d)]
    (case count
      1 (ones d)
      2 (tens-ones d)
      3 (hundreds d)
      4 (thousands d)
      5 (ten-thousands d)
      6 (hundred-thousands d)
      7 (millions d)
      8 (ten-millions d)
      9 (hundred-millions d)
      )))
