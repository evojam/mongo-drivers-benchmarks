mongo-drivers-benchmarks
========================

This is a microbenchmark that compares the official [Java MongoDB driver]
version 3.2.2 with the [Reactivemongo] library (version 0.11.10). The benchmarks
run sequential `insert` and `findOne{_id: _}` queries with three types of
documents: small docs (four fields, up to 1K chars), nested docs (binary trees
with depth up to 10) and multi-field docs (up to 1K keys).

Results
-------

Check out [the report generated on my laptop]. It basically says that
Reactivemongo is slightly slower than the Java driver, but the difference is
negligible (Intel i7, 16GB RAM, Arch x86\_64).

Running yourself
----------------

Launch your local MongoDB instance, run `sbt test` and go make yourself a
coffee. When you come back, you should have a nice report on your desk (well, at
least in `target/benchmark/reports/index.html`). The tests will use
`mongo-driver-benchmarks` collection in the `test` database.

  [Java MongoDB driver]: https://docs.mongodb.org/ecosystem/drivers/java/
  [Reactivemongo]: http://reactivemongo.org/
  [the report generated on my laptop]: http://evojam.github.io/mongo-drivers-benchmarks/
