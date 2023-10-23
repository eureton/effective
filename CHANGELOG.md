# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]
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

[Unreleased]: https://github.com/eureton/effective/compare/0.2.0...HEAD
[0.2.0]: https://github.com/eureton/effective/compare/0.1.0...0.2.0
