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

    val parameter: TypeDef = tpe.tree match {
      case q"Benchmark.BenchType.Generic"     => q"type T"
      case q"Benchmark.BenchType.Miniboxed"   => q"@miniboxed type T"
      case q"Benchmark.BenchType.Specialized" => q"@specialized type T"
    }

    val target: Tree = q"""
      object BenchmarkTarget {
        class C[$parameter] {
          def foo() = this.getClass.toString
        }
      }
    """

    val benchmarks: List[Tree] =
      for (T <- List(tq"Int", tq"String", tq"Long")) yield {
        val benchTitle = "Benchmark for " + tpe.tree.symbol.name + " with " + T.name.toString + ":  "
        val benchString = c.parse("\"" + benchTitle + "\"")

        q"""
          import BenchmarkTarget._

          val c = new C[$T]
          println($benchString + c.foo())
        """
      }

    val tree = q"""
      $target
      ..$benchmarks
    """

    c.Expr[Unit](tree)
  }
}
