package cn.bigdata
package cn.piflow.api


import java.net.InetAddress

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory

import scala.concurrent.{Await, Future}
import spray.json.DefaultJsonProtocol


object HTTPService extends DefaultJsonProtocol with Directives with SprayJsonSupport{
  implicit val config = ConfigFactory.load()
  implicit val system = ActorSystem("PiFlowHTTPService", config)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher


  def route(req: HttpRequest): Future[HttpResponse] = req match {
    case HttpRequest(POST, Uri.Path("/test"), headers, entity, protocol) =>{
      try{

        val bodyFeature = Unmarshal(entity).to[String]
        val flowGroupJson = Await.result(bodyFeature,scala.concurrent.duration.Duration(1000,"second"))


        Future.successful(HttpResponse(200, entity = ""))
      }catch {
        case ex => {
          println(ex)
          Future.successful(HttpResponse(500, entity = "Can not start group!"))
        }
      }
    }
  }



  def run = {

    val ip = InetAddress.getLocalHost.getHostAddress
    println(ip + ":8001 started!!!!")
    Http().bindAndHandleAsync(route, ip, 8001)
  }

  def streamMigrationFile(source: Source[ByteString, NotUsed]): Future[String] = {

    var fileString = ""

    //def foreach[T](f: T ⇒ Unit): Sink[T, Future[Done]]
    val sinkForEach: Sink[ByteString, Future[Done]] = Sink.foreach[ByteString](byteString => fileString =
      fileString.concat(byteString.decodeString("US-ASCII")))

    /*
      def fold[U, T](zero: U)(f: (U, T) ⇒ U): Sink[T, Future[U]] =
        Flow[T].fold(zero)(f).toMat(Sink.head)(Keep.right).named("foldSink")
     */
    val sinkFold: Sink[ByteString, Future[String]] = Sink.fold("") { case (acc, str) =>
      acc + str
    }

    val res1: Future[Done] = source.runWith(sinkForEach)
    val res2: Future[String] = source.runWith(sinkFold)

    res2

  }

}




object Main {


  def main(argv: Array[String]):Unit = {

    HTTPService.run

  }
}
