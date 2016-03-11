import java.util.UUID

import scala.concurrent.Await
import scala.concurrent.duration._

import org.scalameter.api.{Gen, Bench}

import com.evojam.Collection
import com.evojam.documents.NestedDoc
import com.evojam.driver.JavaDriverProvider
import com.evojam.reactivemongo.ReactivemongoProvider


import scala.concurrent.ExecutionContext.Implicits.global

trait NestedDocSpec extends MongoBenchmarkHelpers { self: Bench[_] =>
  val scale: Gen[Int]

  performance of "Handling nested documents" in {
    val comparison = new DriverComparison(ReactivemongoProvider[NestedDoc], JavaDriverProvider[NestedDoc])
    def create(i: Int, id: UUID): NestedDoc = i match {
      case 0 => NestedDoc(s"leaf-$id", id.toString, None, None)
      case _ => NestedDoc(s"node-depth-$i", id.toString, Some(create(i - 1, id)), Some(create(i - 1, id)))
    }
    def createAndInsert(driver: Collection[NestedDoc], i: Int) = {
      val id = UUID.randomUUID()
      driver.insert(create(i, id)) map (_ => id)
    }

    comparison.compare("insert and get").using(scale map (_/100+1)) { (driver, num) =>
      Await.result(
        createAndInsert(driver, num).flatMap(id => driver.get(id.toString)),
        3.seconds
      )
    }

  }

}
