import java.util.UUID

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

import org.scalameter.api._

import com.evojam.Collection
import com.evojam.documents.StringMapFormatter._
import com.evojam.driver.JavaDriverProvider
import com.evojam.reactivemongo.ReactivemongoProvider
import com.evojam.scala.ScalaProvider

trait MultiFieldDocSpec extends MongoBenchmarkHelpers { self: Bench[_] =>
  val scale: Gen[Int]

  performance of "Handling multi-field documents" in {
    val comparison = new DriverComparison(
      ReactivemongoProvider[Map[String, String]],
      JavaDriverProvider[Map[String, String]],
      ScalaProvider[Map[String, String]])

    def create(size: Int, id: UUID): Map[String, String] =
      Vector.fill(size)(Random.alphanumeric.take(20).mkString, "field content").toMap + ("_id" -> id.toString)

    def createAndInsert(driver: Collection[Map[String, String]], i: Int): Future[UUID] = {
      val id = UUID.randomUUID()
      driver
        .insert(create(i, id))
        .map(_ => id)
    }
    comparison.compare("insert and get").using(scale map (_/10)) { (driver, num) =>
      Await.result(createAndInsert(driver, num)
        .flatMap(id => driver.get(id.toString)),
      2.seconds
      )
    }
  }
}
