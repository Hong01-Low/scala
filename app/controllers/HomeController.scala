package controllers

import models.Sample
import SampleForm.{Data, form}
import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.mvc.{Action, _}

import scala.collection.mutable
import scala.reflect.internal.NoPhase.id

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  private val samples = mutable.ArrayBuffer(
    Sample("Low Chun Hong", 1001, 1000, "Puchong")
  )

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def listSample(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.listSample(samples.toSeq))
  }

  def createSample(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.createSample(form))
  }

  def editSample(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.editSample(form))
  }

  val form = Form(
    mapping(
      "fullName" -> nonEmptyText,
      "empCode" -> number(min = 4),
      "salary" -> number(min = 0),
      "city" -> nonEmptyText
    )(Sample.apply)(Sample.unapply)
  )

  def save = Action { implicit request: Request[AnyContent] =>
    form.bindFromRequest().fold(
      hasErrors = { form =>
        println("error: " + form)
        Redirect(routes.HomeController.createSample)
      },
      success = { data =>
        samples.find(_.empCode == data.empCode) match {
          case Some(t) =>
            Redirect(routes.HomeController.createSample).flashing("ERROR" -> "Duplicate Emp code")
          case None =>
            samples.find(_.fullName == data.fullName) match{
              case Some (a) =>
                Redirect(routes.HomeController.createSample).flashing("ERROR" -> "Duplicate Name")
              case None =>
                samples += Sample(data.fullName, data.empCode, data.salary, data.city)
                Redirect(routes.HomeController.listSample).flashing("SUCCESS" -> "Data created successfully")
            }
        }
      }
    )
  }

  def edit(code: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val editData = samples.find(_.empCode == code)
    println(editData)
    editData match {
      case Some(a) =>
        println(a)
        Redirect(routes.HomeController.listSample).flashing("ERROR" -> "Not Match")
      case None =>
        Redirect(routes.HomeController.editSample).flashing("SUCCESS" -> "Match")
    }
  }

  def delete(code: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val del = samples.find(_.empCode == code)
    println(del)
    del.map(d => samples.remove(samples.indexOf(d)))
    Redirect(routes.HomeController.listSample)
  }
}