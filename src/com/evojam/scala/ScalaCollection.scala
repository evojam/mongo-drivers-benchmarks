package com.evojam.scala

import scala.concurrent.ExecutionContext.Implicits.global

import org.bson.Document
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.{MongoClient, MongoCollection}

import com.evojam.Collection
import com.evojam.driver.DocumentFormat

class ScalaCollection[A](collection: MongoCollection[Document], client: MongoClient, implicit val fmt: DocumentFormat[A]) extends Collection[A] {
  override def truncate() = collection.drop().toFuture().map(_ => ())

  override def insert(doc: A) = collection.insertOne(fmt.writes(doc)).toFuture().map(_ => ())

  override def get(id: String) = collection.find[Document](equal("_id", id))
    .first().toFuture()
    .map(_.headOption.map(fmt.reads))

  override def close() = client.close()

  override val name: String = "Scala driver"
}
