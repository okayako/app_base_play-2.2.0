package models

import play.libs.Scala
import be.objectify.deadbolt.core.models.Subject
import scalaz._
import Scalaz._

/**
 *
 * @author Steve Chaloner (steve@objectify.be)
 */

class User(val userName: String) extends Subject
{
  def getRoles: java.util.List[SecurityRole] = {
    if ((userName === "jbarrios@okayako.com")){
      Scala.asJava(List(new SecurityRole(OkayakoRole.ADMIN),
        new SecurityRole(OkayakoRole.EMPLOYEE)))
    }
    else {
      Scala.asJava(List(new SecurityRole(OkayakoRole.GUEST)))
    }
  }

  def getPermissions: java.util.List[UserPermission] = {
    Scala.asJava(List(new UserPermission("printers.edit")))
  }

  def getIdentifier: String = userName
}
