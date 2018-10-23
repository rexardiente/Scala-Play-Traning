package models.domain

import java.util.UUID
import java.time.Instant
import play.api.libs.json._

object User {
  implicit def implUser = Json.format[User]
}

case class User(
    id       : UUID,
    firstname: String,
    lastname : String,
    age      : Int,
    username : String,
    password : String,
    createdAt: Instant // User date added..
  ) {
  def toJson = Json.toJson(this)
}
