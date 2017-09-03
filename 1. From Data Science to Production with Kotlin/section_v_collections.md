# Section V: Collections

Kotlin has several convenient collection types that can be implemented in a variety of ways. In this section, we will focus on arrays, lists, and maps, as well as factory patterns and collection operators.

# Section 5.1: Ranges and For-Loops

In Kotlin, you can pair two values together to create a "range". Some types support ranges like numbers, dates, and other comparables. Some ranges can be looped through.

## 5.1A: Looping through an Int Range

To create a range, put a `..` between the two values. Below, we create a range of 1 through 10, then print each value in that range with a `for` loop.

```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val range = 1..10

    for (i in range) {
        println(i)
    }
}
```

## 5.2B: Checking if a Value is in a Range

Below, we create a range with two dates, and use the `in` operator to see if it contains a certain date in that range.

```kotlin
package com.oreilly

import java.time.LocalDate

fun main(args: Array<String>) {

    val dateRange = LocalDate.of(2017,3,1)..LocalDate.of(2017,6,1)

    val testDate = LocalDate.of(2017,4,6)

    println(testDate in dateRange)
}
```

## 5.3C: Closed Ranges

Note that you cannot loop through a range of `LocalDate`, because it is a `Comparable` and therefore only works with `ClosedRange` rather than `IntRange` or other numeric ranges. We will learn how to iterate a series of dates in a range later.

```kotlin
package com.oreilly


import java.time.LocalDate

fun main(args: Array<String>) {

    val dateRange = LocalDate.of(2017,3,1)..LocalDate.of(2017,6,1)

    for (dt in dateRange) { // error, not supported
        println(dt)
    }
}
```

# Section 5.2: Arrays

An `Array` is a fixed-length collection of items that maintains their order.

# 5.2A Using an Array of Objects

Below, we create an array of vehicles and print the second item in the array.

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val samples = arrayOf(
        Vehicle("Tesla", "Model S", 2017, kilowattHours = 75),
        Vehicle("Ford", "Fusion", 2016, highwayMpg = 26),
        Vehicle("Toyota", "Camry", 2016, highwayMpg = 35)
    )

    println(samples[1])
}

data class Vehicle(val make: String,
                   val model: String,
                   val year: Int,
                   val highwayMpg: Int? = null,
                   val kilowattHours: Int? = null) {

    val isElectric = kilowattHours != null
    val isGas = highwayMpg != null
}
```

We can also loop through the items:

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val samples = arrayOf(
            Vehicle("Tesla", "Model S", 2017, kilowattHours = 75),
            Vehicle("Ford", "Fusion", 2016, highwayMpg = 26),
            Vehicle("Toyota", "Camry", 2016, highwayMpg = 35)
    )

    for (car in samples) {
        println(car)
    }
}

data class Vehicle(val make: String,
                   val model: String,
                   val year: Int,
                   val highwayMpg: Int? = null,
                   val kilowattHours: Int? = null) {

    val isElectric = kilowattHours != null
    val isGas = highwayMpg != null
}
```

## 5.3B: Looping with indices

We can also loop through with indices:

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val samples = arrayOf(
            Vehicle("Tesla", "Model S", 2017, kilowattHours = 75),
            Vehicle("Ford", "Fusion", 2016, highwayMpg = 26),
            Vehicle("Toyota", "Camry", 2016, highwayMpg = 35)
    )

    for ((car,index) in samples.withIndex()) {
        println("Item at $index is $car")
    }
}

data class Vehicle(val make: String,
                   val model: String,
                   val year: Int,
                   val highwayMpg: Int? = null,
                   val kilowattHours: Int? = null) {

    val isElectric = kilowattHours != null
    val isGas = highwayMpg != null
}
```

## 5.2C: Using an Array of Numbers

Because arrays are of fixed length, and this length must be inferred in advance, they are not used often as the amount of data expected is not always known in advance. You are more likely to use Lists, which we will learn about shortly.

Arrays are needed if you need to do numerical vector or matrix work, but more likely you will use libraries like Apache Commons Math, ND4J, Koma, and Kotlin-Statistics to do matrix work for you.

When you are using numeric types in arrays, use the array builder that is optimized for that numeric type. Below, we declare an array of Doubles and print them as a concatenated string using `joinToString()`.

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val vector = doubleArrayOf(1.0, 5.0, 6.0, 11.0)

    println(vector.joinToString(separator = ",", prefix = "[", postfix = "]"))
}
```


Note also that arrays are mutable, and any element can be modified at any given time.

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val vector = doubleArrayOf(1.0, 5.0, 6.0, 11.0)

    vector[2] = 3.0

    println(vector.joinToString(separator = ",", prefix = "[", postfix = "]"))
}
```

## 5.2D Multi-dimensional arrays

To leverage multidimensional arrays, nest `arrayOf()` calls to create as many dimensions as you need. Again, you should try to leverage libraries like Apache Commons Math, ND4J, Koma, or Kotlin-Statistics instead.

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val matrix = arrayOf(
            doubleArrayOf(1.0, 5.0, 6.0, 11.0),
            doubleArrayOf(-51.0, 91.0, 5.0, 67.1),
            doubleArrayOf(29.4, 2.1, 6.4, 65.3)
    )

    println(matrix[1][3])
}
```

# 5.3A Lists

Lists are an ordered collection of elements that, depending on whether it is mutable, can grow or shrink. Lists are a more common type in Kotlin that feel similar to arrays, and work similarly to lists in Python. The primary difference is they pack a lot more features and are immutable (unless you make an immutable list, which we will cover later in this section).

You declare an immutable List using the `listOf()` function.

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val strings = listOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    for (s in strings) {
        println(s)
    }
}
```

Lists are immutable, meaning that factories that "modify" the List will actually return new Lists. Below, we call the `reversed()` function which will return a new `List` with the Strings in reverse order.


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val strings = listOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    val reversed = strings.reversed()

    for (s in reversed) {
        println(s)
    }
}
```

## 5.3B Mutable Lists

To make a mutable List, use the `mutableListOf()` function instead. This list can have items modified, removed, and appended at any time. Try to stick with immutable lists so you don't create opportunities for mistakes.

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val strings = mutableListOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    strings[3] = "Whiskey"

    strings += "Tango" //append Tango
    strings -= "Alpha" //remove Alpha

    println(strings)
}
```

# 5.4 Sets

To only hold unique items (based on the `hashcode()`/`equals()` implementation of objects), you can use a `Set` which may or may not be ordered depending on its underlying implementation.

Below, we try to add an item to a `MutableSet` but since it already contains that item, it will ignore that addition.


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val strings = mutableSetOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    strings += "Alpha"

    println(strings)
}
```

# 5.5 Collection operators

There are many convenient collection operators that allow you to work with and transform collections easily. In _Practical Data Modeling for Production with Kotlin_, we will cover Sequences which can unlock further efficiency in transforming and loading data. But for now we will stick with the collection operators.


## 5.5A forEach()

In Kotlin, you can pass _lambda arguments_ to some functions (known as higher-order functions) to quickly pass a behavior as an argument. We will dissect what this means in more detail in the next video, but for now just know it can quickly specify an instruction on what to do with each element in a collection.

For instance, rather than using a loop to print each item in a `List`, `Array`, `Set`, or `Map`, we can simply call the `forEach()` function and pass a lambda argument saying to print each item (referred to as `it`).


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val strings = listOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    strings.forEach { println(it) }
}
```

As you start using these kinds of functions, you will find loops become a rarity in your daily work.


## 5.5B Mapping to Lengths

Below, we quickly turn a `List<String>` into a `List<Int>` by calling the `map()` function and pass a lambda argument mapping each String to its length.

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val strings = listOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    val lengths = strings.map { it.length }

    lengths.forEach { println(it) }
}
```


## 5.5C Filtering


We can also filter items that meet a condition into a new `List`. For instance, we can derive a new List that only contains Strings greater than 5 characters in length.


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val strings = listOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    val filteredStrings = strings.filter { it.length > 5 }

    filteredStrings.forEach { println(it) }
}
```

## 5.5D Getting Distinct Letters from All Strings

In the below example, we derive all the distinct letters from our six Strings. We use `flatMap()` to break up into a `List` of all the characters, and then `filter()` to remove any empty characters. Finally we use `map()` to make all letters uppercase and then use `distinct()` to get the distinct letters.


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val strings = listOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    val distinctLetters = strings.flatMap { it.split("") }
            .filter { it.trim() != "" }
            .map { it.toUpperCase() }
            .distinct()

    println(distinctLetters)
}
```



# 5.6 Maps

Maps are analagous to Dicts in Python, and they are a useful tool to quickly look up items based on a "Key" object.

Data classes (as well as Strings, Ints, Longs, Dates, and other "value" types) make good key objects because they already implement `hashcode()/equals()`.

## 5.6A

To quickly declare a map on-the-spot, you can use the `mapOf()` function.

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val map = mapOf(
            5 to "Alpha",
            6 to "Beta",
            3 to "Gamma",
            7 to "Delta",
            11 to "Epsilon"
    )

    println(map[6])
    println(map[8])
}
```


## 5.6B

If you want to load data into a map later, you can use `mutableMapOf()` to create a `Map` that can be modified. Note that if multiple values are assigned to the same key, only the last assigned value will persist.


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val strings = listOf("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot")

    val stringsByLengths = mutableMapOf<Int,String>()

    for (s in strings) {
        stringsByLengths[s.length] = s
    }

    println(stringsByLengths)
}
```

We will learn about more robust ways to load maps (including multimaps) in the next video.

## 5.6C

You can also leverage data classes as keys. Below, we retrieve "John Simone" from the `Map` by using a `NameKey`.

```kotlin
package com.oreilly


import java.time.LocalDate

fun main(args: Array<String>) {

    data class Patient(val firstName: String,
                       val lastName: String,
                       val birthday: LocalDate,
                       val whiteBloodCellCount: Int)

    data class NameKey(val firstName: String, val lastName: String)

    val patients = listOf(
            Patient("John", "Simone", LocalDate.of(1989, 1, 7), 4500),
            Patient("Sarah", "Marley", LocalDate.of(1970, 2, 5), 6700),
            Patient("Jessica", "Arnold", LocalDate.of(1980, 3, 9), 3400),
            Patient("Sam", "Beasley", LocalDate.of(1981, 4, 17), 8800),
            Patient("Dan", "Forney", LocalDate.of(1985, 9, 13), 5400),
            Patient("Lauren", "Michaels", LocalDate.of(1975, 8, 21), 5000),
            Patient("Michael", "Erlich", LocalDate.of(1985, 12, 17), 4100),
            Patient("Jason", "Miles", LocalDate.of(1991, 11, 1), 3900),
            Patient("Rebekah", "Earley", LocalDate.of(1985, 2, 18), 4600),
            Patient("James", "Larson", LocalDate.of(1974, 4, 10), 5100),
            Patient("Dan", "Ulrech",  LocalDate.of(1991, 7, 11), 6000),
            Patient("Heather", "Eisner", LocalDate.of(1994, 3, 6), 6000),
            Patient("Jasper", "Martin", LocalDate.of(1971, 7, 1), 6000)
    )

    val mappedByName = mutableMapOf<NameKey, Patient>()

    for (patient in patients) {
        val key = NameKey(patient.firstName, patient.lastName)
        mappedByName[key] = patient
    }

    val retrievedPatient = mappedByName[NameKey("John", "Simone")]
    println(retrievedPatient)
}
```

# 5.7: Factory Patterns with Companion Objects

One thing that can be helpful is to save properties and functions at the "class" level, not the instance of each object. This can be achieved using objects and companion objects.

For instance, we can have a `PatientDirectory` object embedded right inside the `Patient` class that could hold or access the entire database of patients. It can also have a helper function to retrieve a `Patient` for a given ID.


```kotlin
package com.oreilly




fun main(args: Array<String>) {

    val retreivedPatient = Patient.PatientDirectory.forId(3)

    print(retreivedPatient)
}

data class Patient(val id: Int,
                   val firstName: String,
                   val lastName: String) {

    object PatientDirectory {

        val allPatients = listOf(
                Patient(1, "John", "Mooney"),
                Patient(2, "Sam", "Bella"),
                Patient(3, "Jake", "Blaine"),
                Patient(4, "Hannah", "Smith"),
                Patient(5, "John", "Mooney")
        )

        fun forId(id: Int) = allPatients.find { it.id == id }
    }
}
```

You can also eliminate having to explicitly call the embedded object by using a `companion object` instead. This is helpful if you don't intend on having different objects in a class.

```kotlin
package com.oreilly




fun main(args: Array<String>) {

    val retreivedPatient = Patient.forId(3)

    print(retreivedPatient)
}

data class Patient(val id: Int,
                   val firstName: String,
                   val lastName: String) {

    companion object {

        val allPatients = listOf(
                Patient(1, "John", "Mooney"),
                Patient(2, "Sam", "Bella"),
                Patient(3, "Jake", "Blaine"),
                Patient(4, "Hannah", "Smith"),
                Patient(5, "John", "Mooney")
        )

        fun forId(id: Int) = allPatients.find { it.id == id }
    }
}
```
