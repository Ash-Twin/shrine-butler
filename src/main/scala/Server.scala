package shrine.butler

import cask.model.FormFile

object Server extends cask.MainRoutes{

  override def port: Int = 8080
  override def host: String = "0.0.0.0"

  @cask.get("/")
  def hello(): Unit = {
    "server running"
  }

  @cask.postJson("/chat")
  def doThing(value1: ujson.Value, value2: Seq[Int]) = {
    "OK " + value1 + " " + value2
  }

  @cask.postForm("/speak")
  def speak(voiceFile:cask.FormFile):String = {
    val mp3File = s"${voiceFile.filePath}"
    os.move(os.Path(mp3File),os.Path(s"${os.pwd}/speak.mp3"),replaceExisting = true)
    OllamaHelper.whisper(s"${os.pwd}/speak.mp3")
  }

  println("server running")
  initialize()
}