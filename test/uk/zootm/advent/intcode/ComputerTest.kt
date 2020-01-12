package uk.zootm.advent.intcode

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ComputerTest {
    @Test
    fun `day 2 - 1,0,0,0,99 becomes 2,0,0,0,99 (1 + 1 = 2)`() =
        assertExecution("1,0,0,0,99", "2,0,0,0,99")

    @Test
    fun `day 2 - 2,3,0,3,99 becomes 2,3,0,6,99 (3 * 2 = 6)`() =
        assertExecution("2,3,0,3,99", "2,3,0,6,99")

    @Test
    fun `day 2 - 2,4,4,5,99,0 becomes 2,4,4,5,99,9801 (99 * 99 = 9801)`() =
        assertExecution("2,4,4,5,99,0", "2,4,4,5,99,9801")

    @Test
    fun `day 2 - 1,1,1,4,99,5,6,0,99 becomes 30,1,1,4,2,5,6,0,99`() =
        assertExecution("1,1,1,4,99,5,6,0,99", "30,1,1,4,2,5,6,0,99")

    @Test
    fun `day 5 - echo`() {
        val memory = Memory.fromString("3,0,4,0,99")
        val input = Input.of(42)
        val output = CollectingOutput()

        memory.makeComputer(input = input, output = output).execute()

        assertEquals(listOf(42), output.data)
    }

    @Test
    fun `day 5 - parameter types`() =
        assertExecution("1002,4,3,4,33", "1002,4,3,4,99")

    @Test
    fun `day 5 - negatives`() =
        assertExecution("1101,100,-1,4,0", "1101,100,-1,4,99")

    @Test
    fun `day 5 - part 1 regression`() {
        val base = Memory.fromFile(File("inputs/day5.1.txt"))
        val output = CollectingOutput()
        base.makeComputer(
            input = Input.of(1),
            output = output
        ).execute()
        assertEquals(listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 14155342), output.data)
    }

    @Test
    fun `day 5 - equals positional`() {
        val program = "3,9,8,9,10,9,4,9,99,-1,8"
        assertExecution(program, input = listOf(7), expectedOutput = listOf(0))
        assertExecution(program, input = listOf(8), expectedOutput = listOf(1))
        assertExecution(program, input = listOf(9), expectedOutput = listOf(0))
    }

    @Test
    fun `day 5 - less than positional`() {
        val program = "3,9,7,9,10,9,4,9,99,-1,8"
        assertExecution(program, input = listOf(7), expectedOutput = listOf(1))
        assertExecution(program, input = listOf(8), expectedOutput = listOf(0))
        assertExecution(program, input = listOf(9), expectedOutput = listOf(0))
    }

    @Test
    fun `day 5 - equals immediate`() {
        val program = "3,3,1108,-1,8,3,4,3,99"
        assertExecution(program, input = listOf(7), expectedOutput = listOf(0))
        assertExecution(program, input = listOf(8), expectedOutput = listOf(1))
        assertExecution(program, input = listOf(9), expectedOutput = listOf(0))
    }

    @Test
    fun `day 5 - less than immediate`() {
        val program = "3,3,1107,-1,8,3,4,3,99"
        assertExecution(program, input = listOf(7), expectedOutput = listOf(1))
        assertExecution(program, input = listOf(8), expectedOutput = listOf(0))
        assertExecution(program, input = listOf(9), expectedOutput = listOf(0))
    }

    @Test
    fun `day 5 - is zero positional`() {
        val program = "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"
        assertExecution(program, input = listOf(0), expectedOutput = listOf(0))
        assertExecution(program, input = listOf(123), expectedOutput = listOf(1))
    }

    @Test
    fun `day 5 - is zero immediate`() {
        val program = "3,3,1105,-1,9,1101,0,0,12,4,12,99,1"
        assertExecution(program, input = listOf(0), expectedOutput = listOf(0))
        assertExecution(program, input = listOf(321), expectedOutput = listOf(1))
    }

    @Test
    fun `day 5 - compare to 8`() {
        val program = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31," +
                "1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104," +
                "999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99"


        assertExecution(program, input = listOf(-42), expectedOutput = listOf(999))
        assertExecution(program, input = listOf(7), expectedOutput = listOf(999))
        assertExecution(program, input = listOf(8), expectedOutput = listOf(1000))
        assertExecution(program, input = listOf(9), expectedOutput = listOf(1001))
        assertExecution(program, input = listOf(8787), expectedOutput = listOf(1001))
    }

    private fun assertExecution(
        initial: String,
        expectedFinal: String? = null,
        input: List<Int> = listOf(),
        expectedOutput: List<Int> = listOf(),
        trace: Boolean = false
    ) {
        val output = CollectingOutput()
        val computer = Memory.fromString(initial).makeComputer(input = Input.of(*input.toIntArray()), output = output)
        computer.execute(trace = trace)

        if (expectedFinal != null) {
            val expectedMemory = Memory.fromString(expectedFinal)
            assertEquals(expectedMemory, computer.memory)
        }

        assertEquals(expectedOutput, output.data)
    }
}