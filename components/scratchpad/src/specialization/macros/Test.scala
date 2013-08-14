package specialization.macros
import scala.language.experimental.macros
import scala.reflect.macros.Context

object Test {
  // basis should be the actual storage
  trait Storage {
    def getUnit(pos: Int): Unit = ???
    def getBool(pos: Int): Boolean = ???
    def getByte(pos: Int): Byte = ???
    def getChar(pos: Int): Char = ???
    def getShort(pos: Int): Short = ???
    def getInt(pos: Int): Int = ???
    def getLong(pos: Int): Long = ???
    def getFloat(pos: Int): Float = ???
    def getDouble(pos: Int): Double = ???
    def getPosition[T](pos: Int): T = ???
  }

  // case class Tuple2[T1, T2](val _1: T1, val _2: T2)
  trait Tuple2[T1, T2] extends Product {
    def _1: T1
    def _2: T2
    // okay, these are here to satisfy the Product inheriance
    // TODO: How to specialize a specialized inheritance
    def canEqual(that: Any) = that.isInstanceOf[Tuple2[_,_]]
    def productArity = 2
    def productElement(n: Int): Any =
      if (n == 0)
        _1
      else if (n == 1)
        _2
      else
        ???
  }

  object Tuple2 {

    // boxed version of the class:
    class BoxedTuple2[T1, T2](val _1: T1, val _2: T2) extends Tuple2[T1, T2]

    // specialized version of the class:
    class SpecializedTuple2[T1, T2] extends Tuple2[T1, T2] with Storage {
      override def _1: T1 = getPosition[T1](0)
      override def _2: T2 = getPosition[T2](0)
    }

    def apply[T1, T2](t1: T1, t2: T2): Tuple2[T1, T2] = macro createTuple[T1, T2]

    def createTuple[T1: c.WeakTypeTag, T2: c.WeakTypeTag](c: Context)(t1: c.Expr[T1], t2: c.Expr[T2]): c.Expr[Tuple2[T1, T2]] = {
      // here we need to create a specialized class -- quasiquotes would certainly come in handy
      ???
    }
  }

}
