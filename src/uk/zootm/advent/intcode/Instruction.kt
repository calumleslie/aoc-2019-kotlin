package uk.zootm.advent.intcode

import uk.zootm.advent.intcode.Memory.Addr

abstract class Instruction(protected val base: Addr, paramCount: Int) {
    protected val parameters: List<Parameter> =
        (0 until paramCount).map { resolveParameter(it) }

    open fun exec(input: Input, output: Output) {
        // Default is no-op
    }

    open fun next(): Addr? = base + (parameters.size + 1)

    override fun toString() = "${javaClass.simpleName}($parameters)"

    private fun resolveParameter(i: Int): Parameter {
        val location = base + (i + 1)
        return when (findMode(i)) {
            0 -> PositionParameter(location)
            1 -> ImmediateParameter(location)
            else -> throw IllegalArgumentException("Unknown parameter mode ${findMode(i)}")
        }
    }

    private fun findMode(position: Int): Int {
        val modes = base.read() / 100
        val mag = powerOfTen(position)
        return (modes / mag) % (mag * 10)
    }

    private fun powerOfTen(n: Int): Int {
        var result = 1
        (1..n).forEach { result *= 10 }
        return result
    }

    companion object Instructions {
        fun from(addr: Addr): Instruction = when (addr.read() % 100) {
            1 -> Sum(addr)
            2 -> Product(addr)
            3 -> Read(addr)
            4 -> Write(addr)
            5 -> JumpIfTrue(addr)
            6 -> JumpIfFalse(addr)
            7 -> LessThan(addr)
            8 -> Equals(addr)
            99 -> Stop(addr)
            else -> throw UnsupportedOperationException("Unsupported opcode ${addr.read()}")
        }
    }
}

/**
 * Operation 1: Sum
 */
class Sum(base: Addr) : Instruction(base, 3) {
    override fun exec(input: Input, output: Output) {
        parameters[2].write(parameters[0].read() + parameters[1].read())
    }
}

/**
 * Operation 2: Product
 */
class Product(base: Addr) : Instruction(base, 3) {
    override fun exec(input: Input, output: Output) {
        parameters[2].write(parameters[0].read() * parameters[1].read())
    }
}

/**
 * Operation 3: Read
 */
class Read(base: Addr) : Instruction(base, 1) {
    override fun exec(input: Input, output: Output) {
        parameters[0].write(input.read())
    }

    override fun next(): Addr? = base + 2
}

/**
 * Operation 4: Write
 */
class Write(base: Addr) : Instruction(base, 1) {
    override fun exec(input: Input, output: Output) {
        output.write(parameters[0].read())
    }
}

/**
 * Operation 5: jump-if-true
 *
 * if the first parameter is non-zero, it sets the instruction pointer to the value from the second parameter.
 * Otherwise, it does nothing.
 */
class JumpIfTrue(base: Addr) : Instruction(base, 2) {
    override fun next() = if (parameters[0].read() != 0) parameters[1].readAsAddr() else super.next()
}

/**
 * Operation 6: jump-if-false
 *
 * if the first parameter is zero, it sets the instruction pointer to the value from the second parameter. Otherwise, it
 * does nothing.
 */

class JumpIfFalse(base: Addr) : Instruction(base, 2) {
    override fun next() = if (parameters[0].read() == 0) parameters[1].readAsAddr() else super.next()
}

/**
 * Operation 7: less than
 *
 * if the first parameter is less than the second parameter, it stores 1 in the position given by the third parameter.
 * Otherwise, it stores 0.
 */
class LessThan(base: Addr) : Instruction(base, 3) {
    override fun exec(input: Input, output: Output) {
        val lt = parameters[0].read() < parameters[1].read()
        parameters[2].write(if (lt) 1 else 0)
    }
}

/**
 * Operation 8: equals
 *
 * if the first parameter is equal to the second parameter, it stores 1 in the position given by the third parameter. Otherwise, it stores 0.
 */
class Equals(base: Addr) : Instruction(base, 3) {
    override fun exec(input: Input, output: Output) {
        val eq = parameters[0].read() == parameters[1].read()
        parameters[2].write(if (eq) 1 else 0)
    }
}

/**
 * Operation 99: Stop
 */
class Stop(base: Addr) : Instruction(base, 0) {
    override fun next(): Addr? = null
}