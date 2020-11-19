package cn.bigdata

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


object HTTPService1 extends DefaultJsonProtocol with Directives with SprayJsonSupport{
  implicit val config = ConfigFactory.load()
  implicit val system = ActorSystem("PiFlowHTTPService", config)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher


  val routes =
    {
      path("/test"){
        get{
          complete(HttpResponse(500, entity="test!"))
        }
        post(as[String]){

        }

      }~
      path("/xjzhu"){
        get{
          complete("xjzhu")
        }
      }


    }

  def run = {

    val ip = InetAddress.getLocalHost.getHostAddress
    println(ip + ":8002 started!!!!")
    val bindingFuture = Http().newServerAt(ip,8002).bind(routes)

  }

}




object Main1 {


  def main(argv: Array[String]):Unit = {

    HTTPService1.run

  }
}
