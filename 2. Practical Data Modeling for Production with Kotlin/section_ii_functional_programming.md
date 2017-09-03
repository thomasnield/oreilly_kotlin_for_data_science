# Section II: Functional Programming with Kotlin

Kotlin has adopted several functional programming ideas from languages like C# and Scala, but streamlines and improves them in many practical ways.

## 2.1 - Understanding Higher Order Functions

We actually have used higher order functions already when using lambda arguments. The way y

## 2.1A - A simple mapping function

We can use higher-order functions to turn a `String` somehow into a `Int`, but allow the user to define a lambda on how to do that. For instance, we can pass a lambda that maps a `String` to its length.

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

fun main(args: Array<String>) {

    val myString = "Foxtrot"

    val length = mapStringToInt(input = myString, mapper = { it.length })

    println(length)
}

fun mapStringToInt(input: String, mapper: (String) -> Int) = mapper(input)ut)
```
## 2.1B - Lambdas as last argument

If a lambda is the last argument, you can put it outside the rounded paranthesis like so:

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

fun main(args: Array<String>) {

    val myString = "Foxtrot"

    val length = mapStringToInt(myString) { it.length }

    println(length)
}

fun mapStringToInt(input: String, mapper: (String) -> Int) = mapper(input)
```

## 2.1C - Inlining Functions

When passing lambda arguments, you can sometimes get greater efficiency by inlining functions. This basically means injecting the resulting bytecode of the lambda into its target, and therefore eliminating the lambda object upon compilation.

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

fun main(args: Array<String>)
    val myString = "Foxtrot"

    val length = mapStringToInt(myString) { it.length }

    println(length)
}

inline fun mapStringToInt(input: String, crossinline mapper: (String) -> Int) = mapper(input)
```

If you are unsure whether you can inline or not, try it out and see if an error or warning occurs. If there isn't any, then you can take advantage of inlining. You can further read about the benefits of inlining in the Kotlin Reference:

https://kotlinlang.org/docs/reference/inline-functions.html


## 2.2A - Lambda Syntax

There are other syntaxes you can use to define lambdas. The simplest one is the `it` keyword referring to the single paramter input, but there are times you need to be more explicit.

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

val length = mapStringToInt(myString) { it.length }
val length = mapStringToInt(myString) { s -> s.length }
val length = mapStringToInt(myString) { s: String -> s.length }
val length = mapStringToInt(myString, String::length))
```

## 2.2B - Multiple parameter lambdas

If you have multiple parameters in your lambda, separate them by commas and use the arrow syntax.

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

fun main(args: Array<String>) {

    val str1 = "Alpha"
    val str2 = "Beta"

    val combineMethod1 = combineStrings(str1, str2) { s1, s2 -> s1 + s2 }
    println(combineMethod1)

    val combineMethod2 = combineStrings(str1, str2) { s1, s2 -> "$s1-$s2"}
    println(combineMethod2)
}

fun combineStrings(str1: String, str2: String, combiner: (String,String) -> String) = combiner(str1, str2)
```

You can read more about lambda syntax on the Kotlin Reference:

https://kotlinlang.org/docs/reference/lambdas.html

##  2.3 Generics

If you want to support a lambda accepting any given type and/or return9ing any given type, you can leverage generics to increase flexiblity.

## 2.3A: A Generic Higher Order Function

For instance, we can take a `String` input and map it to any given type `R`. Generic types are often defined as a single uppercase letter, and you must put before the function name in a diamond `<R>`.

Below, we take a `dateString` of `2017-01-01` and use a `mapString()` function to convert it a `LocalDate` as well as an `Int` (it's length).

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

import java.time.LocalDate

fun main(args: Array<String>) {

    val dateString = "2017-01-01"

    val date = mapString(dateString) { LocalDate.parse(it) }
    println(date)

    val length = mapString(dateString) { it.length }
    println(length)
}

inline fun <R> mapString(input: String, crossinline mapper: (String) -> R) = mapper(input)
```

This is not dynamically typed. The compiler was smart enough to see `R` was a `LocalDate` in the first call, and an `Int` in the second call. It will enforce these types as they were inferred.

## 2.3B: Multiple Generic Types in a Higher Order Function

You can have multiple generic types for a given function as well. For instance, we can accept a parameter of any given type `T` and return any type `R`. Below, we use a `map()` function that maps any arbitrary type `T` to an arbitrary type `R`. It can be used to turn a `String` into a `LocalDate` as well as an `Int` into a `String`.

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

import java.time.LocalDate

fun main(args: Array<String>) {

    //map a String to a LocalDate
    val dateString = "2017-01-01"
    val date = map(dateString) { LocalDate.parse(it) }
    println(date)

    //map an Int to a String
    val myNumber = 4
    val myNumberAsString = map(myNumber) {
        when (it) {
            1 -> "ONE"
            2 -> "TWO"
            3 -> "THREE"
            4 -> "FOUR"
            5 -> "FIVE"
            6 -> "SIX"
            7 -> "SEVEN"
            8 -> "EIGHT"
            9 -> "NINE"
            else -> throw Exception("Only 1 through 9 supported")
        }
    }

    println(myNumberAsString)
}

inline fun <T,R> map(input: T, crossinline mapper: (T) -> R) = mapper(input)
```

To learn more about generics in depth, go to the Kotlin Reference:
https://kotlinlang.org/docs/reference/generics.html


## 2.4 Sequences

Sometimes a chain of collection operators can expensively create intermediary collections. Therefore, in cases where multiple operations are being processed it can be better to use a `Sequence`. A `Sequence` is much like an assembly line processing one item downstream at a time.

There are many, many operators you can leverage in a `Sequence`, and you can view them all in the Kotlin reference:

https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html

If you really enjoy this style of programming where items are fluently processed in a pipeline, there are similar tools available such as Java 8 Streams (which support efficient parallelization) as well as RxJava Observables and Flowables (which are not only concurrent but handle both data and events).

## 2.4A Sequence Example

A `Sequence` is lazy and does not execute until certain terminator operators are called (like `toList()`, `average()`, `count()`, etc).

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

fun main(args: Array<String>) {

    val codeWords = sequenceOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    val averageLength = codeWords.map { it.length }
            .distinct()
            .average()

    println(averageLength)
}
```

## 2.4B Sequence Example with flatMap()

Pretty much any collection or Iterable type will have an `asSequence()` function that will iterate the elements as a `Sequence`. Some sources can only be iterated once. Others multiple times. You can also use `flatMap()` to turn an element into a `Sequence` of other elements, and merge all those resulting sequeneces together.

Below, we take a `List<String>` and turn it into a `Sequence` that is turned into a `Sequence` of its letters, then we `distinct` them and collect them into another `List`.

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

fun main(args: Array<String>) {

    val codeWords = listOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    val distinctLetters = codeWords.asSequence()
            .flatMap { it.toCharArray().asSequence() }
            .map { it.toUpperCase() }
            .distinct()
            .toList()

    println(distinctLetters)
}
```


## 2.5 `let()` and `apply()`

Kotlin has a few higher order functions available on every single type. Two we will cover are `let()` and `apply()`.


## 2.5A `let()`


The `let()` operator will take any given type `T` and turn it into a type `R`. It certainly is not critical, but it is a very nice convenience that can save you from declaring intermediary variables for values that need to be referred multiple times.

For instance, let's say you want to uppercase a `String` and then concatenate it to its reversed counterpart (essentially mirroring it). You may have to save an `uppercased` variable to do this.

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

fun main(args: Array<String>) {

    val myString = "Bravo"

    val uppercased = myString.toUpperCase()

    val mirrored  = uppercased + "|" + uppercased.reversed()

    println(mirrored)
}
```

But if you use the `let()`, you can avoid that step of saving the `uppercased` variable, and instead immediately calling `let()` on the result of `uppercase()` to immediately transform it to something else.


```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

fun main(args: Array<String>) {

    val myString = "Bravo"

    val mirrored = myString.toUpperCase().let { it + "|" + it.reversed() }

    println(mirrored)
}
```

## 2.5B `let()` for quick printing

You can also use `let()` to quickly print the result of a long chain of operations rather than saving the result to a variable and printing it.

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

fun main(args: Array<String>) {

    val codeWords = sequenceOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    codeWords.flatMap { it.toCharArray().asSequence() }
            .map { it.toUpperCase() }
            .distinct()
            .joinToString(separator = ",")
            .let { println(it) }
}
```

So in a nutshell, `let()` can help keep your code fluent and avoid creating several intermediary variables.


## 2.5C `apply()`

The `apply()` is kind of like `let()`, but it will refer to the object it is called on as `this` rather than `it`, making the lambda operate as if its inside that class.

It can be helpful to declare an object but then immediately manipulate it, without breaking the declaration flow.

Below, we have two examples of `apply()`. The first `apply()` call will do further manipulations with the returned `LocalDate` before assigning it to the `todaysDate` variable. The second will construct a `MyProcess` instance but immediately call its `run()` function before assigning it to the `immediateProcess` variable.

```kotlin
package com.oreilly
\npackage com.oreilly
\r\npackage com.oreilly

import java.time.LocalDate
import java.time.LocalTime

fun main(args: Array<String>) {

    val todaysDate = LocalDate.now().apply {
        //do stuff with LocalDate before assigning to todaysDate variable
        println("Constructed today's date, which is $month $dayOfMonth, $year")

        val tomorrow = plusDays(1)
        println("Tomorrow is ${tomorrow.month} ${tomorrow.dayOfMonth}, ${tomorrow.year}")
    }

    val immediateProcess = MyProcess().apply {
        // kick off the run() function immediately before assigning to the immediateProcess variable
        run()
    }
}

class MyProcess {

    init {
        println("Constructed MyProcess")
    }

    fun run() {
        println("Starting process at ${LocalTime.now()}")
    }
}
```
