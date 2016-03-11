package com.evojam

import scala.concurrent.Future

trait Collection[Document] {

  def truncate(): Future[Unit]
  def insert(doc: Document): Future[Unit]
  def get(id: String): Future[Option[Document]]

  def close(): Unit

  val name: String

}
