package com.evojam.documents

import org.bson.Document
import reactivemongo.bson._

import com.evojam.driver.DocumentFormat

case class SmallDoc(_id: String, a: String, b: Int, c: Boolean)

object SmallDoc {
  implicit val reactiveHandler = Macros.handler[SmallDoc]
  implicit val javaFormatter = new DocumentFormat[SmallDoc] {
    override def writes(a: SmallDoc): Document = new Document("_id", a._id)
      .append("a", a.a)
      .append("b", a.b)
      .append("c", a.c)

    override def reads(doc: Document): SmallDoc = SmallDoc(doc.getString("_id"), doc.getString("a"), doc.getInteger("b"), doc.getBoolean("c"))
  }
}
