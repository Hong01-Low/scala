package controllers

object SampleForm {

  import play.api.data.Form
  import play.api.data.Forms._

  case class Data(fullName: String, empCode: Int, salary: Int, city: String)

  val form = Form(
    mapping(
      "fullName" -> nonEmptyText,
      "empCode" -> number(min = 0),
      "salary" -> number(min = 0),
      "city" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )
}
