package akka

import akka.actor._
import play.api.libs.json._

object AccountManagerActor {
  def props(out: ActorRef) = Props(new AccountManagerActor(out))
  // def props[T <: ActorRef](out: T) = Props(classOf[AccountManagerActor], out)
}

class AccountManagerActor(out: ActorRef) extends Actor {
  override def preStart(): Unit = {
    super.preStart()
  }

  def receive() = {
    case js : JsValue => println("Received JSON")

    case _  : Any     => println("Received other")
  }

  override def postStop(): Unit = {
    // someResource.close()
  }
}

/*
Play will automatically close the WebSocket when your actor
that handles the WebSocket terminates. So, to close the WebSocket,
send a PoisonPill to your own actor:

import akka.actor.PoisonPill

usage: self ! PoisonPill
*/
