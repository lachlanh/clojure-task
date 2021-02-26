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
        (str thousands " " (hundreds (pop d)))
        (str thousands " and " (tens-ones (pop (pop d)))))
      thousands)))


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
      )))

(defn num-word-old
  ""
  [input]
  {:pre [(valid-number? input)]}
  (if (= (quot input 10) 0)
    (get (:ones num-names) input)
    (if (= (quot input 10) 1)
      (get (:teens num-names) (rem input 10))
      (if (<= 2 (quot input 10) 9)
        (str (get (:tens num-names) (quot input 10)) (get (:ones num-names) (rem input 10)))))))
