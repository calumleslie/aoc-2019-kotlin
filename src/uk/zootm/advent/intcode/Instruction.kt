package uk.zootm.advent.intcode

import uk.zootm.advent.intcode.Memory.Addr

interface Instruction {
    fun exec(input: Input, output: Output)
    fun next(): Addr?

    companion object Instructions {
        fun from(addr: Addr): Instruction = when (addr.read()) {
            1 -> Sum(addr)
            2 -> Product(addr)
            3 -> Read(addr)
            4 -> Write(addr)
            99 -> Stop(addr)
            else -> throw UnsupportedOperationException("Unsupported opcode ${addr.read()}")
        }
    }
}

/**
 * Operation 1: Sum
 */
class Sum(private val base: Addr) : Instruction {
    override fun exec(input: Input, output: Output) {
        val in1 = base.arg(0)
        val in2 = base.arg(1)
        val out = base.arg(2)
        out.readPtr().write(in1.readPtr().read() + in2.readPtr().read())
    }

    override fun next(): Addr? = base + 4
}

/**
 * Operation 2: Product
 */
class Product(private val base: Addr) : Instruction {
    override fun exec(input: Input, output: Output) {
        val in1 = base.arg(0)
        val in2 = base.arg(1)
        val out = base.arg(2)
        out.readPtr().write(in1.readPtr().read() * in2.readPtr().read())
    }

    override fun next(): Addr? = base + 4
}

/**
 * Operation 3: Read
 */
class Read(private val base: Addr) : Instruction {
    override fun exec(input: Input, output: Output) {
        val target = base.arg(0)
        target.readPtr().write(input.read())
    }

    override fun next(): Addr? = base + 2
}

/**
 * Operation 3: Write
 */
class Write(private val base: Addr) : Instruction {
    override fun exec(input: Input, output: Output) {
        val source = base.arg(0)
        output.write(source.readPtr().read())
    }

    override fun next(): Addr? = base + 2
}

/**
 * Operation 99: Stop
 */
class Stop(private val base: Addr) : Instruction {
    override fun exec(input: Input, output: Output) {
        // No-op
    }

    override fun next(): Addr? = null
}