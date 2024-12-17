import utils.readInputBlock
import kotlin.math.floor
import kotlin.math.pow

/** [https://adventofcode.com/2024/day/17] */
class Registers : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (registers, program) = readInputBlock("17.txt").split("\n\n")
        val (originalA, originalB, originalC) = registers.lines().map { it.substringAfter(": ").toLong() }
        val originalInstructions = program.substringAfter(": ").trim().split(",").map(String::toInt)

        fun compute(
            startA: Long,
            startB: Long,
            startC: Long,
            instructions: List<Int>,
            singleRun: Boolean = false
        ): List<Int> {
            var a = startA
            var b = startB
            var c = startC
            fun Int.combo(): Long =
                when (this) {
                    in (0..3) -> this.toLong()
                    4 -> a
                    5 -> b
                    6 -> c
                    else -> error("Invalid value")
                }

            var pointer = 0
            val output = mutableListOf<Int>()
            while (pointer < instructions.size) {
                val opcode = instructions[pointer]
                val operand = instructions[pointer + 1]

                when (opcode) {
                    0 -> a = floor((a / 2.0.pow(operand.combo().toInt()))).toLong()
                    1 -> b = b.xor(operand.toLong())
                    2 -> b = operand.combo() % 8
                    3 -> if (a != 0L && !singleRun) {
                        pointer = operand
                        continue
                    }

                    4 -> b = b.xor(c)
                    5 -> output.add((operand.combo() % 8).toInt())
                    6 -> b = floor(a / 2.0.pow(operand.combo().toInt())).toLong()
                    7 -> c = floor(a / 2.0.pow(operand.combo().toInt())).toLong()
                    else -> error("Invalid value")
                }

                pointer += 2
            }

            return output
        }

        fun solveForOutput(expectedOutput: Int, startFrom: Long): Sequence<Long> =
            generateSequence(startFrom - 1) { prev ->
                generateSequence(prev + 1) { it + 1 }
                    .first { a ->
                        compute(a, originalB, originalC, originalInstructions, singleRun = true)
                            .single()
                            .toInt() == expectedOutput
                    }
            }

        fun calculateValue(remainingInstructions: MutableList<Int>, currentValue: Long = 0): Long? {
            if (remainingInstructions.isEmpty()) return currentValue
            val currentInstruction = remainingInstructions.removeLast()

            return solveForOutput(currentInstruction, currentValue * 8)
                .takeWhile { it < currentValue.coerceAtLeast(1) * 8 + 8 }
                .filter { floor(it / 8.0).toLong() == currentValue }
                .firstNotNullOfOrNull { nextValue ->
                    calculateValue(remainingInstructions.toMutableList(), nextValue)
                }
        }

        return if (part2) {
            calculateValue(originalInstructions.toMutableList()) ?: -1
        } else {
            compute(originalA, originalB, originalC, originalInstructions).joinToString(",")
        }
    }
}

fun main() {
    print(Registers().run(part2 = true))
}
