package com.evojam.driver

import org.bson.Document


trait DocumentFormat[A] {
  def writes(a: A): Document
  def reads(doc: Document): A
}

object DocumentFormat {
  def writes[A](a: A)(implicit fmt: DocumentFormat[A]) = fmt.writes(a)
  def reads[A](doc: Document)(implicit fmt: DocumentFormat[A]) = fmt.reads(doc)
}
