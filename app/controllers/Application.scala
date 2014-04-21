package controllers

import play.api._
import play.api.mvc._
import security.MyDeadboltHandler
import be.objectify.deadbolt.scala.DeadboltActions
import securesocial.core.SecureSocial
import securesocial.core.{ Identity, Authorization }
import models.User
import models.OkayakoUser
import play.libs.Akka
import securesocial.core.UserService
import securesocial.core.SocialUser
import securesocial.core.Registry
import securesocial.core.Identity
//
import akka.actor.Props
import akka.actor.ActorSystem
import service._

object Application extends Controller with SecureSocial {

  def index = Action {
    Ok(views.html.index("Home", null))
  }

  def index2 = SecuredAction (Admin.WithRole("EMPLOYEE")) { implicit request =>
    Ok(views.html.index("Restricted", request.user))
    
  }
}