package models.repo

import java.util.UUID
import javax.inject.{ Inject, Singleton }
import scala.concurrent.Future
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.User

@Singleton
class UserRepo @Inject()(
    dao: models.dao.UserDAO,
    protected val dbConfigProvider: DatabaseConfigProvider
  ) extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import profile.api._

  def exist(id: UUID): Future[Boolean] = db.run(dao.Query(id).exists.result)

  def exist(user: String, pass: String): Future[Option[User]] =
    db
      .run(dao.Query.filter(r => r.username === user && r.password === pass)
      .result
      .headOption)

  def all(): Future[Seq[User]]         = db.run(dao.Query.result)

  def add(user: User): Future[Int]     = db.run(dao.Query += user)

  def delete(id: UUID): Future[Int]    = db.run(dao.Query(id).delete)

  def update(user: User): Future[Int]  = db.run(dao.Query.filter(_.id === user.id).update(user))
}
