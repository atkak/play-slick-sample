package controllers

import dao.{DaoComponentDef, Users}
import models.User
import org.junit.runner._
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.db.slick.Database
import play.api.libs.json.Json._
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification with Mockito {
  isolated

  val Application = new Application with FakeDaoComponent

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "2 users" >> {
      val users = List(User(1, "aaa"), User(2, "bbb"))
      Application.Users.findAll(any) returns users

      "render the json which is list of 2 users" in new WithApplication {
        val result = Application.index(FakeRequest(GET, "/"))

        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "application/json")
        contentAsString(result) must equalTo(stringify(toJson(users)))
      }
    }

    "no users" >> {
      val users = List.empty[User]
      Application.Users.findAll(any) returns users

      "render the json which is empty list" in new WithApplication {
        val result = Application.index(FakeRequest(GET, "/"))

        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "application/json")
        contentAsString(result) must equalTo(stringify(toJson(users)))
      }
    }
  }

  trait FakeDaoComponent extends DaoComponentDef {
    val db = mock[Database]
    val Users = mock[Users]
  }
}

