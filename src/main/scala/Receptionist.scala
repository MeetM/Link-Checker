import akka.actor.{ ActorRef, ActorSystem, Props, Actor }

object Receptionist {
    case class Get(url : String)
}

class Receptionist extends Actor {
    import Receptionist._
    
    val depth = 2
    
    def receive = {
        case Get(url : String) => {
            val controller = context.actorOf(Props[Controller])
            controller ! Controller.Check(url, depth)
        }
        
        case Controller.Result(links) => {
          for(link <- links){
            println(link)
          }
        }
    }
}