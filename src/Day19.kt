fun main() {
    Day19.printSolutionIfTest(33, 56*62)
}


object Day19 : Day<Int, Int>(19) {
    override fun part1(lines: List<String>) = solve(lines, 24)
    override fun part2Test(lines: List<String>) = solve2(lines, 32)
    override fun part2(lines: List<String>) = solve2(lines.take(3), 32)

    data class Blueprint(
        val oreRobotCost: Int,
        val clayRobotCost: Int,
        val obsidianRobotOreCost: Int,
        val obsidianRobotClayCost: Int,
        val geodeRobotOreCost: Int,
        val geodeRobotObsidianCost: Int
    )

    fun parseBlueprint(line: String) =
        line.split(" ").mapNotNull { it.toIntOrNull() }.let { Blueprint(it[0], it[1], it[2], it[3], it[4], it[5]) }

    fun solve(lines: List<String>, minutes: Int):Int {
        return lines.map { parseBlueprint(it) }.map{ solveForBlueprint(minutes, it) }
            .mapIndexed{ index, value -> (index+1)*value}.reduce(Int::plus)
    }

    fun solve2(lines: List<String>, minutes: Int):Int {
        return lines.map { parseBlueprint(it) }.map{ solveForBlueprint(minutes, it) }
            .reduce(Int::times)
    }

    fun solveForBlueprint(minutes: Int, blueprint: Blueprint):Int {
        var states = listOf(State(0,0,0,0,1,0,0,0))
        repeat(minutes) {states = tick(states, blueprint)}
        //println(states.joinToString("\n"))
        val result =  states.maxOf { it.geode }
        println(result)
        return result
    }

    fun tick(start: List<State>, blueprint: Blueprint) = start.flatMap { state ->
        //state.next().let {
            listOfNotNull(
                state.next(),
                state.buyOreRobot(blueprint.oreRobotCost),
                state.buyClayRobot(blueprint.clayRobotCost),
                state.buyObsidianRobot(blueprint.obsidianRobotOreCost, blueprint.obsidianRobotClayCost),
                state.buyGeodeRobot(blueprint.geodeRobotOreCost, blueprint.geodeRobotObsidianCost)
            )
        //}
    }.distinct().sortedByDescending { s -> s.score() }.take( 1500 )

    data class State(
        val ore: Int,
        val clay: Int,
        val obsidian: Int,
        val geode: Int,
        val oreRobot: Int,
        val clayRobot: Int,
        val obsidianRobot: Int,
        val geodeRobot: Int
    ) {
        fun next() = copy(
            ore = ore + oreRobot,
            clay = clay + clayRobot,
            obsidian = obsidian + obsidianRobot,
            geode = geode + geodeRobot
        )

        fun buyOreRobot(oreCost: Int) =
            (ore - oreCost).takeIf { it >= 0 }?.let { copy(ore = it).next().copy(oreRobot = oreRobot + 1) }

        fun buyClayRobot(oreCost: Int) =
            (ore - oreCost).takeIf { it >= 0 }?.let {
                copy(ore = it).next().copy(clayRobot = clayRobot + 1)
            }

        fun buyObsidianRobot(oreCost: Int, clayCost: Int) =
            (ore - oreCost).takeIf { it >= 0 }?.let{ newOre ->
                (clay - clayCost).takeIf { it >= 0 }?.let{newClay ->
                    copy(ore = newOre, clay = newClay).next().copy(obsidianRobot = obsidianRobot + 1)
                }
            }

        fun buyGeodeRobot(oreCost: Int, obsidianCost: Int) =
            (ore - oreCost).takeIf { it >= 0 }?.let{ newOre ->
                (obsidian - obsidianCost).takeIf { it >= 0 }?.let{newObsidian ->
                    copy(ore = newOre, obsidian = newObsidian).next().copy(geodeRobot = geodeRobot + 1)
                }
            }

        fun score() = (ore + oreRobot) + 10*(clay + clayRobot) + 1000*(obsidian + obsidianRobot) + 100000 * (geode + geodeRobot)


    }
}

