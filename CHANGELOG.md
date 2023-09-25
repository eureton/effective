# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]
### Changed
### Removed
### Fixed

## 0.2.0 - 2023-09-25
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
