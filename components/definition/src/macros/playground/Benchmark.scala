package macros.playground
import scala.reflect.macros.Context
import scala.collection.mutable.{ListBuffer, Stack}

import scala.language.experimental.macros // crappy sip 18!!!

object Benchmark {

  object BenchType extends Enumeration {
    type BenchType = Value
    val Generic, Miniboxed, Specialized = Value
  }

  import BenchType._

  def benchmark(tpe: BenchType): Unit = macro benchmark_impl

  def benchmark_impl(c: Context)(tpe: c.Expr[BenchType]): c.Expr[Unit] = {
    import c.universe._

    val tree = c.parse("""
      class C {
        def foo = "foo"
      }
      def print_foo(): Unit =
        println((new C).foo)
      print_foo()
    """)

    c.Expr[Unit](tree)
  }

//  def printf(format: String, params: Any*): Unit = macro printf_impl
//
//  def printf_impl(c: Context)(format: c.Expr[String], params: c.Expr[Any]*): c.Expr[Unit] = {
//    import c.universe._
//    val Literal(Constant(s_format: String)) = format.tree
//    val evals = ListBuffer[ValDef]()
//
//    def precompute(value: Tree, tpe: Type): Ident = {
//      val freshName = newTermName(c.fresh("eval$"))
//      evals += ValDef(Modifiers(), freshName, TypeTree(tpe), value)
//      Ident(freshName)
//    }
//
//    val paramsStack = Stack[Tree]((params map (_.tree)): _*)
//    val refs = s_format.split("(?<=%[\\w%])|(?=%[\\w%])") map {
//      case "%d" => precompute(paramsStack.pop, typeOf[Int])
//      case "%s" => precompute(paramsStack.pop, typeOf[String])
//      case "%%" => Literal(Constant("%"))
//      case part => Literal(Constant(part))
//    }
//    val stats = evals ++ refs.map(ref => reify(print(c.Expr[Any](ref).splice)).tree)
//    c.Expr[Unit](Block(stats.toList, Literal(Constant(()))))
//  }
}
