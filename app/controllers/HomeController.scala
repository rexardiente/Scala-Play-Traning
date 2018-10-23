package controllers

import javax.inject._
import java.util.UUID
import java.time.Instant
import java.util.Date
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import models.domain._
import models.repo._
import akka.AccountManagerActor

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
    userRepo: UserRepo,
    msg: MessagesApi,
    cc: ControllerComponents,
    implicit val system: ActorSystem,
    implicit val mat: Materializer
  ) extends AbstractController(cc) with I18nSupport {
  private def noSession: Result = Unauthorized("Oops, you are not connected")

  private def loginForm = Form(mapping(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText
  )(Authentication.apply)(Authentication.unapply))

  private def userForm = Form(tuple(
    "firstname" -> nonEmptyText,
    "lastname"  -> nonEmptyText,
    "age"       -> number,
    "username"  -> nonEmptyText,
    "password"  -> nonEmptyText,
    "createdAt" -> optional(date)
  ))

  def ws() = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef(out => AccountManagerActor.props(out))
  }

  // Rejecting a WebSocket without session
  def secureSocket = WebSocket.acceptOrResult[String, String] { request =>
    Future.successful(request.session.get("session") match {
      case None => Left(Forbidden)
      case Some(_) => Right(ActorFlow.actorRef(out => AccountManagerActor.props(out)))
    })
  }

  def index() = Action.async { implicit request =>
    Future.successful(Ok(views.html.index(loginForm)))
  }

  def signin() = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formErr  => Future.successful(BadRequest(views.html.index(formErr))),
      valid =>
        userRepo
          .exist(valid.username, valid.password)
          .map({ count =>
          if(count.size == 1)
            Redirect(routes.HomeController.dashboard).withSession("session" -> "user@gmail.com")
          else
            BadRequest(views.html.index(loginForm))
         })
    )
  }

  def dashboard() = Action.async { implicit request =>
    request.session.get("session").map { user =>
      Future.successful(Ok(views.html.dashboard(userForm)))
    }
    .getOrElse(Future.successful(noSession))
  }

  def getUsers() = Action.async { implicit request =>
    request.session.get("session").map { user =>
      userRepo.all().map(r => Ok(Json.toJson(r)))
    }
    .getOrElse(Future.successful(noSession))
  }

  def addUser() = Action.async { implicit request =>
    request.session.get("session").map { user =>
      userForm.bindFromRequest.fold(
        formErr => Future.successful(BadRequest(views.html.dashboard(userForm))),
        { case (a, b, c, d, e, f) =>
          userRepo
            .add(User(UUID.randomUUID, a, b, c, d, e, f.map(_.toInstant).getOrElse(Instant.now)))
            .map(r => if(r < 0) NotFound else Created )
        }
      )
    }
    .getOrElse(Future.successful(noSession))
  }

  def updateUser(id: UUID) = Action.async { implicit request =>
    request.session.get("session").map { user =>
      userForm.bindFromRequest.fold(
        formErr => Future.successful(BadRequest(views.html.index(loginForm))),
        { case (a, b, c, d, e, f) =>
          userRepo
            .add(User(id, a, b, c, d, e, f.map(_.toInstant).getOrElse(Instant.now)))
            .map(r => if(r < 0) NotFound else Ok)
        }
      )
    }
    .getOrElse(Future.successful(noSession))
  }

  def deleteUser(id: UUID) = Action.async { implicit request =>
    request.session.get("session").map { user =>
      userRepo
        .delete(id)
        .map(r => if(r < 0) NotFound else Ok)
    }
    .getOrElse(Future.successful(noSession))
  }

  def signout() = Action.async { implicit request =>
    Future.successful(Ok(views.html.index(loginForm)).withNewSession)
  }
}
