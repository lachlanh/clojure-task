# wefarm-task

## Lachlan Hespe

A Clojure library to turn numbers into a english language string 99 -> 'ninety nine'

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

Coverage
```bash
lein cloverage
```

## Usage

```clojure
(num->word 99) ; return ninety nine
```

## License

Copyright © 2021 Lachlan Hespe

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
