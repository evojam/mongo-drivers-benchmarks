import scala.concurrent.Await
import scala.concurrent.duration._

import org.scalameter.api._

import com.evojam.Collection

trait MongoBenchmarkHelpers {  self: Bench[_] =>

  class DriverComparison[T](reactive: => Collection[T], java: => Collection[T]) {
    def compare(method: String) = new SingleMethodComparison(method)

    class SingleMethodComparison(name: String) {
      def using[A](gen: Gen[A])(block: (Collection[T], A) => Unit) = {
        measure method name in {
          runWithDriver(gen, () => reactive, "reactivemongo")(block)
          runWithDriver(gen, () => java, "java")(block)
        }
      }

      private def runWithDriver[A](gen: Gen[A], driverGen: () => Collection[T], curve: String)(
          block: (Collection[T], A) => Unit) = {

        var _driver: Collection[T] = null
        self.using(gen) curve curve beforeTests {
          _driver = driverGen()
          Await.result(_driver.truncate(), 10.seconds)
        } afterTests {
          _driver.close()
        } in {
          block(_driver, _)
        }

      }
    }

  }

}
