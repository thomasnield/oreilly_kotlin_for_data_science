# Section III: Kotlin Basics

Let's write our first Kotlin application. Right-click the `src/main/kotlin` folder, click "New", and select "Kotlin file/class" to create a new Kotlin file (or just use the `hello.kt` file that was created with the archetype). Call it "Launcher" or whatever you like. This file will serve as the entry point to launch our application.

## 3.1 Hello World

In the "Launcher" file, add the following snippit:

```kotlin
package com.oreilly

fun main(args: Array<String>) {

}
```

What we just added is the `main()` function, which is an entry-point to a Kotlin application. It executes everything inside the block `{ }`. It accepts an `args` argument which will be an array of strings. These arguments can be provided from a command line or bash file to your application, which won't be using for this course.

We'll start with the obligatory "Hello World", and use `println()` to write it in the console. To run the application, click the Kotlin icon in the gutter.

```kotlin
package com.oreilly

fun main(args: Array<String>) {

    println("Hello World!")
}
```

Curly brackets are used to build blocks of code, whereas Python uses indentation. Whitespaces and indents have no impact on how the code compiles.

## 3.2A Variables

Variables in Kotlin are declared with the `val` or `var` keywords.

Below, we declare two `Int` variables, `x` and `y`. We create a third `Int` variable called `sum` which is the sum of the two.

```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val x = 10
    val y = 23

    val sum = x + y

    println(sum)
}
```

`x`, `y`, and `sum` are statically declared as `Int` types. This was inferred because they are being assigned `Int` values. You can find this out by clicking one of them and clicking `CTRL + Q`.

## 3.2B Variables (Explicit types)

We can also explicitly declare them as `Int` values like this, although it is not necessary because the values can be inferred from the assignments.


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val x: Int = 10
    val y: Int = 23

    val sum: Int = x + y

    println(sum)
}
```


## 3.2C Variables (Using mutable var)

The `val` means you cannot reassign a value to the variable.  For instance, you cannot reassign a value to `y` or `sum`. This permanent, unchanging state is known as immutablity and is something you should strive for. It is a best practice in modern programming because it minimizes bugs and accidents, and is especially embraced in functional programming.


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val x = 10
    val y = 23

    val sum = x + y

    println(sum)

    y = 10        // error, can't be reassigned
    sum = x + y   // error, can't be reassigned

    println(sum)
}
```

If you do want variables to "mutate", or change value, use the `var` keyword instead.


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    var x = 10
    var y = 23

    var sum = x + y

    println(sum)

    y = 10        // okay
    sum = x + y   // okay

    println(sum)
}
```

## 3.3 Types and Operators

Kotlin is a statically-typed language. This means you must commit a variable, property, or any item to be of one type for its entire life. This is different than dynamic languages like Python, where a variable can hold any type at any given time. While in some ways this reduces flexibility, you will reap larger benefits in clarity, reliability, and evolvability.

For instance, if you have declared a `myNumber` variable to be a number, you cannot change it to a `String` later. The compiler will not let you.

## 3.3A - Type safety

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    var myNumber = 10

    myNumber = "Alpha"  // error, must be an Int

    println(myNumber)
}
```

With static typing, the benefit you gain is greater visibility, refactorability, and reliability. This also enables productive tooling in IDEA like autocompletion and code assistance as you type.

## 3.3B - Working with dates

(View slides for basic data types)

Below, we get today's date, print it, then create another date off it that is 30 days later. Note you have to hit `ALT + ENTER` to automatically import the `LocalDate` type, just like a Python module.


```kotlin
package com.oreilly


import java.time.LocalDate

fun main(args: Array<String>) {

    val today = LocalDate.now()

    println(today)

    val thirtyDaysFromNow = today.plusDays(30L)

    println(thirtyDaysFromNow)
}
```

Note that the `LocalDate` is immutable, and the `plusDays()` function will create a new `LocalDate` off the old one.


## 3.3C - Working with BigDecimal (and String Interpolation)

Here is an example that works with money, which you often want to use the `BigDecimal` type for (for more accurate division and multiplication). You can create a `BigDecimal` off a `Double` or other numeric types using its `valueOf()` factory function.

```kotlin
package com.oreilly


import java.math.BigDecimal

fun main(args: Array<String>) {

    val balance = BigDecimal.valueOf(1808.2)
    println("BALANCE: $balance")

    val transactionAmount = BigDecimal.valueOf(56.12)
    val newBalance = balance - transactionAmount
    println("NEW BALANCE: $newBalance")
}
```

Note above how we can inject a value into a `String` to be printed by using a `$` within a literal String. This is called interpolation. You can also inject an entire expression in a string like this.

```kotlin
package com.oreilly

import java.math.BigDecimal

fun main(args: Array<String>) {

    val balance = BigDecimal.valueOf(1808.2)
    println("BALANCE: $balance")

    val transactionAmount = BigDecimal.valueOf(56.12)
    println("NEW BALANCE: ${balance - transactionAmount}")
}
```

## 3.3C - Comparing items

There are a number of operators available in Kotlin, and they support several types and custom types which we will discuss later.

(Switch to slides to see operators)

For instance, the equality operator checks if two items are equal, and will return a `Boolean` true/false value:


```kotlin
package com.oreilly

import java.time.LocalDate

fun main(args: Array<String>) {

    val date1 = LocalDate.of(2017,3,7)
    val date2 = LocalDate.of(2017,3,12)

    val isSameMonth = date1.month == date2.month

    println(isSameMonth)
}
```

Comparative operators check if one item is less than (or equal to) or greater than (or equal to) another :


```kotlin
package com.oreilly

import java.time.LocalDate

fun main(args: Array<String>) {

    val date1 = LocalDate.of(2017,3,7)
    val date2 = LocalDate.of(2017,3,12)

    val result = date1 >= date2

    println(result)
}
```

We will learn about creating our own types in the next section, and how they can support these operators.


## 3.4 - Functions

Kotlin supports functions that feel similar to Python, with a few additional conveniences.

## 3.4A - A parameterless Unit function

To declare a function, you specify it with the `fun` keyword, followed by the function name, and then paranthesis where arguments will go.

Here we create a simple function that prints a random `Int`. It accepts no arguments and returns a `Unit`, which effectively means it returns nothing.

```kotlin
package com.oreilly

import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
    printRandomInt()
}

fun printRandomInt() {
    val randomInt = ThreadLocalRandom.current().nextInt()
    println(randomInt)
}
```


## 3.4B - A function that returns a value

If your function is going to return a meaningful value, you will need to specify the return type following the paranthesis `()`, as in `fun gereateRandomInt(): Int` which will return an `Int`.


```kotlin
package com.oreilly

import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
   println(generateRandomInt())
}

fun generateRandomInt(): Int {
    return ThreadLocalRandom.current().nextInt()
}
```

## 3.4C - A single-line function

The function above only has one line in its body, so we can actually use a simpler syntax using an `=`. We can even remove the return type which it will infer.

```kotlin
package com.oreilly

import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
   println(generateRandomInt())
}

fun generateRandomInt() = ThreadLocalRandom.current().nextInt()
```

## 3.4D - A function with parameters

We can also provide parameters to a function, like a `min` and `max` for our random function. These have to be declared with specified types, which in this case both will be `Int`.

```kotlin
package com.oreilly

import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
   println(generateRandomInt(100,200))
}

fun generateRandomInt(min: Int, max: Int) =
        ThreadLocalRandom.current().nextInt(min, max + 1)
```


## 3.4E - Passing parameters by name

You can also pass the parameters in any order by invoking them by name. This is encouraged to explicitly call out which parameters you are providing, and prevents mixing up arguments.

```kotlin
package com.oreilly

import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
   println(generateRandomInt(max=200, min=100))
}

fun generateRandomInt(min: Int, max: Int) =
        ThreadLocalRandom.current().nextInt(min, max + 1)
```


## 3.4F - Default parameters

You can specify default values for parameters. You'll typically want these parameters to be declared last in the function, so you are not forced to call the needed parameters by name.

For instance, we can make the `min` parameter default to `0`.

```kotlin
package com.oreilly

import java.util.concurrent.ThreadLocalRandom

fun main(args: Array<String>) {
    println(generateRandomInt(100))
}

fun generateRandomInt(max: Int, min: Int = 0) =
        ThreadLocalRandom.current().nextInt(min, max + 1)
```

Default parameters can also refer to the other parameters to calculate their default value.



## 3.5 - Nullable Types

Kotlin has a special feature not available in other languages called nullable types. This effectively prevents errors with null values by flagging them as possibly null. This can stop a wide array of null-related errors at compile time rather than at run time.

Before we start, note that when you assign a null value to variable (without explicitly declaring its type), its type will be `Nothing`.

## 3.5A - Declaring a nullable type

```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val myValue = null  //type is `Nothing`
}
```

In this case, we would rather this `myValue`, even if it is null, be a `String`. However, if you explicitly make it an `String` type, it will throw a compile error.

```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val myValue: String = null // error
}
```

The compiler does not like indiscriminate null values, and explicitly expects you to declare items as "nullable" if they can be null. The way you do this is to make the type "nullable", and this can be done by following the type with a question mark `?`.

```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val myValue: String? = null
}
```

Now the variable is nullable. Let's see what this means.

 ## 3.5B - Null Safety

 Let's say we want to get the `length` of `myValue`. The problem is the value might be null, so calling its `length` property will throw a compile error. This is because the compiler is stopping us from a possible `NullPointerException`, and would like us to handle the possibility it is null before calling its `length`.

 ```kotlin
package com.oreilly

 fun main(args: Array<String>) {

    val myValue: String? = null

    val length = myValue.length // error

    println(length)
}
```

One way we can solve this is to use an `if` expression (covered in more detail next section) to check if the `myValue` is null. This would satisfy the compiler.

```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val myValue: String? = null

    if (myValue != null) {
        val length = myValue.length // okay

        println(length)
    }
}
```

A more idiomatic way Kotlin handles null values though is using coalescing operators, also called "safe-calls". This will only proceed to get the `length` property if the `myValue` is not null. Otherwise it will stop and just yield `null`.


```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val myValue: String? = null

    val length = myValue?.length

    println(length)
}
```

You can chain as many safe-calls as you like, which helps avoid "pyramids of doom" built off nested `if` statements.


## 3.5C - The Elvis Operator

If you want to switch a null value to another value if it is null, you can use the "Elvis" operator to achieve this quickly. It is named after the eponymous music artist for which the operator `?:` looks like.

Below, we default our operation to a length of `0` if the expression yields a null value.


```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val myValue: String? = null

    val length = myValue?.length?:0

    println(length)
}
```

```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val myValue: String? = "Foxtrot"

    val length = myValue?.length?:0

    println(length)
}
```

## 3.5C - Nullable Exceptions and the "Bang! Bang!" Operator

The "Bang! Bang!" operator, unofficially known as the "hold my beer" operator, is a brute force way to treat a nullable type as no longer nullable. The problem is it will throw a null pointer exception if the value is indeed null.

```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val myValue: String? = "Foxtrot"

    val length: Int = myValue!!.length   // okay

    println(length)
}
```


```kotlin
package com.oreilly


fun main(args: Array<String>) {

    val myValue: String? = null

    val length: Int = myValue!!.length   // runtime error

    println(length)
}
```

This is an operator you will want to avoid using because it is better to leverage the null safety that Kotlin offers, and choose more effective strategies to handle the null value. If you want to force a value to not be nullable anymore, you can throw an explicit Exception instead.

```kotlin
package com.oreilly

fun main(args: Array<String>) {

    val myValue: String? = "Foxtrot"

    val length: Int = myValue?.length ?: throw Exception("This should not be null!")

    println(length)
}
```

# 3.6 Project Navigation and Organization

Cover the following:
* Version Control Tracking 
* Files and packages
* CTRL + Q to see object types
* CTRL + B to jump to declaration
* SHIFT + F6 to rename
* Quick search and Maven pane
