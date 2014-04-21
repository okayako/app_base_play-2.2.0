import play.api.{ Logger, Application }
import play.api.GlobalSettings
import play.api.libs.concurrent.Execution.Implicits._
import play.libs.Akka
import scala.concurrent.duration._
import akka.actor.Props
import akka.actor.ActorSystem
import service._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
  }
}