fun main() {
    Day18.printSolutionIfTest(64, 58)
}


object Day18 : Day<Int, Int>(18) {
    override fun part1(lines: List<String>): Int {
        val cubes = build(lines).values.filter { !it.air }
        return cubes.flatMap { c -> c.faces.values }.count { it }
    }

    override fun part2(lines: List<String>): Int {
        val cubes = build(lines)
        val solidCubes = cubes.values.filter { !it.air }
        while(addAir(cubes)){}
        while(removeAir(cubes)){}

        val airFaces = cubes.filter { it.value.air }.values.flatMap { c -> c.faces.values }.count { !it }

        val faces =  solidCubes.flatMap { c -> c.faces.values }.count { it }
        //val bubbles = air.filter { it.faces.values.all { t -> !t } }
        //cubes.forEach { point, c -> println("$point ${c.faces} Air: ${c.air}") }
        return faces - airFaces

        //3376 too high
    }

    private fun addAir(cubes: MutableMap<Day18.Point, Day18.Cube>): Boolean {
        val air = cubes.filter { it.value.air }.toMap()
        var added = 0
        air.keys.forEach{ point ->
            if (isSurroundedBy(point, cubes, 5)){
                addMissing(point, cubes)
                added++
            }
        }
        println("Added $added")
        return added > 0

    }

    private fun addMissing(point: Point, cubes: MutableMap<Point, Cube>) {
        val points = listOf(
            Point(point.x - 1, point.y, point.z),
            Point(point.x + 1, point.y, point.z),
            Point(point.x, point.y - 1, point.z),
            Point(point.x, point.y + 1, point.z),
            Point(point.x, point.y, point.z - 1),
            Point(point.x, point.y, point.z + 1)
        )
        //println("$point surrounded: $surrounded")
        points.filter { p -> cubes[p] == null }.forEach { cubes[it] = Cube(air=true) }

    }

    private fun removeAir(cubes: MutableMap<Point, Cube>): Boolean {
        val air = cubes.filter { it.value.air }.toMap()
        var removed = 0
        air.keys.forEach{ point ->
            if (!isSurroundedBy(point, cubes, 6)){
                cubes.remove(point)
                removed++
            }
        }
        println("Removed $removed")
        return removed > 0
    }

    private fun isSurroundedBy(point: Point, cubes: MutableMap<Point, Cube>, numPoints:Int): Boolean {
        val points = listOf(
            Point(point.x - 1, point.y, point.z),
            Point(point.x + 1, point.y, point.z),
            Point(point.x, point.y - 1, point.z),
            Point(point.x, point.y + 1, point.z),
            Point(point.x, point.y, point.z - 1),
            Point(point.x, point.y, point.z + 1)
        )
        //println("$point surrounded: $surrounded")
        return points.mapNotNull { p -> cubes[p] }.count() == numPoints
    }

    private fun processFace(cubes: MutableMap<Point, Cube>, cube: Cube, face: String, otherFace: String, point: Point) {
        var otherCube = cubes[point]
        if (otherCube == null){
            otherCube = Cube(air=true)
            cubes[point] = otherCube
        }
        otherCube.faces[otherFace] = false
        if (!otherCube.air)
            cube.faces[face] = false

    }

    /*
    ######
    #OOO##
    #O O##
    #OOO##
     */

    private fun build(lines: List<String>): MutableMap<Point, Cube> {
        val cubes = lines.map { it.split(",").map { c -> c.toInt() } }
            .map { (x, y, z) -> Point(x, y, z) }.associateWith { Cube() }.toMutableMap()
        cubes.toMap().forEach { (point, cube) ->
            cube.faces.forEach { (face, value) ->
                if (value) {
                    when (face) {
                        "xm" -> processFace(cubes, cube, face, "xp", Point(point.x - 1, point.y, point.z))
                        "xp" -> processFace(cubes, cube, face, "xm", Point(point.x + 1, point.y, point.z))
                        "ym" -> processFace(cubes, cube, face, "yp", Point(point.x, point.y - 1, point.z))
                        "yp" -> processFace(cubes, cube, face, "ym", Point(point.x, point.y + 1, point.z))
                        "zm" -> processFace(cubes, cube, face, "zp", Point(point.x, point.y, point.z - 1))
                        "zp" -> processFace(cubes, cube, face, "zm", Point(point.x, point.y, point.z + 1))
                    }
                }
            }
        }
        return cubes
    }

    data class Point(val x: Int, val y: Int, val z: Int)

    class Cube(val air:Boolean = false) {
        val faces = mutableMapOf(
            "xm" to true,
            "xp" to true,
            "ym" to true,
            "yp" to true,
            "zm" to true,
            "zp" to true,
        )
    }

}

