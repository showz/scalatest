package dao

import com.google.inject.Inject
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import models.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
 * Created by showz on 2015/11/02.
 */
class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Users = TableQuery[UsersTable]

  /**
    * すべて取得
    * @return
    */
  def all(): Future[Seq[User]] = db.run(Users.result)

  /**
    * ID指定での取得
    * @param id
    * @return
    */
  def findById(id: Long): Future[User] = db.run(Users.filter(_.id === id).result.head)

  /**
    * Insert
    * @param user
    * @return
    */
  def insert(user: User): Future[Unit] = db.run(Users += user).map { _ => () }

  private class UsersTable(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Option[Long]]("id", O.PrimaryKey)
    def name = column[String]("name")
    def gender = column[Option[String]]("gender")

    def * = (id, name, gender) <> (User.tupled, User.unapply _)
  }
}
