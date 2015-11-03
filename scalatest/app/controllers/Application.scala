package controllers

import javax.inject.Inject

import dao.UserDAO
import graphQL.SchemaDefinition
import graphQL.SchemaDefinition.UserRepo
import models.User
import play.api.data.{Forms, Form}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._
import sangria.parser.QueryParser
import sangria.execution.Executor
import play.api.libs.json._
import sangria.integration.playJson._

import scala.util.{Failure, Success}


class Application @Inject() (userDAO: UserDAO) extends Controller {

  /**
    * User 一覧ページ（トップ)
    * @return
    */
  def index = Action.async {
    userDAO.all.map { case us => Ok(views.html.index(us)) }
  }

  /**
    * ユーザ情報フォーム
    */
  val userForm = Form {
    Forms.mapping(
      "id" -> Forms.optional(Forms.longNumber()),
      "name" -> Forms.text(),
      "gender" -> Forms.optional(Forms.text())
    )(User.apply)(User.unapply)
  }

  /**
    * ユーザ情報Post先
    * @return
    */
  def insertUser = Action.async { implicit request =>
    println(userForm.bindFromRequest)
    val user: User = userForm.bindFromRequest.get
    userDAO.insert(user).map(_ => Redirect(routes.Application.index))
  }

  /**
    * GraphQL用Executor
    */
  val executor = Executor(
    schema = SchemaDefinition.UserSchema,
    userContext = new UserRepo(userDAO),
    maxQueryDepth = Some(7))

  /**
    * Get用 GraphQL Action
    * @param query
    * @param variables
    * @param operation
    * @return
    */
  def graphql(query: String, variables: Option[String], operation: Option[String]) =
    Action.async(executeQuery(query, variables, operation))

  /**
    * Post用 GraphQL Action
    * @return
    */
  def graphqlBody = Action.async(parse.json) { request =>
    val query = (request.body \ "query").as[String]
    val variables = (request.body \ "variables").asOpt[String]
    val operation = (request.body \ "operation").asOpt[String]

    executeQuery(query, variables, operation)
  }

  /**
    * GraphQL Query実行
    * @param query
    * @param variables
    * @param operation
    * @return
    */
  private def executeQuery(query: String, variables: Option[String], operation: Option[String]) =
    QueryParser.parse(query) match {
      case Success(queryAst) =>
        executor.execute(queryAst, operationName = operation, variables = variables map Json.parse getOrElse Json.obj()) map (Ok(_))
      case Failure(error) => throw error
    }

}
