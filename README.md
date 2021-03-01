# wefarm-task

## Lachlan Hespe

A Clojure library to turn numbers into a natural language string 99 -> 'ninety nine'

## Technical Decisions

- Keep the code simple and readable.
- Take an iterative approach to solving the problem. Tests preceeded most of the code with an approach of just enough to get something working. Some of the tests are now redundant, but show the progression through the code. (also see the commit history for more detail)
- Follow consistent clojure style as much as possible, used [https://guide.clojure.style].

## Notes

- Maybe some more examples of natural language numbers. I used 3007002 as a test and had to think a bit whether "three million _and_ seven thousand and two" may be correct, decided it wasn't :).
- I feel that the my implementation could be further collapsed using more functional language features, but had to decide when to stop, I look forward to getting feedback on how it could be improved.

## Build

Commands listed here require having lein/clojure/java installed and accessible on the path.
;; TODO LH maybe include a docker file for building/testing.

```bash
lein jar
```

## Test

```bash
lein test
```

### Coverage

```bash
lein cloverage
```

## Usage

```clojure
(num->word 99) ;=> returns ninety nine
```

## License

Copyright © 2021 Lachlan Hespe

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
