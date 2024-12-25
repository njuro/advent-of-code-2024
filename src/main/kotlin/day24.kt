import utils.readInputBlock

/** [https://adventofcode.com/2024/day/24] */
class Wires : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (initial, rawGates) = readInputBlock("24.txt").split("\n\n").map(String::trim).map(String::lines)

        val initialWires = mutableMapOf<String, Int>()
        initial.forEach {
            val (wire, value) = it.split(": ")
            initialWires[wire] = value.toInt()
        }
        var wires = initialWires.toMutableMap()

        data class Gate(val inputs: Set<String>, val operator: String, val output: String)

        val gates = rawGates.map {
            val (input1, operation, input2, _, output) = it.split(" ")
            Gate(setOf(input1, input2), operation, output)
        }.toMutableSet()

        fun getOutputsForPrefix(prefix: Char) =
            wires.entries.filter { it.key.startsWith(prefix) }.sortedByDescending { it.key }
                .joinToString("") { it.value.toString() }


        fun calculateValues() {
            wires = initialWires.toMutableMap()
            val unprocessedGates = gates.toMutableSet()
            while (unprocessedGates.isNotEmpty()) {
                val gate =
                    unprocessedGates.first { gate -> gate.inputs.all { it in wires } }.also(unprocessedGates::remove)
                val operation = when (gate.operator) {
                    "AND" -> Int::and
                    "OR" -> Int::or
                    "XOR" -> Int::xor
                    else -> error("Invalid operation")
                }
                wires[gate.output] = gate.inputs.map(wires::getValue).reduce(operation)
            }
        }

        return if (part2) {
            val faultyGates = mutableSetOf<String>()

            fun Long.toBinaryString(): String =
                if (this > 0) div(2).toBinaryString() + mod(2) else ""

            fun Char.index(index: Int) = "$this${index.toString().padStart(2, '0')}"

            fun findGate(input1: String? = null, input2: String? = null, operator: String? = null): Gate =
                gates.single { gate ->
                    listOfNotNull(
                        input1?.let { it in gate.inputs },
                        input2?.let { it in gate.inputs },
                        operator?.let { it == gate.operator }
                    ).reduce(Boolean::and)
                }

            fun swap(output1: String, output2: String) {
                faultyGates.add(output1)
                faultyGates.add(output2)
                val gate1 = gates.first { it.output == output1 }.also(gates::remove)
                val gate2 = gates.first { it.output == output2 }.also(gates::remove)
                gates.add(gate1.copy(output = output2))
                gates.add(gate2.copy(output = output1))
            }

            val expected = (
                    getOutputsForPrefix('x').toLong(2) + getOutputsForPrefix('y').toLong(2)
                    ).toBinaryString()

            do {
                calculateValues()
                val actual = getOutputsForPrefix('z')
                val faultyBit = expected.reversed().zip(actual.reversed()).withIndex()
                    .filter { (_, value) -> value.first != value.second }.map { it.index }.firstOrNull() ?: break

                val previousAndOutput = findGate('x'.index(faultyBit - 1), 'y'.index(faultyBit - 1), "AND").output
                val carryIn = findGate(previousAndOutput, operator = "OR").output
                val xorOutput = findGate('x'.index(faultyBit), 'y'.index(faultyBit), "XOR").output
                val andOutput = findGate('x'.index(faultyBit), 'y'.index(faultyBit), "AND").output

                val sumGate = findGate(carryIn, operator = "XOR")
                if (sumGate.inputs.first { it != carryIn } != xorOutput) {
                    swap(xorOutput, andOutput)
                }
                if (sumGate.output != 'z'.index(faultyBit)) {
                    swap(sumGate.output, 'z'.index(faultyBit))
                }
            } while (true)
            faultyGates.sorted().joinToString(",")
        } else {
            calculateValues()
            getOutputsForPrefix('z').toLong(2)
        }
    }
}

fun main() {
    print(Wires().run(part2 = true))
}
