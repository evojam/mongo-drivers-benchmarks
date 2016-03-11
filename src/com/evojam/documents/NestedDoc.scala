package com.evojam.documents

import org.bson.Document
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

import com.evojam.driver.DocumentFormat

case class NestedDoc(name: String, _id: String, left: Option[NestedDoc], right: Option[NestedDoc])

object NestedDoc {
  implicit object NestedDocHandler extends BSONDocumentReader[NestedDoc] with BSONDocumentWriter[NestedDoc]{
    override def read(bson: BSONDocument): NestedDoc = {
      val left = bson.get("left").map(t => read(t.asInstanceOf[BSONDocument]))
      val right = bson.get("right").map(t => read(t.asInstanceOf[BSONDocument]))
      NestedDoc(bson.getAs[String]("name").get, bson.getAs[String]("_id").get, left, right)
    }

    override def write(t: NestedDoc): BSONDocument = {
      BSONDocument("_id" -> t._id, "name" -> t.name)
        .add("left" -> t.left.map(write))
        .add("right" -> t.right.map(write))
    }
  }
  implicit val javaFormat = new DocumentFormat[NestedDoc] {
    override def writes(a: NestedDoc): Document = {
      val doc = new Document("name", a.name)
      a.left.foreach(t => doc.append("left", writes(t)))
      a.right.foreach(t => doc.append("right", writes(t)))
      doc.append("_id", a._id)
    }

    override def reads(doc: Document): NestedDoc = {
      val left = Option(doc.get[Document]("left", classOf[Document])).map(reads)
      val right = Option(doc.get[Document]("right", classOf[Document])).map(reads)
      NestedDoc(doc.getString("name"), doc.getString("_id"), left, right)
    }
  }

}