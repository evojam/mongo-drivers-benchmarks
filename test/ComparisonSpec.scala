import org.scalameter.{Bench, Gen}


object ComparisonSpec extends Bench.OfflineReport with SmallDocSpec with NestedDocSpec with MultiFieldDocSpec {
  override lazy val scale: Gen[Int] = Gen.range("scale")(0, 1000, 1)

}
