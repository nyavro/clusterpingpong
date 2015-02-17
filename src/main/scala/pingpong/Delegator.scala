package pingpong

import akka.actor.{Props, Actor, ActorRef}

/**
 * Created by eny on 18.02.15.
 */
class Delegator(target:ActorRef) extends Actor {
  override def receive: Receive = {
    case msg:AnyRef => target forward msg
  }
}

object Delegator {
  def props(target:ActorRef) = Props(classOf[Delegator], target)
}