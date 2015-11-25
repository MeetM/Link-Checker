import akka.actor.{ ActorRef, ActorSystem, Props, Actor }
import akka.pattern._

object Controller {
  case class Check(url : String, depth : Int)
  case class Result(links : Set[String])
}

class Controller extends Actor{
  import Controller._
  var cache = Set.empty[String]
  var childCounter = 0
  
  def receive = {
    case Check(url : String, depth : Int) => {
      if(!cache(url) && depth > 0){
        childCounter = childCounter + 1
        val getter = context.actorOf(Props(new Getter(url, depth - 1)))
        getter ! Getter.Get
      }
      cache+=url
    }
    
    case Getter.Done => {
      childCounter = childCounter - 1
      if(childCounter == 0) context.parent ! Result(cache)
    }
  }
}