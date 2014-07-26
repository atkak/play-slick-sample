import play.api.libs.json.{Json, Writes}

/**
 * Created by kakegawaatsushi on 14/07/26.
 */
package object models {
  implicit val write: Writes[User] = Json.writes[User]
}
