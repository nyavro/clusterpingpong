package pingpong

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

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
    system.actorOf(VeryImportantActor.props(node))
  }
}
