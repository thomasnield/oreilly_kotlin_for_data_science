# Section IV - Flow Control and Classes

Like any capable programming language, Kotlin has operators to control program flow and logic, like `if`, `when`, and loops. However, you likely won't use these often after you discover Sequences and other functional tools in Kotlin. But they are here for your use. We will also cover pragmatic OOP for data science, and learn some practical ways you can leverage object-oriented programming with Kotlin.

Note we are not going to cover Kotlin's language and class features comprehensively, and instead focus on the parts that add immediate value for data science tasks. You can always check out https://kotlinlang.org/docs/reference/ to get a complete walkthrough of Kotlin's language features.

# 4-1: `if`

In Kotlin, there are two helpful operators to express conditional actions or values: `if` and `when`.

## 4-1A: `if` with an action

The `if` expression should be fairly familiar. It will return a value or execute an action when a condition tests true.

For example, we can print a message if a `currentSpeed` is greater than the `speedLimit`.

```kotlin
fun main(args: Array<String>) {

    val speedLimit = 65
    val currentSpeed = 40

    if (currentSpeed > speedLimit) {
        println("Exceeding speed limit!")
    }
}
```

## 4-1B: `if-else` with an action

You can also add an `else` clause to execute an action if the condition is `false`.

```kotlin
fun main(args: Array<String>) {

    val speedLimit = 65
    val currentSpeed = 40

    if (currentSpeed > speedLimit) {
        println("Exceeding speed limit!")
    }
    else {
        println("Speed is okay!")
    }
}
```

If each branch is one line, you don't need curly brackets.

```kotlin
fun main(args: Array<String>) {

    val speedLimit = 65
    val currentSpeed = 40

    if (currentSpeed > speedLimit)
        println("Exceeding speed limit!")
    else
        println("Speed is okay!")
}
```

## 4-1C: `if-else` that returns a value

The `if` expression can actually return a value (in our earlier examples, it returned a `Unit`). This means you can save the result of an `if` expression and assign it to a variable.

```kotlin
fun main(args: Array<String>) {

    val distance = 150

    val haulType = if (distance > 200) {
        "LONG HAUL"
    }
    else {
        "SHORT HAUL"
    }

    println("$distance is a $haulType")
}
```

## 4-1D: `if-else` that returns a value

If you don't need multiple lines in a block, you can shorthand this in a single line without curly brackets `{ }`.

```kotlin
fun main(args: Array<String>) {

    val distance = 150

    val haulType = if (distance > 200) "LONG HAUL" else "SHORT HAUL"

    println("$distance is a $haulType")
}
```

## 4-1D: "AND"

You can combine conditions using the "and" `&&` operator, which joins several conditions together and requires all of them to be `true`. Below we check for sleet conditions, which must have rain present and a temperature less than 32 degrees to occur.


```kotlin
fun main(args: Array<String>) {

    val isRain = true
    val temperature = 31

    if (isRain && temperature < 32) {
        println("FORECAST: Freezing sleet")
    }
}
```

## 4-1E: "OR"

The double-pipe `||` serves as an `OR` operator, which joins several conditions and requires one of them to be `true` to yield `true`. Below, we check for any snowfall or sleet conditions. Note we group up the sleet conditons in paranthesis `( )` so there is no mixup between the `AND` and `OR`.

```kotlin
fun main(args: Array<String>) {

    val isRain = false
    val temperature = 38

    val snowFall = 12
    if (snowFall > 0 || (isRain && temperature < 32)) {
        println("FORECAST: Freezing sleet or snow")
    }
}
```

# 4-2: `when` Expressions

A `when` expression is a more flexible alternative to `if` that allows you to specify multiple conditions mapped to resulting values.


## 4-2A: Measuring Wind Speed

Below we map different conditions to different `println` actions (each which return `Unit`) to categorize a wind speed.

```kotlin
fun main(args: Array<String>) {

    // solicit wind speed input
    println("Input a wind speed")
    val windSpeed = readLine()?.toInt()?: throw Exception("Please provide a wind speed!")

    // evaluate wind speed category
    when {
        windSpeed >= 40 -> println("HIGH")
        windSpeed >= 30 -> println("MODERATE")
        else -> println("LOW")
    }
}
```

## 4-2B: Measuring Wind Speed (by mapping value)

We can also use a `when` to map each condition to a value, and save that resulting value to a variable.

```kotlin
fun main(args: Array<String>) {

    // solicit wind speed input
    println("Input a wind speed")
    val windSpeed = readLine()?.toInt()?: throw Exception("Please provide a wind speed!")

    // evaluate wind speed category
    val windSeverity = when {
        windSpeed >= 40 -> "HIGH"
        windSpeed >= 30 -> "MODERATE"
        else -> "LOW"
    }

    println("$windSpeed MPH has a severity of $windSeverity")
}
```


## 4-2C: Throwing an Exception in `when`

If you want to invalidate everything that falls to the `else` clause, you can have it throw an `Exception`. This is a good way to exhaustively define valid conditions, and throw an error for anything else.

```kotlin
fun main(args: Array<String>) {

    // solicit wind speed input
    val windSpeed = readLine()?.toInt()?: throw Exception("Please provide a wind speed!")

    // evaluate wind speed category
    val windSeverity = when {
        windSpeed >= 40 -> "HIGH"
        windSpeed >= 30 -> "MODERATE"
        windSpeed >= 0 -> "LOW"
        else -> throw Exception("Wind speed must be positive!")
    }

    println("$windSpeed MPH has a severity of $windSeverity")
}
```


# 4-3 Classes

Object-oriented programming and classes have often been overlooked in data science, primarily because they traditionally require a lot of boilerplate code and reduce flexibility. This is not the case with Kotlin. Classes are a powerful tool in expressing business domains, especially with data classes which are an effective substitute for tuples.

Currently, classes don't provide direct means to translate to matrices and other tabular data structures essential for learning tasks. But classes provide a way of keeping business domain code well organized, and it's not much effort to fluently convert a class into a vector/matrix that works with data science libraries. This will be covered in _Practical Data Modeling for Production, with Kotlin_.

## 4-3A: A Basic Class

A class is an entity that is used to create instances of objects, which can be used to model things in our world. For instance, we can create a `Patient` class that holds `firstName`, `lastName`, and `birthday` properties. Each type must be explicitly declared, with a `val` or `var` keyword just like variables. We can use this to create several patients with these properties.

Note you can also provide the properties as named parameters, just like functions.

```kotlin
import java.time.LocalDate

fun main(args: Array<String>) {

    val firstPatient = Patient("Elena", "Patterson", LocalDate.of(1985, 1, 4))
    println("First patient is ${firstPatient.firstName} ${firstPatient.lastName}")

    val secondPatient = Patient(firstName="John", lastName="Payne", birthday=LocalDate.of(1981, 6, 11))
    println("Second patient is ${secondPatient.firstName} ${secondPatient.lastName}")
}

class Patient(val firstName: String, val lastName: String, val birthday: LocalDate)
```

## 4-3B: Putting functions in a class

It is possible to add functions to a class which can use the properties (or other code in your project) to produce helpful calculations. Below, we add a `getAge()` function to the `Patient` class which returns the patient's age.

```kotlin
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {

    val firstPatient = Patient("Elena", "Patterson", LocalDate.of(1985, 1, 4))

    println("${firstPatient.firstName}'s age is ${firstPatient.getAge()}")
}

class Patient(val firstName: String, val lastName: String, val birthday: LocalDate) {

    fun getAge() = ChronoUnit.YEARS.between(birthday, LocalDate.now())
}
```

Everything we learned about functions from the last section can be applied to functions inside classes.

## 4-3B: Putting Derived Properties in a class

However, the above example could be improved by using a derived property instead. Functions are often used when you expect parameters could be provided. If we are simply calculating an attribute based on no parameters at all about the item, we can express a "derived property" like so:

```kotlin
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {

    val patient = Patient("Elena", "Patterson", LocalDate.of(1985, 1, 4))

    println("${patient.firstName}'s age is ${patient.age}")
}

class Patient(val firstName: String, val lastName: String, val birthday: LocalDate) {

    val age get() = ChronoUnit.YEARS.between(birthday, LocalDate.now())
}
```

`get()` will calculate the value every time it is called. If you omit the `get()` keyword, this will calculate and persist the value once. This can be good if the value is expensive to calculate, but then it takes up memory.

```kotlin
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {

    val patient = Patient("Elena", "Patterson", LocalDate.of(1985, 1, 4))

    println("${patient.firstName}'s age is ${patient.age}")
}

class Patient(val firstName: String, val lastName: String, val birthday: LocalDate) {

    val age = ChronoUnit.YEARS.between(birthday, LocalDate.now())
}
```


## 4-4: Data Classes

When your class is often holding data (which is the case for our `Patient` class), it can be useful to declare it as a `data class` instead. This enhances the class with a lot of additional functionality, including making it print-friendly and displaying its contents.


```kotlin
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {

    val patient = Patient("Elena", "Patterson", LocalDate.of(1985, 1, 4))

    println(patient)
}

data class Patient(val firstName: String, val lastName: String, val birthday: LocalDate) {

    val age get() = ChronoUnit.YEARS.between(birthday, LocalDate.now())
}
```

The `data class` sports the following features:

* A concept of equality to see if two instances of data classes are equal based on their properties
* A `toString()` implementation that displays the contents of the object
* A `copy()` function that allows you to create new objects off the old one, and change certain properties.
* `componentN()` functions that numerically correspond to each property.

Here is a demonstration of equality. Again, the properties in the primary constructor (where the provided properties are held) drive the data class features, including equality.

```kotlin
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {

    val firstPatient = Patient("Elena", "Patterson", LocalDate.of(1985, 1, 4))
    val secondPatient = Patient("Elena", "Patterson", LocalDate.of(1985, 1, 4))
    val thirdPatient = Patient("Alex", "Johnson", LocalDate.of(1981, 3, 15))

    if (firstPatient == secondPatient)
        println("firstPatient and secondPatient are duplicates!")

    if (firstPatient != thirdPatient)
        println("firstPatient and thirdPatient are not duplicates!")

}

data class Patient(val firstName: String, val lastName: String, val birthday: LocalDate) {

    val age get() = ChronoUnit.YEARS.between(birthday, LocalDate.now())
}
```

Here is a demonstration of copying-and-modifying a `Patient`.


```kotlin
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {

    val patient = Patient("Elena", "Patterson", LocalDate.of(1985, 1, 4))

    val modifiedPatient = patient.copy(lastName = "Connors")

    println(patient)
    println(modifiedPatient)
}

data class Patient(val firstName: String, val lastName: String, val birthday: LocalDate) {

    val age get() = ChronoUnit.YEARS.between(birthday, LocalDate.now())
}
```

This copy-and-modify patterns maintains immutability, which is preferable and can help prevent accidental mutations.

# 4-5 Leveraging Initializers

Sometimes you may use an `init { }` block to further initialize your class with certain operations like loading data or performing validation. Below, we have a `Vehicle` class that needs either a `highwayMpg` or a `kilowattHours`, but it cannot be assigned both. We use the `init` block below to throw an error if these rules are broken.

```kotlin
fun main(args: Array<String>) {

    val firstVehicle = Vehicle("Tesla", "Model S", 2017, kilowattHours = 75)
    val secondVehicle = Vehicle("Ford", "Fusion", 2016, highwayMpg = 26, kilowattHours = 38)
}

data class Vehicle(val make: String,
                   val model: String,
                   val year: Int,
                   val highwayMpg: Int? = null,
                   val kilowattHours: Int? = null) {

    init {
        if (highwayMpg == null && kilowattHours == null)
            throw Exception("A highwayMpg or kilowattHours must be assigned!")

        if (highwayMpg != null && kilowattHours != null)
            throw Exception("highwayMpg and kilowattHours cannot both be assigned!")
    }

    val isElectric = kilowattHours != null
    val isGas = highwayMpg != null
}
```

# 4-6 Singletons

Sometimes we want to only have a single instance of a given class and make it easily accessible everywhere. We can do this using the `object` keyword instead of `class`, and it will have all the features of a class other than its restricted to a single instance.

For instance, we may set universal arguments for some business parameters, like model constants. Here, we create a `ModelArguments` object that contains two mutable properties that can be reassigned at any time.

```kotlin

fun main(args: Array<String>) {

    ModelArguments.minimumAccuracy = .20
    ModelArguments.targetAccuracy = .60

    val correctPredictions = 205
    val totalPredictions =  500

    val accuracy = correctPredictions.toDouble() / totalPredictions.toDouble()

    println(ModelArguments.meetsTarget(accuracy))
}

object ModelArguments {
    var minimumAccuracy = 0.0
    var targetAccuracy = 0.0

    fun meetsTarget(accuracy: Double) = accuracy >= targetAccuracy
    fun meetsMinimum(accuracy: Double) = accuracy >= minimumAccuracy
}
```

# 4-7 Enums


Enums are kind of like Singletons, but are restricted to a few specified instances rather than just one. They can be useful to define a type that only has a strict set of values. For example, instead of using a "MALE" and "FEMALE" as strings, you can make this an enum type instead that allows no other values (whereas Strings can allow any value).

```kotlin
fun main(args: Array<String>) {

    val patient = Patient(firstName = "John", lastName = "Mooney", gender = Gender.MALE)

    println(patient.gender)

}

data class Patient(val firstName: String,
                   val lastName: String,
                   val gender: Gender)


enum class Gender {
    MALE,
    FEMALE
}
```

Enums can be much more complex and have abstract functions with different implementations, but we will keep enums simple for our purposes. Since enums are classes, we can specify properties for each instance. Below, we supply a "chromosomes" property which holds a `String` of "XX" or "XY" for `FEMALE` and `MALE` respectively.

```kotlin
fun main(args: Array<String>) {

    val patient = Patient(firstName = "John", lastName = "Mooney", gender = Gender.MALE)

    println(patient.gender.chromosomes)
}

data class Patient(val firstName: String,
                   val lastName: String,
                   val gender: Gender)


enum class Gender(val chromosomes: String) {
    MALE("XY"),
    FEMALE("XX")
}
```
