# effective

`RSpec`-style `expect { ... }.to change` assertions for Clojure.

## Installation

### Leiningen/Boot

``` clj
[com.eureton/effective "0.3.0"]
```

### Clojure CLI/deps.edn

``` clj
com.eureton/effective {:mvn/version "0.3.0"}
```

### Gradle

``` java
implementation("com.eureton:effective:0.3.0")
```

### Maven

``` xml
<dependency>
  <groupId>com.eureton</groupId>
  <artifactId>effective</artifactId>
  <version>0.3.0</version>
</dependency>
```

## Usage

Use the `expect` macro to string up any number of assertions with respect to a given Clojure form.

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
          [{:to-not-change @x}]))
```

The above expands to the following:

``` clojure
(let [x (atom "ABC")]
  (let [before-0 @x
        _ (swap! x clojure.string/upper-case)
        after-0 @x]
    (is (= after-0 before-0) ":to-not-change check failed")))
```

## Development

Run tests:

``` bash
lein test
```

## License

[MIT License](https://github.com/eureton/effective/blob/master/LICENSE)
