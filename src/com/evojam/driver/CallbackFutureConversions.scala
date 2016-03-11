package com.evojam.driver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise

import com.mongodb.async.SingleResultCallback

trait CallbackFutureConversions {

  def runAsFuture[A](f: (SingleResultCallback[A]) => Unit) = {
    val promise = Promise[A]()
    f(new SingleResultCallback[A] {
      override def onResult(result: A, t: Throwable): Unit = {
        if (t == null) promise.success(result)
        else promise.failure(t)
      }
    })
    promise.future
  }

  def runAsUnitFuture(f: (SingleResultCallback[Void]) => Unit) = runAsFuture[Void](f) map (_ => ())
}
