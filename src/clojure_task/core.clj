(ns clojure-task.core
  (:require [clojure.string :as str]))

(def num-names {:ones ["zero" "one" "two" "three" "four"
                       "five" "six" "seven" "eight" "nine"]
                :teens ["ten" "eleven" "twelve" "thirteen" "fourteen"
                        "fifteen" "sixteen" "seventeen" "eighteen" "nineteen"]
                :tens [nil nil "twenty" "thirty" "forty"
                       "fifty" "sixty" "seventy" "eighty" "ninety"]})

(def period-names [nil "thousand" "million"])

(def min-int 0)
(def max-int 999999999)

(defn- valid-number?
  [x]
  (and (integer? x) (<= min-int x max-int)))

(defn- digits
  "Transforms an integer to a reversed vector of digits.
  eg. 123 -> [3 2 1]"
  [x]
  (->> x
       (iterate #(quot % 10))
       (take-while pos?)
       (mapv #(mod % 10))))

(defn- ones
  [d]
  ((:ones num-names) d))

(defn- tens
  [ds]
  (str
   ((:tens num-names) (last ds))
   (when (pos? (first ds))
     (str " " (ones (first ds))))))

(defn- tens-ones
  "Checks if digit sequence passed in is a ones 0-9,
  teens 10-19 or tens 19-99 and maps to words."
  [ds]
  (let [has-tens (and (= (count ds) 2) (pos? (last ds)))
        is-teen (and has-tens (= (last ds) 1))]
    (if has-tens
      (if is-teen
        ((:teens num-names) (first ds))
        (tens ds))
      (ones (first ds)))))

(defn- tens-ones-prefix
  "Adds 'and' to tens and ones that need it to satisfy
  natural language rules 1021 - one thousand and twenty one"
  [ds prefix]
  (let [tens (tens-ones ds)
        has-tens (pos? (reduce + ds))]
    (when has-tens
      (if prefix
        (str "and " tens)
        tens))))

(defn- hundreds
  "Transforms the digit sequence to hundreds, tens and ones. If
  hundreds are present overrides prefix to true otherwise passes
  it along (this controls inclusion of 'and')."
  [ds prefix]
  (let [has-hundreds (and (= (count ds) 3) (pos? (last ds)))
        hundreds (str (ones (last ds)) " " "hundred")
        tens-pos (if (= (count ds) 3) (pop ds) ds)]
    (if has-hundreds
      (str/join " " (keep identity [hundreds (tens-ones-prefix tens-pos true)]))
      (tens-ones-prefix tens-pos prefix))))

(defn- period-name
  [i]
  (when-let [p (period-names i)]
    (str " " p)))

(defn- period
  "Takes a period (number part grouped in 3's) the index of the part and
  the length of the overall number. Returns the natural language version
  of the period with the period name added (thousand, million etc)"
  [ds i len]
  (when-let [period (hundreds ds (and (= i 0) (> len 3)))]
    (str period (period-name i))))

(defn num->word
  "Takes a integer between 0 and 999999999 and returns the natural
  language version eg. 123 -> 'one hundred and twenty three'."
  [x]
  {:pre [(valid-number? x)]}
  (let [ds (digits x)
        len (count ds)
        periods (map vec (partition 3 3 [] ds))]
    (if (= x 0)
      (ones x); special case for zero
      (->> periods
           (map-indexed #(period %2 %1 len))
           (keep identity) ; remove nils
           reverse
           (str/join " ")))))
