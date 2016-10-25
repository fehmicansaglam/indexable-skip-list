package net.fehmicansaglam.collection

import org.scalatest._

import scala.util.Random

class IndexableSkipListSpec extends FlatSpec with Matchers {

  "An IndexableSkipList" should "find" in {
    val skipList = new IndexableSkipList[Integer]

    (1 to 64).foreach { _ =>
      val num = Random.nextInt(100)
      println(s"Adding $num")
      skipList.insert(num)
      skipList.contains(num) === true
      println(skipList)
    }

    println(skipList.size)

  }

  it should "find closest" in {
    val skipList = new IndexableSkipList[Integer]

    skipList.insert(1)
    println(skipList)

    skipList.insert(2)
    println(skipList)

    skipList.insert(3)
    println(skipList)

    skipList.insert(4)
    println(skipList)

    skipList.insert(6)
    println(skipList)

    skipList.contains(1) shouldBe true
    skipList.contains(2) shouldBe true
    skipList.contains(3) shouldBe true
    skipList.contains(4) shouldBe true
    skipList.contains(5) shouldBe false
    skipList.contains(6) shouldBe true

    skipList.closest(5) shouldBe 4
  }

}
