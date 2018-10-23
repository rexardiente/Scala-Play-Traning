package actors

import akka.actor._

case object StartMessage
case object StopMessage

class PingPongExample {
  implicit val system: ActorSystem = ActorSystem("PingPongExample")
  implicit val pong  : ActorRef    = system.actorOf(Props[Pong])
  val ping           : ActorRef    = system.actorOf(Props[Ping])

  ping ! "Ping"
}


class Ping(actor: ActorRef, system: ActorSystem) extends Actor {
  /* override def preStart(): Unit = super.preStart()
  *  override def postStop(): Unit = super.postStop()
  */

  def receive() = {
    case StartMessage =>
      actor ! "Ping!"

    case str: String =>     // Print "Pong!"
      println(s"Received: $str")

      /* Example validation here */
      if (100 > 100) {
        system.stop(actor)  // Stop by ActorSystem instance.
        context.stop(actor) // Stop a child actor by using the context reference.
        context.stop(self)  // An actor can also stop itself.
        actor ! PoisonPill  // You can stop an actor by sending it a PoisonPill message.
      } else {
        sender ! "Ping!"   // Send ""
      }

    case _ =>
      println("Something went wrong!")
  }
}

class Pong extends Actor {
  def receive() = {
    case str: String =>
      println(s"Received: $str") // Print "Ping!"
      sender ! "Pong!"           // Send "Pong!"

    case _ =>
      println("Something went wrong!")
  }
}

object Algorithm {
  def getCombinaison(n: Int) = {
    var table = new Array[Int](n+1)

    table(0) = 1

    for(x <- 1 to 6) {
      for(y <- (x - 1) to 6) {
        table(y) = table(y - x)
      }
    }

    table
  }
}

// static int getCombinaison(int n)
// {
//     int[] table = new int[n+1];
//     // table[i] will store count of solutions for

//     // Base case (If given value is 0)
//     table[0] = 1;

//     // One by one consider given 3 moves and update the table[]
//     // values after the index greater than or equal to the
//     // value of the picked move
//     for (int j=1; j<7; j++) {
//         for (int i=j; i<=n; i++)
//            table[i] += table[i-j];
//     }

//     return table[n];
// }
