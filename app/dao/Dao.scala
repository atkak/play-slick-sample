package dao

import play.api.db.slick.{Database, DB}

trait DaoComponentDef {
  val db: Database
  val Users: Users
}

trait DaoComponent extends DaoComponentDef {
  val db = DB(play.api.Play.current)
  val Users = new UsersImpl(db.driver)
}
