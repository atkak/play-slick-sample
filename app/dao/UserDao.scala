package dao

import models.User
import play.api.db.slick.Profile

import scala.slick.driver.JdbcProfile
import scala.slick.jdbc.JdbcBackend


trait Users {
  def createTable(implicit session: JdbcBackend#Session)
  def dropTable(implicit session: JdbcBackend#Session)
  def findAll(implicit session: JdbcBackend#Session): List[User]
  def createUser(user: User)(implicit session: JdbcBackend#Session)
}

class UsersImpl(val profile: JdbcProfile) extends Users with UserComponent with Profile {
  import profile.simple._

  val users = scala.slick.lifted.TableQuery[UserTable]

  def createTable(implicit session: JdbcBackend#Session) = users.ddl.create
  def dropTable(implicit session: JdbcBackend#Session) = users.ddl.drop
  def findAll(implicit session: JdbcBackend#Session) = users.list
  def createUser(user: User)(implicit session: JdbcBackend#Session) = users.insert(user)
}

trait UserComponent { this: Profile =>
  import profile.simple._

  class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.NotNull)
    def * = (id, name) <> (User.tupled, User.unapply)
  }
}
