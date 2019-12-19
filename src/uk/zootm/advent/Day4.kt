package uk.zootm.advent

import kotlin.test.assertFalse

class Combination(private val digits: IntArray) {
    companion object Builder {
        fun from(value: Int): Combination {
            // Could represent 0-prefixed numbers as 6 digits but it's harder and puzzle input does not include them
            if (value !in (100000..999999)) {
                throw IllegalArgumentException("Must be 6-digit number but got $value")
            }
            val result = IntArray(6)
            var pos = 5
            var remaining = value
            while (pos >= 0) {
                result[pos] = remaining % 10
                remaining /= 10
                pos--
            }
            return Combination(result)
        }
    }

    // Part 1
    fun meetsCriteria() = hasConsecutiveDigits() && isNonDecreasing()

    // Part 2
    fun meetsExtendedCriteria() = hasListOfSize2() && isNonDecreasing()

    private fun isNonDecreasing() = !anyConsecutivePair { a, b -> a > b }
    private fun hasConsecutiveDigits() = anyConsecutivePair { a, b -> a == b }

    private fun hasListOfSize2(): Boolean {
        var previous = -1
        var length = 0
        for(current in digits) {
            if(previous == current) {
                length++
            } else {
                if( length == 2 ) {
                    // End of a 2-digit stream
                    return true
                }
                length = 1
            }
            previous = current
        }
        // Was the last stream of length 2?
        return length == 2
    }

    private fun anyConsecutivePair(predicate: (Int, Int) -> Boolean) =
        (0 until digits.size - 1).any { predicate.invoke(digits[it], digits[it + 1]) }

    override fun toString(): String {
        return digits.joinToString(separator = "")
    }
}


fun main() {
    assert(Combination.from(111111).meetsCriteria())
    assertFalse(Combination.from(223450).meetsCriteria())
    assertFalse(Combination.from(123789).meetsCriteria())

    println((356261..846303)
        .filter { Combination.from(it).meetsCriteria() }
        .count())

    assert(Combination.from(112233).meetsExtendedCriteria())
    assertFalse(Combination.from(123444).meetsExtendedCriteria())
    assert(Combination.from(111122).meetsExtendedCriteria())
    assert(Combination.from(112222).meetsExtendedCriteria())

    println((356261..846303)
        .filter { Combination.from(it).meetsExtendedCriteria() }
        .count())
}