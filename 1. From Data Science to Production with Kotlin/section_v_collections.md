# Section V: Collections

Kotlin has several convenient collection types that can be implemented in a variety of ways. In this section, we will focus on arrays, lists, and maps, as well as factory patterns and collection operators.

# Section 5-1: Arrays

An `Array` is a fixed-length collection of items that maintains their order.

# 5-1A Using an Array of Objects

Below, we create an array of vehicles and print the second item in the array.

```kotlin
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

## 5-1B Using an Array of Numbers

Because arrays are of fixed length, and this length must be inferred in advance, they are not used often as the amount of data expected is not always known in advance. You are more likely to use Lists, which we will learn about shortly.

Arrays are needed if you need to do numerical vector or matrix work, but more likely you will use libraries like Apache Commons Math, ND4J, Koma, and Kotlin-Statistics to maximize matrix work. When you are using numeric types in arrays, use the array builder that is optimized for that numeric type. Below, we declare an array of Doubles and print them as a concatenated string using `joinToString()`.

```kotlin
fun main(args: Array<String>) {

    val vector = doubleArrayOf(1.0, 5.0, 6.0, 11.0)

    println(vector.joinToString(separator = ",", prefix = "[", postfix = "]"))
}
```

To leverage multidimensional arrays, nest `arrayOf()` calls to create as many dimensions as you need. Again, this wil not create the optimal implementation and you should try to leverage libraries like Apache Commons Math, ND4J, Koma, or Kotlin-Statistics instead.

```kotlin
fun main(args: Array<String>) {

    val matrix = arrayOf(
            doubleArrayOf(1.0, 5.0, 6.0, 11.0),
            doubleArrayOf(-51.0, 91.0, 5.0, 67.1),
            doubleArrayOf(29.4, 2.1, 6.4, 65.3)
    )

    println(matrix[1][3])
}
```

# 5-2 Lists
