package com.example

import com.example.message._
import akka.actor.Actor
import akka.actor.Identify
import scala.concurrent.duration._
import akka.actor.ActorIdentity
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.OneForOneStrategy
import akka.actor.AllForOneStrategy
import akka.actor.SupervisorStrategy._

/**
 * 記者
 */
class Reporter() extends Actor {
  
  override val supervisorStrategy = 
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    //AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _ : DontBotherMeException => Resume
    case _ : ExplodeException => Restart
    case _ : IamGodException => Stop
    case _ : Exception => Escalate
    }
  
  val penguins = new Array[ActorRef](10)
  
  sendIdentifyRequest()
  
  def sendIdentifyRequest() {
    for (i <- 0 to 8) {
      val actor = context.actorOf(Props(classOf[Penguin], s"Penguin-$i"))
      actor ! Identify(actor.path.toString())
      penguins(i) = actor
    }
      
    val dong = context.actorOf(Props[DongDong])
    dong ! Identify(dong.path.toString())
    penguins(9) = dong
    println(dong.path)
    
    import context.dispatcher
    context.setReceiveTimeout(5 seconds) // 設定 timeout 5 seconds
  }
  
  
  var count = 0
  def receive = {
    case ActorIdentity(path, Some(actor)) =>
      count += 1
      if (count == penguins.length) {
        context.setReceiveTimeout(Duration.Undefined)
      }
      
      println(s"$path found")
      actor ! Interest
      
    /* 有三個興趣的回覆 */
    case Three(name, a, b, c) => 
      println(s"$name: $a, $b, $c")
      
    
    /* 只有二個興趣的回覆，反問 why */      
    case Two(name, a, b) =>
      println(s"$name: $a, $b")
      sender() ! Why()
    
    /* 接到 why 的回覆 */
    case Because(name, msg) =>
      println(s"$name: $msg")
      
    case Iam(name) =>
      println(s"$name: I am $name")
      
    case DontHitMe(name, hit) =>
      println(s"$name: Do Hit Me, I had been $hit times")
      
    case ResponseHit(name, hit) =>
      println(s"$name: I had been hit $hit times")
      
    case HitDong =>
      penguins(9) ! Hit
      penguins(9) ! QueryHit
      
    case ex: Exception => 
      println(">" + ex.getMessage())
  }
  
  override def preStart() = {
    println("Reporter pre-start")
  }
  
  override def postStop() = {
    println("Reporter post-start")
  }
}