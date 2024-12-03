import utils.readInputLines
import kotlin.math.abs
import kotlin.math.sign

/** [https://adventofcode.com/2024/day/2] */
class Reports : AdventOfCodeTask {
    override fun run(part2: Boolean): Any =
        readInputLines("2.txt").map { line -> line.split(" ").map { it.toInt() } }.toMutableList()
            .count { report ->
                if (part2) report.indices.any { index -> (report.toMutableList().apply { removeAt(index) }).isSafe() }
                else report.isSafe()
            }

    private fun List<Int>.isSafe(): Boolean =
        zipWithNext().map { (prev, next) -> next - prev }.let { differences ->
            differences.all { abs(it) in 1..3 } &&
                    differences.map { sign(it.toDouble()) }.distinct().size == 1
        }
}

fun main() {
    print(Reports().run(part2 = true))
}
