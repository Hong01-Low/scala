package controllers

import models.Sample
import SampleForm.{Data, form}
import play.api.data.Form

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

  private val postUrl = routes.HomeController.createSample

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def listSample(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.listSample(samples.toSeq, postUrl))
  }

  def createSample(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.createSample())
  }

  val successFunction = { data: Data =>
    // This is the good case, where the form was successfully parsed as a Data object.
    val sampleData = Sample(fullName = data.fullName, empCode = data.empCode, salary = data.salary, city = data.city)
    samples += Sample
    Redirect(routes.HomeController.listSample).flashing("note" -> "Personal Details added!")
  }
}