import akka.actor.{ ActorRef, ActorSystem, Props, Actor }

object Client extends App {
    val system = ActorSystem("LinkCheckerSystem")
    
    val receptionist = system.actorOf(Props[Receptionist], "receptionist");
    
    receptionist ! Receptionist.Get("https://www.google.com");
    
}