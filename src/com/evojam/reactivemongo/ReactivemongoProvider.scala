package com.evojam.reactivemongo

import scala.concurrent.ExecutionContext.Implicits.global

import reactivemongo.api.MongoDriver
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter}

import com.evojam.Collection

object ReactivemongoProvider {
  def apply[T](dbName: String, collectionName: String)(implicit reader: BSONDocumentReader[T], writer: BSONDocumentWriter[T]): Collection[T] = {
    val driver = new MongoDriver
    val connection = driver.connection(Seq("localhost"))
    val db = connection(dbName)
    val collection = db(collectionName)
    new ReactivemongoCollection[T](driver, collection, reader, writer)
  }
  def apply[T](implicit r: BSONDocumentReader[T], w: BSONDocumentWriter[T]): Collection[T] = apply[T]("test", "mongo-driver-benchmarks")

}
