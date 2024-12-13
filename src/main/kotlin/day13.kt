import utils.readInputBlock

/** [https://adventofcode.com/2024/day/13] */
class Automates : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val pattern = Regex(
            """
            Button A: X\+(\d+), Y\+(\d+)
            Button B: X\+(\d+), Y\+(\d+)
            Prize: X=(\d+), Y=(\d+)
        """.trimIndent()
        )

        return pattern.findAll(readInputBlock("13.txt")).map { it.groupValues.drop(1).map(String::toLong) }.sumOf {
            val offset = if (part2) 10000000000000L else 0L
            val x1 = it[0]
            val x2 = it[2]
            val y1 = it[1]
            val y2 = it[3]
            val z1 = it[4] + offset
            val z2 = it[5] + offset
            val a = (z2 * x2 - y2 * z1) / (y1 * x2 - y2 * x1)
            val b = (z1 - x1 * a) / x2
            val correct = a * x1 + b * x2 == z1 && a * y1 + b * y2 == z2
            if (correct) 3 * a + b else 0
        }
    }
}

fun main() {
    print(Automates().run(part2 = true))
}
