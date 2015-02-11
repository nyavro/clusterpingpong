package pingpong

import akka.actor.{Actor, Props}

class VeryImportantActor(node:Int) extends Actor {
  override def receive: Receive = {
    case "DoVeryImportantJob" => sender ! s"Very important job is done on node $node"
    case value => sender ! s"Don't disturb me with $value! I'm doing only very important job on node $node!"
  }
}

object VeryImportantActor {
  def props(node:Int) = Props(classOf[VeryImportantActor], node)
}
