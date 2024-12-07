import utils.readInputLines

/** [https://adventofcode.com/2024/day/7] */
class Equations : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val equations = readInputLines("7.txt").map { line ->
            val (result, operands) = line.split(": ")
            result.toLong() to operands.trim().split(" ").map { it.toLong() }
        }

        fun computeCombinations(size: Int, acc: List<Char> = emptyList()): List<List<Char>> {
            if (acc.size == size) return listOf(acc)

            return computeCombinations(size, acc + '*') +
                    computeCombinations(size, acc + '+') +
                    if (part2) computeCombinations(size, acc + '|') else emptyList()
        }

        val operatorMap = mutableMapOf<Int, List<List<Char>>>()

        return equations.sumOf { (result, operands) ->
            val candidates = operatorMap.computeIfAbsent(operands.size - 1, ::computeCombinations)
            val valid = candidates.any { operators ->
                operands.reduceIndexed { index, prev, next ->
                    when (operators[index - 1]) {
                        '*' -> prev * next
                        '+' -> prev + next
                        '|' -> "$prev$next".toLong()
                        else -> error("Unexpected operator")
                    }
                } == result
            }
            if (valid) result else 0
        }
    }
}

fun main() {
    print(Equations().run(part2 = true))
}
