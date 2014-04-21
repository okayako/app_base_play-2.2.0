/**
 * Copyright 2012 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package service

import play.api.mvc.{ RequestHeader, Request }
import play.api.templates.{ Html, Txt }
import play.api.{ Logger, Plugin, Application }
import securesocial.core.{ Identity, SecuredRequest, SocialUser }
import play.api.data.Form
import securesocial.controllers.Registration.RegistrationInfo
import securesocial.controllers.PasswordChange.ChangeInfo
import securesocial.controllers.TemplatesPlugin
import controllers.Admin.ChangeUserNameInfo
import play.api.data._
import play.api.data.Forms._

/**
 * The default views plugin.  If you need to customise the views just create a new plugin that
 * extends TemplatesPlugin and register it in the play.plugins file instead of this one.
 *
 * @param application
 */

class CustomViewsPlugin(application: Application) extends TemplatesPlugin {
  override def getLoginPage[A](implicit request: Request[A], form: Form[(String, String)],
    msg: Option[String] = None): Html =
    {
      views.html.SecureSocialViews.login(form, msg)
    }
  override def getSignUpPage[A](implicit request: Request[A], form: Form[RegistrationInfo], token: String): Html = {
    //    views.html.SecureSocialViews.Registration.signUp(form, token)
    views.html.developing("getSignUpPage")
  }

  override def getStartSignUpPage[A](implicit request: Request[A], form: Form[String]): Html = {
    //    views.html.SecureSocialViews.Registration.startSignUp(form)
    views.html.developing("getSignUpPage")
  }

  override def getStartResetPasswordPage[A](implicit request: Request[A], form: Form[String]): Html = {
    views.html.SecureSocialViews.Registration.startResetPassword(form)
//    views.html.developing("getSignUpPage")
  }

  def getResetPasswordPage[A](implicit request: Request[A], form: Form[(String, String)], token: String): Html = {
    views.html.SecureSocialViews.Registration.resetPasswordPage(form, token)
//    views.html.developing("getSignUpPage")
  }

  def getPasswordChangePage[A](implicit request: SecuredRequest[A], form: Form[ChangeInfo]): Html = {
    val form2 = Form(
      mapping(
      "fullName" -> text
      )(ChangeUserNameInfo.apply)(ChangeUserNameInfo.unapply)
    )
    val form3 = form2.fill(ChangeUserNameInfo(request.user.fullName.toString()))
    views.html.SecureSocialViews.passwordChange(form, form3)
    
//    views.html.developing("getSignUpPage")
  }

  def getNotAuthorizedPage[A](implicit request: Request[A]): Html = {
//    views.html.SecureSocialViews.notAuthorized()
    views.html.developing("getSignUpPage")
  }

  def getSignUpEmail(token: String)(implicit request: RequestHeader): (Option[Txt], Option[Html]) = {
    (None, Some(views.html.SecureSocialViews.mails.signUpEmail(token)))
  }

  def getAlreadyRegisteredEmail(user: Identity)(implicit request: RequestHeader): (Option[Txt], Option[Html]) = {
    (None, Some(views.html.SecureSocialViews.mails.alreadyRegisteredEmail(user)))
  }

  def getWelcomeEmail(user: Identity)(implicit request: RequestHeader): (Option[Txt], Option[Html]) = {
    (None, Some(views.html.SecureSocialViews.mails.welcomeEmail(user)))
  }

  def getUnknownEmailNotice()(implicit request: RequestHeader): (Option[Txt], Option[Html]) = {
    (None, Some(views.html.SecureSocialViews.mails.unknownEmailNotice(request)))
  }

  def getSendPasswordResetEmail(user: Identity, token: String)(implicit request: RequestHeader): (Option[Txt], Option[Html]) = {
    (None, Some(views.html.SecureSocialViews.mails.passwordResetEmail(user, token)))
  }

  def getPasswordChangedNoticeEmail(user: Identity)(implicit request: RequestHeader): (Option[Txt], Option[Html]) = {
    (None, Some(views.html.SecureSocialViews.mails.passwordChangedNotice(user)))
  }
}