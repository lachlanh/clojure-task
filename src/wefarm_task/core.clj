(ns wefarm-task.core)

(defn- valid-number?
  [x]
  (and (integer? x) (>= x 0) (<= x 999999999)))

(defn num-word
  ""
  [input]
  {:pre [(valid-number? input)]}
  "sixty six")
