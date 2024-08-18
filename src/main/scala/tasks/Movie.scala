package shrine.butler
package tasks
import upickle.default.{ReadWriter => RW, macroRW}
case class Movie(id: String, title: String, genres: Seq[String])
object Movie{
  implicit val rw: RW[Movie] = macroRW
}