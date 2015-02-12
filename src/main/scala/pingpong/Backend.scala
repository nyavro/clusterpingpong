package pingpong

import akka.actor._
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberUp}
import akka.cluster.{Cluster, Member, MemberStatus}
import com.typesafe.config.ConfigFactory


class Backend(actor:ActorRef) extends Actor {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case state: CurrentClusterState =>
      state.members.filter(_.status == MemberStatus.Up) foreach register
    case MemberUp(m) => register(m)
    case any:Any => actor forward any
  }

  def register(member: Member): Unit =
    if (member.hasRole("frontend"))
      context.actorSelection(RootActorPath(member.address) / "user" / "frontend") ! "RegisterBackend"
}
/**
 * Created by eny on 12.02.15
 */
object Backend {
  def main (args: Array[String]):Unit = {
    val port = if(args.isEmpty) "0" else args(0)
    val node = if(args.length < 2) 0 else args(1).toInt
    val config = ConfigFactory
      .parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString("akka.cluster.roles=[backend]"))
      .withFallback(ConfigFactory.load())
    val system = ActorSystem("ClusterSystem", config)
    val executor: ActorRef = system.actorOf(VeryImportantActor.props(node))
    system.actorOf(Props(classOf[Backend], executor))
  }
}
