package com.example

import com.example.message._
import akka.actor.Actor

class DontBotherMeException extends Exception

class ExplodeException extends Exception

class IamGodException extends Exception

/**
 * 企鵝
 */
class Penguin(val name: String) extends Actor {
  protected var hit = 0
  
  def rInterest() = {
    println(name + " got Interest message")
    sender() ! Three(name, "吃飯", "睡覺", "打東東")
  }
  
  def rHit() = {
    println(name + " got Hit message")
    sender() ! Iam(name)
  }
  
  def rQueryHit() = {
    println(name + " got Query Hit message")
    sender() ! ResponseHit(name, hit)
  }
  
  def rWhy() = {
    println(name + " got Why message and Ignore")
  }
  
  def rWhoAreYou() = {
    println(name + " got WhoAreYou message")
    sender() ! Iam(name)
  }
  
  def receive = {
    case Interest => rInterest()
    
    case Hit => rHit()
    
    case QueryHit => rQueryHit()
    
    case Why() => rWhy()
    
    case WhoAreYou => rWhoAreYou()
  }
  
  override def preStart() = {
    println(s"$name pre-start")
  }
  
  override def postStop() = {
    println(s"$name post-stop")
  }
}

/**
 * 叫東東的企鵝
 */
class DongDong extends Penguin("東東") {
  
  override def rInterest() = {
    println(name + " got Interest message")
    sender() ! Two(name, "吃飯", "睡覺")
  }
  
  override def rWhy() = {
    println(name + " got Why message")
    sender() ! Because(name, "我就是" + name)
  }
  
  override def rHit() = {
    println(name + " got Hit message")
    hit += 1
    
    if (hit == 4)
      throw new DontBotherMeException
    else if (hit == 6)
      throw new ExplodeException
    else if (hit > 6)
      throw new IamGodException
    else
      sender() ! DontHitMe(name, hit)
  }
}


