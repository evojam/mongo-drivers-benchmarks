package com.evojam.scala

import org.mongodb.scala.MongoClient

import com.evojam.driver.DocumentFormat

object ScalaProvider {
  def apply[T](implicit fmt: DocumentFormat[T]): ScalaCollection[T] = {
    val client = MongoClient()
    val collection = client.getDatabase("test").getCollection[org.bson.Document]("mongo-driver-benchmarks")
    new ScalaCollection[T](collection, client, fmt)
  }
}
