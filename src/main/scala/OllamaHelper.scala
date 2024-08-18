package shrine.butler

import io.github.ollama4j.OllamaAPI
import io.github.ollama4j.models.chat.{OllamaChatMessage, OllamaChatMessageRole, OllamaChatRequest, OllamaChatRequestBuilder, OllamaChatResult}

import scala.collection.mutable
import scala.jdk.CollectionConverters.ListHasAsScala
import scala.sys.process._
import scala.util.matching.Regex

object OllamaHelper {
  private val host = "http://localhost:11434/"
  private val ollamaAPI = new OllamaAPI(host)
  ollamaAPI.setRequestTimeoutSeconds(20)
  private val Model = "wangshenzhi/llama3-8b-chinese-chat-ollama-q4"

  private val prompt =
    """你是星际拓荒主题威士忌酒吧的智能管家，酒吧名字叫Shrine，取自星际拓荒Outerwilds游戏内挪麦人种族在
      |量子卫星上建立的量子祭坛，你的任务是给主理人Spike提供一系列服务，包含聊天、记账、显示收付款支付码、调节室内空调温度
      |吧台一共有6个椅子，分别称呼它们为木炉星(Timber Hearth)、深巨星(Giants Deep)、黑棘星(Dark Bramble)、余烬双星(Hourglass Twins)、碎空星(Brittle Hollow)、量子卫星(Quantum Moon)
      |在我点单与记账时称呼它们的中文或者英文名时，请正确识别
      |每次在我点单与记账时，请按 座位号-账单-总金额 为我生成一个6行3列的表格
      |举个例子，木炉星-390-格兰花格12*1(80)，格兰菲迪12*1(70)，格兰朵那18*2(240)
      |这里的意思就是格兰花格12年威士忌1杯80元，格兰菲迪12年威士忌1杯70元，格兰朵那18年威士忌2杯240元
      |""".stripMargin

  private val chatBuilder = OllamaChatRequestBuilder.getInstance(Model)
  private var chatHistory: mutable.Buffer[OllamaChatMessage] = mutable.Buffer.empty

  private val requestModel: OllamaChatRequest = chatBuilder
    .withMessage(OllamaChatMessageRole.USER, prompt)
    .build()
  private val chatResult: OllamaChatResult = ollamaAPI.chat(requestModel)

  def whisper(file:String): String ={
    val whisperCmd = s"whisper --fp16 False --model small $file"
    val whisperRes = whisperCmd.!!
    println(whisperRes)
    val pattern: Regex = """\[\d{2}:\d{2}\.\d{3} --> \d{2}:\d{2}\.\d{3}\] (.*?)\n""".r
    // 提取所有匹配的内容
    val matches = pattern.findAllIn(whisperRes).matchData.map(_.group(1)).toList
    // 合并所有提取的内容
    val chatMessage = matches.mkString
    println(chatMessage)
    val response = ollamaChat(chatMessage).getResponse
    println(response)
    response
  }

  def ollamaChat(chat:String): OllamaChatResult = {
    val chatRequest = chatBuilder
      .withMessages(chatResult.getChatHistory)
      .withMessage(OllamaChatMessageRole.USER, chat)
      .build()
    val ollamaChatResult = ollamaAPI.chat(chatRequest)
    chatHistory = ollamaChatResult.getChatHistory.asScala
    println(chatHistory.mkString("\n"))
    ollamaChatResult
  }

}
