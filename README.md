# clojure-task

## Lachlan Hespe

A Clojure library to turn an integer input into a natural language string eg. 99 -> 'ninety nine'

## Technical Decisions

- Keep the code simple and readable.
- Take an iterative approach to solving the problem. Tests preceeded most of the code  to get something working. Some of the tests are now redundant, but show the progression through the code. (also see the commit history for more detail)
- Follow consistent clojure style as much as possible, used [https://guide.clojure.style].

## Notes

- This task was a previous coding challenge.

## Build

Commands listed here require having lein/clojure/java installed and accessible on the path [*](#Docker-alternative).

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

#### Docker alternative

If you don't have lein installed it is possible to run the lein commands using the standard clojure docker image. Use `docker run -it --rm -v "$PWD":/usr/src/app -w /usr/src/app clojure lein cloverage` any artifacts of the lein command will probably have permissions matching the docker daemon (everything was created as root when tested locally), it should be possible to overcome this with the `-u` flag in docker but that is tangential to the task at hand.


## License

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
