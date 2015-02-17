package pingpong

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest._

class DelegatorTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll{

  def this() = this(ActorSystem("PostponeSpec"))

  override def afterAll() = system.shutdown()

  "Delegates job to underlying actor" in {
    val actor = system.actorOf(VeryImportantActor.props(222))
    val delegator = system.actorOf(Delegator.props(actor))
    delegator ! "DoVeryImportantJob"
    expectMsg("Very important job is done on node 222")
    expectNoMsg()
  }

  "Delegates job to underlying actor 2" in {
    val actor = system.actorOf(VeryImportantActor.props(333))
    val delegator = system.actorOf(Delegator.props(actor))
    delegator ! "DoSomeJob"
    expectMsg("Don't disturb me with DoSomeJob! I'm doing only very important job on node 333!")
    expectNoMsg()
  }

  "Delegates job to underlying actor 3" in {
    val actor = system.actorOf(VeryImportantActor.props(555))
    val delegator1 = system.actorOf(Delegator.props(actor))
    val delegator = system.actorOf(Delegator.props(delegator1))
    delegator ! "DoSomeJob"
    expectMsg("Don't disturb me with DoSomeJob! I'm doing only very important job on node 555!")
    expectNoMsg()
  }
}
