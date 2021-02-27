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

(deftest ten-thousands-test
  (testing "values 10,000 - 99,000"
    (is (= (num-word 10000) "ten thousand"))
    (is (= (num-word 10005) "ten thousand and five"))
    (is (= (num-word 10105) "ten thousand one hundred and five"))
    ))

(deftest hundred-thousands-test
  (testing "values 100,000 - 999,999"
    (is (= (num-word 100000) "one hundred thousand"))
    (is (= (num-word 110005) "one hundred and ten thousand and five"))
    (is (= (num-word 917055) "nine hundred and seventeen thousand and fifty five"))
    (is (= (num-word 207255) "two hundred and seven thousand two hundred and fifty five"))
    ))

(deftest millions-test
  (testing "values 1,000,000 - 9,999,999"
    (is (= (num-word 1000000) "one million"))
    (is (= (num-word 9517000) "nine million five hundred and seventeen thousand"))
    (is (= (num-word 3007002) "three million seven thousand and two")) ;; TODO LH not sure if this should be three million and seven thousand and two ???
    ))

