import utils.readInputLines

/** [https://adventofcode.com/2024/day/22] */
class Sellers : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val initialNumbers = readInputLines("22.txt").map { it.toLong() }

        val sellers = initialNumbers.map { initial ->
            generateSequence(initial) { current ->
                val first = (current xor (current * 64)) % 16777216
                val second = (first xor (first / 32)) % 16777216
                (second xor (second * 2048)) % 16777216
            }.take(2001).toList()
        }

        return if (part2)
            sellers.map { secretNumbers ->
                val prices = secretNumbers.map { (it % 10).toInt() }
                val changes = prices.zipWithNext().map { (prev, next) -> next - prev }
                val sequences = mutableMapOf<List<Int>, Int>()
                changes.withIndex().windowed(4).forEach { indexedSequence ->
                    val sequence = indexedSequence.map { it.value }
                    if (sequence !in sequences) {
                        sequences[sequence] = prices[indexedSequence.last().index + 1]
                    }
                }
                sequences
            }.reduce { combined, sequences ->
                sequences.forEach { (sequence, totalValue) ->
                    combined.merge(sequence, totalValue, Int::plus)
                }
                combined
            }.values.max()
        else sellers.sumOf { it.last() }
    }
}

fun main() {
    print(Sellers().run(part2 = true))
}
