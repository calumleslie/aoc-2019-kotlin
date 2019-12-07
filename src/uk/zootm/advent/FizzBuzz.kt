package uk.zootm.advent

fun main() {
    for(i in 1..100) {
        println(when {
            i % 3 == 0 && i % 5 == 0 -> "FizzBuzz"
            i % 3 == 0 -> "Fizz"
            i % 5 == 0 -> "Buzz"
            else -> i
        })
    }
}