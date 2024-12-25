import utils.readInputBlock

/** [https://adventofcode.com/2024/day/24] */
class Wires : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val wires = mutableMapOf<String, Int>()
        val (initial, rawGates) = readInputBlock("24.txt").split("\n\n").map(String::trim).map(String::lines)
        initial.forEach {
            val (wire, value) = it.split(": ")
            wires[wire] = value.toInt()
        }
        val gates = rawGates.associate {
            val (input1, operation, input2, _, output) = it.split(" ")
            output to (setOf(input1, input2) to operation)
        }.toMutableMap()

        fun getOutputsForPrefix(prefix: Char) =
            wires.entries.filter { it.key.startsWith(prefix) }.sortedByDescending { it.key }
                .joinToString("") { it.value.toString() }

        while (gates.isNotEmpty()) {
            val gate = gates.entries.first { (_, value) -> value.first.all { it in wires } }
            gates.remove(gate.key)
            val (inputs, operator) = gate.value
            val operation = when (operator) {
                "AND" -> Int::and
                "OR" -> Int::or
                "XOR" -> Int::xor
                else -> error("Invalid operation")
            }
            wires[gate.key] = inputs.map(wires::getValue).reduce(operation)
        }
        return if (part2) {
            fun Long.toBinaryString(): String =
                if (this > 0) div(2).toBinaryString() + mod(2) else ""

            val actual = getOutputsForPrefix('z')
            val expected = (
                    getOutputsForPrefix('x').toLong(2) + getOutputsForPrefix('y').toLong(2)
                    ).toBinaryString()
            val faultyBits = expected.reversed().zip(actual.reversed()).withIndex()
                .filter { (_, value) -> value.first != value.second }.map { it.index }
            val faultyGates = listOf("wgb", "wbw", "z09", "gwh", "rcb", "z21", "z39", "jct")
            println(faultyBits)
//            do {
//                // TODO
//            } while (faultyBits.isNotEmpty())
            faultyGates.sorted().joinToString(",")
        } else getOutputsForPrefix('z').toLong(2)
    }
}

fun main() {
    print(Wires().run(part2 = true))
}
