# effective

`RSpec`-style `expect { ... }.to change` assertions for Clojure.

## Installation

[![Clojars Project](https://img.shields.io/clojars/v/com.eureton/effective.svg)](https://clojars.org/com.eureton/effective)

## Usage

Use the `expect` macro to string up any number of assertions with respect to a given Clojure form. Assertions are grouped by expressions - use `:to-change`, `:to-not-change` and `:to-conjoin` to specify expressions.

Multiple expressions may be specified. By default, `expect` generates code which asserts that they **all** hold. You may use the keyword `:any` (see example below) to specify that **one or more** of them hold.

#### Examples

``` clojure
(let [x (atom 0)]
  (expect (swap! x inc)
          [{:to-change @x
            :from 0
            :to 1}]))
```

The above expands to the following:

``` clojure
(let [x (atom 0)]
  (let [before-0 @x
        _ (swap! x inc)
        after-0 @x]
    (is (= 0 before-0) ":from check failed")
    (is (= 1 after-0) ":to check failed")))
```

---

``` clojure
(let [x (atom [:a :b :c])]
  (expect (swap! x pop)
          [{:to-change (count @x) :by -1}]))
```

The above expands to the following:

``` clojure
(let [before-0 (count @x)
      _ (swap! x pop)
      after-0 (count @x)]
  (is (= -1 (- after-0 before-0)) ":by check failed"))
```

---

``` clojure
(let [x (atom "ABC")]
  (expect (swap! x clojure.string/upper-case)
          :any
          [{:to-change (count @x) :from 3 :to 4}
           {:to-not-change @x}]))
```

The above expands to the following:

``` clojure
(let [x (atom "ABC")]
  (let [before-0 (count @x)
        before-1 @x
        _ (swap! x clojure.string/upper-case)
        after-0 (count @x)
        after-1 @x]
    (is (or (and (= 3 before-0)
                 (= 4 after-0))
            (= after-1 before-1)))))
```

---

`:from`, `:to` and `:by` could also be assigned a function. In that case, we assert the result of applying the function to the corresponding checkpoint:

``` clojure
(let [x (atom 1)]
  (expect (swap! x inc)
          [{:to-change @x :from odd? :to even?}]))
```

The above expands to the following:

``` clojure
(let [before-0 @x
      _ (swap! x inc)
      after-0 @x]
  (is (odd? before-0) ":from check failed")
  (is (even? after-0) ":to check failed"))
```

---

`:from-within` / `:to-within` let us assert that a checkpoint lies within some continuous numerical range. Use them with a `[radius :of origin]` vector as follows:

``` clojure
(let [x (atom 0)]
  (expect (swap! x inc)
          [{:to-change @x :from-within [0.6 :of -0.05]}]))
```

The above expands to the following:

``` clojure
(let [x (atom 0)]
  (let [before-0 @x
        _ (swap! x inc)
        after-0 @x]
    (is (>= 0.6 (java.lang.Math/abs (- before-0 -0.05)))
        ":from-within check failed")))
```

---

Assert growth of sequential collections with `:to-conjoin`. It may be given either static values or predicates:

``` clojure
(let [x (atom [-2 -1])]
  (expect (reset! x [-2 -1 0 1 2])
          [{:to-conjoin @x :with [zero? 1 even?]}]))
```

The above expands to the following:

``` clojure
(let [x (atom [-2 -1])]
  (let [before-0 @x
        _ (reset! x [-2 -1 0 1 2])
        after-0 @x]
    (is (= before-0 (pop (pop (pop after-0)))) ":with check failed")
    (is (zero? (peek (pop (pop after-0)))) ":with check failed")
    (is (= 1 (peek (pop after-0))) ":with check failed")
    (is (even? (peek after-0)) ":with check failed")))
```

## Development

Run tests:

``` bash
lein test
```

## License

[MIT License](https://github.com/eureton/effective/blob/master/LICENSE)
