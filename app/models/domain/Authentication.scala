package models.domain

import play.api.libs.json._

object Authentication {
  implicit def implAuthentication = Json.format[Authentication]
}

case class Authentication(username: String, password: String)
