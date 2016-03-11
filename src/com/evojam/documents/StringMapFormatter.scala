package com.evojam.documents

import collection.JavaConversions._
import org.bson.Document
import reactivemongo.bson.{BSONString, BSONDocument, BSONDocumentReader, BSONDocumentWriter}

import com.evojam.driver.DocumentFormat

object StringMapFormatter {
  implicit object Handler extends BSONDocumentReader[Map[String, String]] with BSONDocumentWriter[Map[String, String]] {
    override def read(bson: BSONDocument): Map[String, String] =
      bson.elements.map({case (key, value) => (key, value.asInstanceOf[BSONString].as[String])}).toMap

    override def write(t: Map[String, String]): BSONDocument =
      BSONDocument(t.toStream.map({case (k,v) => (k, BSONString(v))}))
  }

  implicit object javaFmt extends DocumentFormat[Map[String, String]] {
    override def writes(a: Map[String, String]) = {
      val doc = new Document()
      a foreach {
        case (k, v) => doc.append(k, v)
      }
      doc
    }

    override def reads(doc: Document): Map[String, String] =
      doc.keySet().map(key => (key, doc.getString(key))).toMap
  }

}
