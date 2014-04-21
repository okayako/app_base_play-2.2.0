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
import play.api.data.Form
import play.api.data._
import play.api.data.Forms._

object Admin extends Controller with SecureSocial {

  case class ChangeUserNameInfo(fullName: String)

  val changeUserNameInfoForm = Form(
    mapping(
      "fullName" -> text)(ChangeUserNameInfo.apply)(ChangeUserNameInfo.unapply))

  case class WithRole(role: String) extends Authorization {
    def isAuthorized(user: Identity) = {
      val okayakoUser = OkayakoUser.toOkayakoUser(user)
      okayakoUser.roles.contains(role)
    }
  }

  def changeUserName = SecuredAction(WithRole("GUEST")) { implicit request =>
    val changeUserNameInfoFormData = changeUserNameInfoForm.bindFromRequest.get
    val u = UserService.save(SocialUser(request.user).copy(fullName = changeUserNameInfoFormData.fullName.toString()))
    Ok(views.html.index("Restricted", request.user))
  }

}