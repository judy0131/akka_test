package cn.bigdata
package cn.piflow.api


import java.net.InetAddress

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import com.typesafe.config.ConfigFactory

import scala.concurrent.{Await, Future}
import spray.json.DefaultJsonProtocol
import scala.concurrent._
import scala.concurrent.duration._


object HTTPService extends Directives{
  implicit val config = ConfigFactory.load()
  implicit val system = ActorSystem("PiFlowHTTPService", config)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher


  def route(req: HttpRequest): Future[HttpResponse] = req match {
    case HttpRequest(POST, Uri.Path("/test"), headers, entity, protocol) =>{


        val s1 = entity.dataBytes
        val s2 = s1.runReduce((u1, u2) => u1.++(u2))


       s2.onComplete {
          case scala.util.Success(json) => {
            println(json)
            println(json.utf8String)
            //result = Future.successful(HttpResponse(500, entity="Fail!"))
          }
          case scala.util.Failure(ex) => {
            println(ex)
            //result = Future.successful(HttpResponse(500, entity="Fail!"))
          }
          case _ => {
            println("Exception!!!!!!!!!!!!!!!!")
            //result = Future.successful(HttpResponse(500, entity="Fail!"))
          }
        }

      Future.successful(HttpResponse(500, entity="OK!"))

    }
  }

  def run = {

    val ip = InetAddress.getLocalHost.getHostAddress
    println(ip + ":8001 started!!!!")
    Http().bindAndHandleAsync(route, ip, 8001)

  }

}




object Main {


  def main(argv: Array[String]):Unit = {

    HTTPService.run

  }
}
