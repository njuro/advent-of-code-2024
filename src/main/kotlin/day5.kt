import utils.readInputBlock

/** [https://adventofcode.com/2024/day/5] */
class Rules : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (rawRules, rawUpdates) = readInputBlock("5.txt").split("\n\n").map { it.lines() }
        val rules = rawRules.map { line -> line.split("|").let { it.first().toInt() to it.last().toInt() } }
        val updates = rawUpdates.filter { it.isNotBlank() }
            .map { line -> line.split(",").filter { it.isNotEmpty() }.map { it.toInt() } }

        val (valid, notValid) = updates.partition { update ->
            rules.filter { (x, y) -> x in update && y in update }
                .all { (x, y) -> update.indexOf(x) < update.indexOf(y) }
        }

        val printed = if (part2) notValid.map { update ->
            update.sortedWith { a, b -> if ((a to b) in rules) -1 else 1 }
        } else valid

        return printed.sumOf { it[it.size / 2] }
    }

}

fun main() {
    print(Rules().run(part2 = true))
}
