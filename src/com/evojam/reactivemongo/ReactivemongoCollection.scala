package com.evojam.reactivemongo

import scala.concurrent.Future

import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._
import scala.concurrent.ExecutionContext.Implicits.global

import com.evojam.Collection

class ReactivemongoCollection[Doc](
    driver: MongoDriver,
    collection: BSONCollection,
    implicit val reader: BSONDocumentReader[Doc],
    implicit val writer: BSONDocumentWriter[Doc]) extends Collection[Doc] {

  override val name = "reactivemongo"
  override def truncate(): Future[Unit] = collection.drop(failIfNotFound = false).map(_ => ())

  override def insert(doc: Doc): Future[Unit] = collection.insert(doc) map {
    case result if result.ok => ()
    case result => throw result.errmsg.map(new Exception(_)).getOrElse(new Exception("Unknown reactivemongo failure"))
  }

  override def get(id: String): Future[Option[Doc]] = collection.find(BSONDocument("_id" -> id)).one[Doc]

  override def close() = {
    collection.db.connection.close()
    driver.close()
  }
}

