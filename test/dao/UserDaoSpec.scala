package dao

import models.User
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.db.slick.DB
import play.api.test._

import scala.slick.jdbc.meta.MTable

@RunWith(classOf[JUnitRunner])
class UserDaoSpec extends Specification {
  sequential

  "UsersImpl" should {

    "#findAll" >> {

      "no users" >> {
        "fetches list of users" in new WithApplication with UserTableSetup {
          db.withSession { implicit session =>
            val users = Users.findAll

            users must equalTo(List.empty[User])
          }
        }
      }

      "two users" >> {
        val users = Seq(User(1, "aaa"), User(2, "bbb"))

        "fetches list of users" in new WithApplication with UserTableSetup {

          override def before = {
            super.before

            db.withTransaction { implicit session =>
              users foreach Users.createUser
            }
          }

          db.withSession { implicit session =>
            val users = Users.findAll

            users must equalTo(users)
          }
        }
      }

    }

  }

  trait UserTableSetup extends BeforeAfter { this: WithApplication =>
    val db = DB("unittest")
    val Users = new UsersImpl(db.driver)

    def before = db withSession { implicit session =>
      if (MTable.getTables("users").list().isEmpty) {
        Users.createTable
      }
    }

    def after = db withSession { implicit session =>
      if (MTable.getTables("users").list().nonEmpty) {
        Users.dropTable
      }
    }
  }

}
