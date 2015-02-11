package pingpong

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest._

class VeryImportantActorTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll{

  def this() = this(ActorSystem("PostponeSpec"))

  override def afterAll() = system.shutdown()

  "Does very important job on node 113" in {
    val actor = system.actorOf(VeryImportantActor.props(113))
    actor ! "DoVeryImportantJob"
    expectMsg("Very important job is done on node 113")
    expectNoMsg()
  }

  "Rejects requests other than VeryImportant" in {
    val actor = system.actorOf(VeryImportantActor.props(123))
    actor ! "DoSomeJob"
    expectMsg("Don't disturb me with DoSomeJob! I'm doing only very important job on node 123!")
    expectNoMsg()
  }
}
