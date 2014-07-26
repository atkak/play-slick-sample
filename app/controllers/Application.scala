package controllers

import dao.{DaoComponentDef, DaoComponent}
import play.api._
import play.api.mvc._
import play.api.db.slick._
import play.api.libs.json.Json._

trait Application extends Controller { this: DaoComponentDef =>
  import play.api.Play.current

  def index = DBAction { implicit rs =>
    val users = Users.findAll
    Ok(toJson(users))
  }

  def dbActionWithTransaction = DBAction { implicit rs =>
    rs.dbSession.withTransaction {
      // ...
    }
    rs.dbSession.withTransaction {
      // ...
    }
    Ok
  }

  def actionWithSession = Action { implicit request =>
    db.withSession { implicit session =>
      // ...
    }
    Ok
  }

}

object Application extends Application with DaoComponent