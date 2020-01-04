package uk.zootm.advent.intcode

import uk.zootm.advent.parseExecDump
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    private fun assertExecution(initial: String, expectedFinal: String) {
        val computer = Computer.fromString(initial)
        computer.execute()

        val expected = Computer.fromString(expectedFinal)
        assertTrue(expected.memoryEquals(computer), "final state $computer != $expected")
    }
}