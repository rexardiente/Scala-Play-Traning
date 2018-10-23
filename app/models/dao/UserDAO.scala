package models.dao

import java.util.UUID
import java.time.Instant
import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain._

@Singleton
final class UserDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider
  ) extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import profile.api._

  protected class UserTable(tag: Tag) extends Table[User](tag, "User") {
    def id        = column[UUID]   ("ID", O.PrimaryKey)
    def firstname = column[String] ("FIRSTNAME")
    def lastname  = column[String] ("LASTNAME")
    def age       = column[Int]    ("AGE")
    def username  = column[String] ("USER_NAME")
    def password  = column[String] ("PASSWORD")
    def createdAt = column[Instant]("CREATED_AT")

   def * = (id, firstname, lastname, age, username, password, createdAt) <> (
      (User.apply _).tupled, User.unapply)
  }

  object Query extends TableQuery(new UserTable(_)) {
    def apply(id: UUID) = this.withFilter(_.id === id)
  }
}
