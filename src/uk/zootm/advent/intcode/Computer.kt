package uk.zootm.advent.intcode

import java.io.File
import java.io.FileInputStream

class Computer(val memory: IntArray) {
    var instruction = Addr(0)
    var finished = false

    companion object Parser {
        fun fromString(input: String): Computer {
            val memory = input.splitToSequence(",")
                .map { Integer.parseInt(it) }
                .toList()
                .toIntArray()

            return Computer(memory)
        }

        fun fromFile(file: File): Computer {
            val text = FileInputStream(file).bufferedReader().use { it.readLine() }
            return fromString(text)
        }
    }

    fun copy(): Computer = Computer(memory.clone())

    fun execute(trace: Boolean = false) {
        while (!finished) {
            step()
            if (trace) println(this)
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

    fun memoryEquals(other: Computer): Boolean = this.memory.contentEquals(other.memory)

    override fun toString(): String = this.memory.joinToString(",")

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