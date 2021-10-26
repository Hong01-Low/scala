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
    Sample("Low Chun Hong", 1001, 1000, "Puchong"),
    Sample("Soh Kah Lok", 1002, 1500, "Klang"),
    Sample("Shantini", 1003, 1200, "Kepong")
  )

  private val postUpdate = routes.HomeController.editSample

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def listSample(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.listSample(samples.toSeq))
  }

  def createSample(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.createSample())
  }

  def editSample(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.editSample())
  }

  val form = Form(
    mapping(
      "fullName" -> nonEmptyText,
      "empCode" -> number(min = 0),
      "salary" -> number(min = 0),
      "city" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

  def save = Action { implicit request: Request[AnyContent] =>
    form.bindFromRequest().fold(
      hasErrors = { form =>
        println("error: " + form)
        Redirect(routes.HomeController.createSample)
      },
      success = { data =>
        samples += Sample(data.fullName, data.empCode, data.salary, data.city)
        Redirect(routes.HomeController.listSample)
      }
    )
  }

  def edit(code: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    println(code)
    Redirect(routes.HomeController.editSample)
  }
}