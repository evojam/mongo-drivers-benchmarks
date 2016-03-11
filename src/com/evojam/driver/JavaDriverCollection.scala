package com.evojam.driver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import org.bson.Document

import com.evojam.Collection
import com.mongodb.async.client.{MongoClient, MongoClients, MongoCollection}
import com.mongodb.client.model.Filters

class JavaDriverCollection[A](client: MongoClient, collection: MongoCollection[Document], implicit val fmt: DocumentFormat[A])
  extends Collection[A] with CallbackFutureConversions {

  override def truncate(): Future[Unit] = runAsFuture(collection.drop).map(_ => ())

  override def insert(doc: A): Future[Unit] = runAsUnitFuture(cb =>
      collection.insertOne(DocumentFormat.writes(doc), cb)
    )

  override def get(id: String): Future[Option[A]] =
    runAsFuture(collection.find(Filters.eq("_id", id)).first) map (d => Option(d) map DocumentFormat.reads[A])

  override def close(): Unit = {
    client.close()
  }

  override val name: String = "Java driver"
}

