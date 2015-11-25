import akka.actor.{ ActorRef, ActorSystem, Props, Actor}
import akka.pattern._

import org.jsoup.Jsoup

import scala.collection.JavaConverters._

object Getter {
  case object Get
  case object Done
}

class Getter(url : String, depth : Int) extends Actor {
  import Getter._

  implicit val exec = context.dispatcher
  
  def receive = {
    case Get => {
      val future = WebClient.get(url)
      future pipeTo self
    }
    
    case body : String => {
      for(link <- findLinks(body))
        context.parent ! Controller.Check(link, depth)
      stop()
    }
    
    case _ : Any => stop()
  }
  
  def stop() = {
    context.parent ! Done
    context.stop(self)
  }
  
  def findLinks(body : String) : Iterator[String] = {
    val document = Jsoup.parse(body)
    val links = document.select("a[href]")
    for {
      link <- links.iterator().asScala
      val cleanUrl = link.absUrl("href").trim()
      if (cleanUrl.length() != 0)
    } yield cleanUrl   
  }
}