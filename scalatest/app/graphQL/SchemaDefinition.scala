package graphQL

import com.google.inject.{ImplementedBy, Inject}
import dao.UserDAO
import sangria.schema._
import models.User

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Success

/**
  * Created by showz on 2015/11/03.
  */
object SchemaDefinition {
  val User = ObjectType("User", "User", fields[Unit, User](
    Field("id", OptionType(LongType), Some("User.id"), resolve = _.value.id),
    Field("name", OptionType(StringType), Some("User.name"), resolve = _.value.name),
    Field("gender", OptionType(StringType), Some("User.gender"), resolve = _.value.gender)
  ))

  class UserRepo @Inject() (userDAO: UserDAO) {
    def getUser(id: Long): Option[User] = {
      val result = Await.ready(userDAO.findById(id), Duration.Inf)
      result.value.get match {
        case Success(user) => Some(user)
        case _ => None
      }
    }
  }

  val ID = Argument("id", LongType, description = "id of the character")

  val Query = ObjectType("Query", fields[UserRepo, Unit](
    Field("User", OptionType(User), arguments = ID :: Nil, resolve = ctx => ctx.ctx.getUser(ctx arg ID))
  ))

  val UserSchema = Schema(Query)
}
