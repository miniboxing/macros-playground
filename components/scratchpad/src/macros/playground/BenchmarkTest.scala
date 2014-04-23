package macros.playground

object BenchmarkTest {
  def main(args: Array[String]): Unit = {

    import Benchmark._
    import BenchType._

    benchmark(Specialized)
  }
}
