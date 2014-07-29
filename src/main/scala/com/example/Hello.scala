package com.example

import akka.actor.ActorSystem
import akka.actor.Props
import com.example.message._

object Hello extends App {
  
  val system = ActorSystem("Hello")
  
  val report = system.actorOf(Props[Reporter])
  
  report ! HitDong  // 1
  
  report ! HitDong  // 2
  
  report ! HitDong  // 3
  
  report ! HitDong  // 4
  
  report ! HitDong  // 5
  
  report ! HitDong  // 6
  
  report ! HitDong  // 7
  
  report ! HitDong  // 8
  
  report ! HitDong  // 9
  
}
