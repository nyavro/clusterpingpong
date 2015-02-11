package pingpong

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated, _}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class Frontend extends Actor {
  var backends = IndexedSeq.empty[ActorRef]
  var jobCounter = 0

  def receive = {
    case "DoVeryImportantJob" if backends.isEmpty =>
      sender() ! "Service unavailable, try again later"
    case "DoVeryImportantJob" =>
      jobCounter += 1
      backends(jobCounter % backends.size) forward "DoVeryImportantJob"
    case "RegisterBackend" if !backends.contains(sender()) =>
      context watch sender()
      backends = backends :+ sender()
    case Terminated(a) =>
      backends = backends.filterNot(_ == a)
  }
}

object Frontend {
  def main(args: Array[String]):Unit = {
    val port = if(args.isEmpty) "0" else args(0)
    val node = if(args.length < 2) 0 else args(1).toInt
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString(s"akka.cluster.roles=[frontend]"))
      .withFallback(ConfigFactory.load())
    val system = ActorSystem("ClusterSystem", config)
    val frontend = system.actorOf(Props[Frontend], name = "frontend")
    val counter = new AtomicInteger
    val random = new Random()
    import system.dispatcher
    def nextMsg:String = {
      if(random.nextBoolean()) "DoVeryImportantJob" else "Job-" + counter.incrementAndGet()
    }
    system.scheduler.schedule(2.seconds, 2.seconds) {
      implicit val timeout = Timeout(5.seconds)
        (frontend ? nextMsg) onSuccess {
          case result => println(result)
        }
    }
  }
}
