package shrine.butler

import com.meilisearch.sdk.{Client, Config}
import shrine.butler.tasks.Movie
import upickle.default._

object MeiliHelper {
  private val MasterKey = "QS9dC06x2pnSTmNsmF_6NfhZCoORuMiJyLNeAZhP9AQ"
  private val client = new Client(new Config("http://localhost:7700", MasterKey))
  val index = client.index("movie")

  def main(args: Array[String]): Unit = {
    val items = Seq(
      Movie("1", "Carol", Seq("Romance", "Drama")),
      Movie("2", "Wonder Woman", Seq("Action", "Adventure")),
      Movie("3", "Life of Pi", Seq("Adventure", "Drama")),
      Movie("4", "Mad Max: Fury Road", Seq("Adventure", "Science Fiction")),
      Movie("5", "Moana", Seq("Fantasy", "Action")),
      Movie("6", "Philadelphia", Seq("Drama"))
    )
    val json = write(items)
    index.addDocuments(json)
  }
}
