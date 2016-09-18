package net.fehmicansaglam.collection

import scala.util.Random

sealed trait Node[T] extends Ordered[Node[T]] {

  def right: Option[Node[T]]

  def bottom: Option[Node[T]]

}

case class Element[T: Ordering](element: T,
                                var right: Option[Node[T]] = None,
                                var bottom: Option[Node[T]] = None) extends Node[T] {
  override def compare(that: Node[T]): Int = that match {
    case _: Head[_] => 1
    case _: Nil[_] => -1
    case Element(element, _, _) => implicitly[Ordering[T]].compare(this.element, element)
  }
}

case class Head[T](var right: Option[Element[T]] = None,
                   var bottom: Option[Head[T]] = None) extends Node[T] {
  override def compare(that: Node[T]): Int = -1
}

case class Nil[T]() extends Node[T] {
  override def right: Option[Node[T]] = None

  override def bottom: Option[Node[T]] = None

  override def compare(that: Node[T]): Int = 1
}

class IndexableSkipList[T: Ordering] {

  private var head = Head[T]()
  private var maxLevels = 1

  def insert(element: T): this.type = {
    val levels = Iterator.continually(Random.nextBoolean()).takeWhile(identity).size + 1

    while (maxLevels < levels) {
      val newHead = Head(bottom = Some(head))
      head = newHead
      maxLevels += 1
    }

    var curr: Node[T] = head
    val i = maxLevels - levels
    (1 to i).foreach { _ =>
      curr = head.bottom.get
    }


    val newElement = Element(element)

    while (newElement > curr && curr.right.isDefined) {
      curr = curr.right.get
    }

    println(curr)


    this
  }
}

object IndexableSkipList extends App {
  val list = new IndexableSkipList[Int]
  (1 to 100).foreach { _ =>
    list.insert(100)
  }

  val abuzer: List[Node[Int]] = List(Head(), Nil(), Element(5), Head(), Element(1))
  println(abuzer.sorted)
}
