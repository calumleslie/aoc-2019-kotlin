package uk.zootm.advent

import java.io.File
import java.io.FileInputStream

class Program(val memory: IntArray) {
    var instruction = Addr(0)
    var finished = false

    companion object Parser {
        fun fromString(input: String): Program {
            val memory = input.splitToSequence(",")
                .map { Integer.parseInt(it) }
                .toList()
                .toIntArray()

            return Program(memory)
        }

        fun fromFile(file: File): Program {
            val text = FileInputStream(file).bufferedReader().use { it.readLine() }
            return fromString(text)
        }
    }

    fun execute(trace: Boolean = false) {
        while (!finished) {
            step()
            if (trace) dump()
        }
    }

    fun step() {
        when (instruction.read()) {
            1 -> op1(instruction.arg(0), instruction.arg(1), instruction.arg(2))
            2 -> op2(instruction.arg(0), instruction.arg(1), instruction.arg(2))
            99 -> {
                finished = true
                return
            }
        }
        instruction = instruction.nextInstruction()
    }

    // op1 = sum then store
    fun op1(in1: Addr, in2: Addr, out: Addr) {
        out.readPtr().write(in1.readPtr().read() + in2.readPtr().read())
    }

    // op2 = product then store
    fun op2(in1: Addr, in2: Addr, out: Addr) {
        out.readPtr().write(in1.readPtr().read() * in2.readPtr().read())
    }

    fun dump() {
        println(memory.joinToString())
    }

    inner class Addr(private val i: Int) {
        init {
            assert(i < memory.size, lazyMessage = { "Found invalid location $i (memory size is ${memory.size}" })
        }

        fun write(value: Int) {
            memory[i] = value
        }

        fun readPtr() = Addr(memory[i])

        fun read() = memory[i]

        fun nextInstruction() = Addr(i + 4)

        fun arg(index: Int) = Addr(i + 1 + index)
    }
}
//1,0,0,0,99 becomes 2,0,0,0,99 (1 + 1 = 2).
// becomes 2,3,0,6,99 (3 * 2 = 6).
//2,4,4,5,99,0 becomes 2,4,4,5,99,9801 (99 * 99 = 9801).
//1,1,1,4,99,5,6,0,99 becomes 30,1,1,4,2,5,6,0,99.


fun main() {
    parseExecDump("1,0,0,0,99")
    parseExecDump("2,3,0,3,99")
    parseExecDump("2,4,4,5,99,0")
    parseExecDump("1,1,1,4,99,5,6,0,99")

    val prog1 = Program.fromFile(File("inputs/day2.1.txt"))
    // before running the program, replace position 1 with the value 12 and replace position 2 with the value 2
    prog1.Addr(1).write(12)
    prog1.Addr(2).write(2)
    prog1.execute()
    println("Part 1: ${prog1.Addr(0).read()}")
}

fun parseExecDump( input: String) {
    val program = Program.fromString(input)
    program.execute()
    program.dump()
}