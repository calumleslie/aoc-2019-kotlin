package uk.zootm.advent.intcode

import uk.zootm.advent.intcode.Memory.Addr
import java.io.File
import java.io.FileInputStream

class Computer(val memory: Memory) {
    var instruction = memory.Addr(0)
    var finished = false

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

    override fun toString(): String = "Computer(instruction: $instruction, finished: $finished, memory: $memory)"

}