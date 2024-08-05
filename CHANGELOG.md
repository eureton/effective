# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]

## [0.9.0] - 2024-08-05
### Fixed
- lopsided interpretation of arguments ([#11](https://github.com/eureton/effective/issues/11))

### Added
- new flags:
  - `:from-fn`
  - `:to-fn`
  - `:by-fn`
  - `:with-fn`
- `effective.util` namespace
  - maiden entry is `contains-map?`
  - placeholder for useful helpers

## [0.8.0] - 2024-08-04
### Added
- implementation of `Composer` which:
  - tallies number of failures
  - groups them by operation / flag
- exposes new composer via the `:test` composition policy
- suite of tests which:
  - provoke failure of generated assertions
  - assert the number of failures

## [0.7.0] - 2024-08-01
### Fixed
- complicated, manual input validation ([#7](https://github.com/eureton/effective/issues/7))

### Added
- [`malli`](https://github.com/metosin/malli) schemas to replace manual:
  - normalization
  - sanitization
  - initialization
  - validation

## [0.6.0] - 2024-07-29
### Added
- operations:
  - `:to-conjoin`
  - `:to-pop`
- flags:
  - `:with` for asserting on conjoined values
  - `:times` for asserting on number of `pop`s
- discounted conformance of input to an implicit schema

### Fixed
- treats symbol-headed lists as functions ([#5](https://github.com/eureton/effective/issues/5))

### Changed
- flags may be shared across operations
- improves validation of input

## [0.5.0] - 2024-01-24
### Added
- Support for disjunction of generated assertions

## [0.4.0] - 2023-11-18
### Added
- Support for function arguments to:
  - `:from`
  - `:to`
  - `:by`

## [0.3.0] - 2023-10-23
### Added
Support for arithmetic comparisons. New `config` options:
- `:from-lt`
- `:from-lte`
- `:from-gt`
- `:from-gte`
- `:from-not`
- `:from-less-than`
- `:from-less-than-or-equal`
- `:from-greater-than`
- `:from-greater-than-or-equal`
- `:from-within`
- `:to-lt`
- `:to-lte`
- `:to-gt`
- `:to-gte`
- `:to-not`
- `:to-less-than`
- `:to-less-than-or-equal`
- `:to-greater-than`
- `:to-greater-than-or-equal`
- `:to-within`
- `:by-lt`
- `:by-lte`
- `:by-gt`
- `:by-gte`
- `:by-not`
- `:by-less-than`
- `:by-less-than-or-equal`
- `:by-greater-than`
- `:by-greater-than-or-equal`
- `:by-within`

Support for asserting no change:
- `:to-not-change` option

### Changed
Renamed:
- `:changes` to `:to-change` in order to conform with `:to-not-change`
- `effect` to `expect`

## [0.2.0] - 2023-09-25
### Changed
- generated code relies on `clojure.core` functions only
- all the action is encapsulated in a single `let` block

### Removed
- `monitor` and `predicate` namespaces

### Fixed
- readability of the generated code

### Added
- top-level tests of the `effect` macro itself

## 0.1.0 - 2023-09-20
### Added
- Initial commit.
- asserts only, i.e. runs effect and monitors in a function.

[Unreleased]: https://github.com/eureton/effective/compare/0.9.0...HEAD
[0.9.0]: https://github.com/eureton/effective/compare/0.8.0...0.9.0
[0.8.0]: https://github.com/eureton/effective/compare/0.7.0...0.8.0
[0.7.0]: https://github.com/eureton/effective/compare/0.6.0...0.7.0
[0.6.0]: https://github.com/eureton/effective/compare/0.5.0...0.6.0
[0.5.0]: https://github.com/eureton/effective/compare/0.4.0...0.5.0
[0.4.0]: https://github.com/eureton/effective/compare/0.3.0...0.4.0
[0.3.0]: https://github.com/eureton/effective/compare/0.2.0...0.3.0
[0.2.0]: https://github.com/eureton/effective/compare/0.1.0...0.2.0
