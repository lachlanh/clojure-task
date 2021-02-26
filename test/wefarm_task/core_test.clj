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

(deftest teens-test
  (testing "values 10 through 19"
    (is (= (num-word 10) "ten"))
    (is (= (num-word 19) "nineteen"))))

(deftest tens-test
  (testing "values 20 through 99"
    (is (= (num-word 20) "twenty"))
    (is (= (num-word 26) "twenty six"))
    (is (= (num-word 99) "ninety nine"))
    (is (= (num-word 42) "forty two"))))

(deftest hundreds-test
  (testing "values 100 through 999"
    (is (= (num-word 100) "one hundred"))
    (is (= (num-word 102) "one hundred and two"))
    (is (= (num-word 116) "one hundred and sixteen"))
    ))

(deftest thousands-test
  (testing "values 1000 through 9999"
    (is (= (num-word 1000) "one thousand"))
    (is (= (num-word 1002) "one thousand and two"))
    (is (= (num-word 1016) "one thousand and sixteen"))
    (is (= (num-word 1020) "one thousand and twenty"))
    (is (= (num-word 1120) "one thousand one hundred and twenty"))))
