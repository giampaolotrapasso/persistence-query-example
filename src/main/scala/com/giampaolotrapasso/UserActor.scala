package com.giampaolotrapasso

import akka.actor.Actor.Receive
import akka.actor.Props
import akka.persistence.PersistentActor
import com.giampaolotrapasso.UserActor.{MailUpdated, ChangeMail, AddUser, UserAdded, User}
import com.typesafe.scalalogging.LazyLogging

class UserActor(userName: String) extends PersistentActor with LazyLogging {

  override def persistenceId = s"user-$userName"

  var state: User = User(userName, "", 0)

  override def receiveCommand: Receive = {
    case AddUser(u, mail, age) =>
      persist(UserAdded(u, mail, age)) { event =>
        logger.warn(s"persisted $event")
        state = User(u, mail, age)
      }
    case ChangeMail(u, newMail) =>
      persist(MailUpdated(u, newMail)) { event =>
        logger.warn(s"persisted $event")
        state = state.copy(mail = newMail)
      }
  }

  override def receiveRecover: Receive = {
    case _ =>
  }

}

object UserActor {

  def props(userName: String) = Props(new UserActor(userName))

  case class User(userName: String, mail: String, age: Int)

  trait Command

  case class AddUser(userName: String, mail: String, age: Int) extends Command
  case class ChangeMail(userName: String, newMail: String)     extends Command

  trait Event
  case class UserAdded(userName: String, mail: String, age: Int) extends Event
  case class MailUpdated(userName: String, newMail: String)      extends Event
}
