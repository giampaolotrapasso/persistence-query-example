package com.giampaolotrapasso

import akka.actor.Actor
import akka.persistence.inmemory.query.journal.scaladsl.InMemoryReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.ActorMaterializer
import com.giampaolotrapasso.QueryByMailActor.{Response, UsersByMail}
import com.giampaolotrapasso.UserActor.{UserAdded, User}
import com.typesafe.scalalogging.LazyLogging

class QueryByMailActor extends Actor with LazyLogging {

  val readJournal =
    PersistenceQuery(context.system).readJournalFor[InMemoryReadJournal](InMemoryReadJournal.Identifier)

  var usersByDomain: Map[String, List[String]] = Map.empty

  val min = 0
  val max = 1000

  implicit val materializer = ActorMaterializer()

  readJournal.allPersistenceIds().runForeach { persistenceId =>
    readJournal.eventsByPersistenceId(persistenceId, min, max).runForeach { eventEnvelope =>
      eventEnvelope.event match {
        case event @ UserAdded(user, mail, age) =>
          logger.info(s"received $event for $persistenceId")
          val domain = mail.split("@").last
          logger.info("Current domain " + domain)
          val list: List[String] = usersByDomain.getOrElse(domain, List.empty[String])
          logger.info("Current list " + list)
          val newList: List[String] = list :+ mail
          usersByDomain = usersByDomain + (domain -> newList)
      }
    }

  }

  override def receive = {
    case UsersByMail(domain) => {
      sender() ! Response(domain, usersByDomain(domain))
    }
  }

}

object QueryByMailActor {

  case class UsersByMail(mail: String)
  case class Response(domain: String, mails: List[String])
}
