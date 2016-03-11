package com.evojam.driver

import com.mongodb.async.client.MongoClients

object JavaDriverProvider {
  def apply[T](dbName: String, colName: String)(implicit fmt: DocumentFormat[T]): JavaDriverCollection[T] = {
    val client = MongoClients.create("mongodb://localhost")
    val db = client.getDatabase(dbName)
    new JavaDriverCollection[T](client, db.getCollection(colName), fmt)
  }

  def apply[T](implicit fmt: DocumentFormat[T]): JavaDriverCollection[T] = apply[T]("test", "mongo-driver-benchmarks")
}
