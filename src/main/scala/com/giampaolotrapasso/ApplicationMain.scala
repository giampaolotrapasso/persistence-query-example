package com.giampaolotrapasso

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.serialization.Serialization
import akka.util.Timeout
import com.giampaolotrapasso.QueryByMailActor.Response

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object ApplicationMain extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  //implicit val materializer = ActorMaterializer()
  implicit val serialization = Serialization
  implicit val timeout       = Timeout(3.seconds)
  val system                 = ActorSystem("People")

  val queryActor = system.actorOf(Props(classOf[QueryByMailActor]))

  val user1 = system.actorOf(UserActor.props("user1"), "user1")
  val user2 = system.actorOf(UserActor.props("user2"), "user2")
  val user3 = system.actorOf(UserActor.props("user3"), "user3")
  val user4 = system.actorOf(UserActor.props("user4"), "user4")

  user1 ! UserActor.AddUser("user1", "alice@gmail.com", 20)
  user2 ! UserActor.AddUser("user2", "bob@yahoo.com", 24)
  user3 ! UserActor.AddUser("user3", "charlie@live.com", 23)
  user4 ! UserActor.AddUser("user4", "don@gmail.com", 32)

  Thread.sleep(3000)

  val f = (queryActor ? QueryByMailActor.UsersByMail("gmail.com")).mapTo[Response]

  f onComplete {
    case Success(response) => {
      println(s"User with domain ${response.domain}")
      for (user <- response.mails) println(user)
    }
    case Failure(t) => println("An error has occured: " + t.getMessage)
  }

  Await.result(f, 4.seconds)
  system.terminate()
}
