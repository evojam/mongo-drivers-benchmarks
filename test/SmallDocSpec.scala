import java.util.UUID

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

import org.scalameter._

import com.evojam.Collection
import com.evojam.documents.SmallDoc
import com.evojam.driver.JavaDriverProvider
import com.evojam.reactivemongo.ReactivemongoProvider
import com.evojam.scala.ScalaProvider

trait SmallDocSpec extends MongoBenchmarkHelpers { self: Bench.OfflineReport =>
  val scale: Gen[Int]


  performance of "Handling flat documents" in {
    val comparison = new DriverComparison(
      ReactivemongoProvider[SmallDoc],
      JavaDriverProvider[SmallDoc],
      ScalaProvider[SmallDoc])

    def createAndInsert(driver: Collection[SmallDoc], i: Int): Future[UUID] = {
      val id = UUID.randomUUID()
      driver
        .insert(SmallDoc(id.toString, Random.alphanumeric.take(i).mkString, i, i%2==0))
        .map(_ => id)
    }
    comparison.compare("insert and get").using(scale) { (driver, num) =>
      Await.result(createAndInsert(driver, num)
        .flatMap(id => driver.get(id.toString))
        .map(docOpt => require(docOpt.get.b == num)),
        2.seconds
      )
    }
    comparison.compare("insert and get with long field").using(scale) {(driver, num) =>
      Await.result(createAndInsert(driver, num*100)
        .flatMap(id => driver.get(id.toString))
        .map(docOpt => require(docOpt.get.b == num*100)),
        4.seconds
      )
    }
  }

}
