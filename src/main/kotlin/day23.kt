import utils.readInputLines

/** [https://adventofcode.com/2024/day/23] */
class Groups : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val nodes = mutableSetOf<String>()
        val pairs = readInputLines("23.txt").map {
            it.split("-").toSet().also(nodes::addAll)
        }.toSet()

        return if (part2) {
            val groups = mutableSetOf<Set<String>>()
            pairs.forEach { pair ->
                val group = pair.toMutableSet()
                while (true) {
                    val next = nodes.find { node ->
                        node !in group && group.all { existing -> setOf(node, existing) in pairs }
                    } ?: break
                    group.add(next)
                }
                groups.add(group)
            }
            groups.maxBy { it.size }.sorted().joinToString(",")
        } else {
            val triples = mutableSetOf<Set<String>>()
            pairs.forEach {
                val (a, b) = it.toList()
                nodes.forEach { c ->
                    if (c != a && c != b && setOf(a, c) in pairs && setOf(b, c) in pairs) {
                        triples.add(setOf(a, b, c))
                    }
                }
            }
            triples.count { triple -> triple.any { it.startsWith("t") } }
        }
    }
}

fun main() {
    print(Groups().run(part2 = true))
}
