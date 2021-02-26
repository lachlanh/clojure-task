(ns wefarm-task.core-test
  (:require [clojure.test :refer :all]
            [wefarm-task.core :refer :all]))

(deftest bootstrap-test
  (testing "bootstrap test, do something"
    (is (= "sixty six" (num-word 66)))))

(deftest input-bounds-test
  (testing "check that out of bounds input fails"
    (is (thrown? java.lang.AssertionError (num-word -500)))
    (is (thrown? java.lang.AssertionError (num-word "99")))
    (is (thrown? java.lang.AssertionError (num-word 1000000000)))))

(deftest ones-test
  (testing "values 0 through 9"
    (is (= (num-word 0) "zero"))
    (is (= (num-word 1) "one"))
    (is (= (num-word 9) "nine"))))
