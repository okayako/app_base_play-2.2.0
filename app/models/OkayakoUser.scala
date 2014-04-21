package models

/**
 * Created with IntelliJ IDEA.
 * User: pawanacelr
 * Date: 17/09/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */

import securesocial.core._
import securesocial.core.providers._
import scala.Some

//Play Salat
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection

import play.libs.Scala
import play.api.Play.current
import org.bson.types.ObjectId

import org.scala_tools.time.Imports._

import be.objectify.deadbolt.core.models.Subject

import mongoContext._

object OkayakoRole {
  val ADMIN = "ADMIN"
  val GUEST = "GUEST"
  val EMPLOYEE = "EMPLOYEE"
  val ANNONYMOUS = "ANNONYMOUS"

  def isAdminRole(roles:Seq[String]):Boolean = {
    import scalaz._
    import Scalaz._
    roles.filter(a => a === OkayakoRole.ADMIN).size > 0
  }
}

case class OkayakoRole(titles:Seq[String])

case class OkayakoUser(id: ObjectId = new ObjectId,fullName:String, email:String, identity:IdentityId, method:AuthenticationMethod, roles:Seq[String] = Seq(OkayakoRole.GUEST), createdOn:DateTime = DateTime.now, passwordInfo:Option[PasswordInfo] = None)
//@Salat
//case class AcelrUser(id: ObjectId = new ObjectId,fullName:String, email:String, identity:IdentityId, socialUser:Option[Identity], createdOn:DateTime = DateTime.now)

object OkayakoUser {
  com.mongodb.casbah.commons.conversions.scala.RegisterConversionHelpers()
  com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers()

  val collection = MongoConnection()("app-base")("users")
  val dao = new SalatDAO[OkayakoUser, ObjectId](collection = collection) {}

  val adminEmailList:List[String] = List("jbarrios@okayako.com")
  def isAdmin(email:String):Boolean = {
    import scalaz._
    import Scalaz._
    adminEmailList.filter(a => a === email).size > 0
  }
  def toOkayakoUser(socialuser:Identity):OkayakoUser = {
    val roles = if (isAdmin(socialuser.email.get)) Seq(OkayakoRole.ADMIN,OkayakoRole.EMPLOYEE) else Seq(OkayakoRole.GUEST)
    OkayakoUser(fullName = socialuser.fullName,email = socialuser.email.get,identity = socialuser.identityId, method = socialuser.authMethod, roles = roles, passwordInfo = socialuser.passwordInfo)
  }


  def toIdentity(user:OkayakoUser):Identity = SocialUser(identityId = user.identity,firstName = user.fullName,
    lastName = user.fullName,
    fullName = user.fullName,
    email = Some(user.email),
    authMethod = user.method, avatarUrl = None, passwordInfo = user.passwordInfo)

  def find(identity:IdentityId):Option[Identity] = {
    dao.findOne(MongoDBObject("identity.userId" -> identity.userId, "identity.providerId" -> identity.providerId)) match {
      case Some(user) =>
        Some(toIdentity(user))
      case _ =>
        None
    }
  }

  def findByEmailAndProvider(email:String,provider:String):Option[Identity] = {
    println(s"Email - $email and provider - $provider")
    dao.findOne(MongoDBObject("email" -> email, "identity.providerId" -> provider)) match {
      case Some(user) =>
        Some(toIdentity(user))
      case _ =>
        None
    }
  }

  def findUserByEmailAndProvider(email:String,provider:String):Option[OkayakoUser] = {
    println(s"Email - $email and provider - $provider")
    dao.findOne(MongoDBObject("email" -> email, "identity.providerId" -> provider))
  }
}

import org.scala_tools.time.Imports._

object TokenDAO {
  com.mongodb.casbah.commons.conversions.scala.RegisterConversionHelpers()
  com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers()

  val collection = MongoConnection()("app-base")("tokens")
  val dao = new SalatDAO[Token, String](collection = collection) {}

  def findToken(uuid:String):Option[Token] = dao.findOne(MongoDBObject("uuid" -> uuid))

  def deleteExpiredTokens() {
    val now = DateTime.now
    dao.find(MongoDBObject("expirationTime" -> MongoDBObject("$lte" -> now))).foreach(
      TokenDAO.dao.remove(_)
    )
  }
}
