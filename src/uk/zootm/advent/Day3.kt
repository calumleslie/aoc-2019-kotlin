package uk.zootm.advent

import java.io.FileInputStream
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

data class Vector(val x: Int, val y: Int) {
    operator fun times(magnitude: Int) = Vector(x * magnitude, y * magnitude)
    operator fun plus(other: Vector) = Vector(x + other.x, y + other.y)

    fun manhattanTo(other: Vector) = (x - other.x).absoluteValue + (y - other.y).absoluteValue
}

data class Segment(val direction: Direction, val magnitude: Int) {
    fun pointsFrom(start: Vector): List<Vector> = (1..magnitude)
        .map { m -> start + direction.vector * m }

    val vector: Vector = direction.vector * magnitude
}

data class Path(val segments: List<Segment>) {
    companion object Parser {
        fun parse(commands: String) = Path(commands.split(',').map { parseSegment(it) })

        private fun parseSegment(command: String): Segment {
            val direction = parseDirection(command[0])
            val magnitude = command.substring(1 until command.length).toInt()
            return Segment(direction, magnitude)
        }

        private fun parseDirection(c: Char) = when (c) {
            'U' -> Direction.UP
            'D' -> Direction.DOWN
            'L' -> Direction.LEFT
            'R' -> Direction.RIGHT
            else -> throw IllegalArgumentException("Unknown command $c")
        }
    }

    private fun points(): List<Vector> {
        val result = ArrayList<Vector>()
        var location = ORIGIN
        for (segment in segments) {
            result.addAll(segment.pointsFrom(location))
            location += segment.vector
        }
        return result
    }

    fun intersect(other: Path) = points().toSet().intersect(other.points())

    // Note this returns 0 if the point is not in the path. The +1 is to account for the fact that we don't include
    // origin in the set of points
    fun stepsTo(vector: Vector) = points().indexOf(vector) + 1
}

val ORIGIN = Vector(0, 0)

enum class Direction(x: Int, y: Int) {
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    val vector = Vector(x, y)
    operator fun times(magnitude: Int) = vector * magnitude
}

fun closestToOrigin(points: Set<Vector>) = points.minBy { it.manhattanTo(ORIGIN) }

fun distanceToClosestIntersection(path1: Path, path2: Path): Int? =
    closestToOrigin(path1.intersect(path2))?.manhattanTo(ORIGIN)

fun stepsToLeastCostlyIntersection(path1: Path, path2: Path): Int? {
    val intersections = path1.intersect(path2)
    return intersections
        .map { path1.stepsTo(it) + path2.stepsTo(it) }
        .min()
}

fun main() {
    example1()
    example2()
    withInputPaths { path1, path2 -> println(distanceToClosestIntersection(path1, path2)) }

    example3()
    example4()
    withInputPaths { path1, path2 -> println(stepsToLeastCostlyIntersection(path1, path2)) }
}

fun <T> withInputPaths(function: (Path, Path) -> T): T {
    return FileInputStream("inputs/day3.1.txt").bufferedReader().use {
        function.invoke(
            Path.parse(it.readLine()),
            Path.parse(it.readLine())
        )
    }
}

fun example1() {
    assertEquals(
        159, distanceToClosestIntersection(
            Path.parse("R75,D30,R83,U83,L12,D49,R71,U7,L72"),
            Path.parse("U62,R66,U55,R34,D71,R55,D58,R83")
        )
    )
}

fun example2() {
    assertEquals(
        135, distanceToClosestIntersection(
            Path.parse("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51"),
            Path.parse("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        )
    )
}

fun example3() {
    assertEquals(
        610, stepsToLeastCostlyIntersection(
            Path.parse("R75,D30,R83,U83,L12,D49,R71,U7,L72"),
            Path.parse("U62,R66,U55,R34,D71,R55,D58,R83")
        )
    )
}

fun example4() {
    assertEquals(
        410, stepsToLeastCostlyIntersection(
            Path.parse("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51"),
            Path.parse("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        )
    )
}