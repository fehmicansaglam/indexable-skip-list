package net.fehmicansaglam.collection

import org.scalatest._

import scala.util.Random

class IndexableSkipListSpec extends FlatSpec with Matchers {

  "An IndexableSkipList" should "find " in {
    val skipList = new IndexableSkipList[Integer]

    (1 to 1000000).foreach { _ =>
      val num = Random.nextInt(1000000)
      println(s"Adding $num")
      skipList.insert(num)
      skipList.contains(num) === true
    }

//    println(skipList)
  }

}
