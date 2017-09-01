# Section III: Adapting Kotlin to your Domain

One of the more modern, useful features of Kotlin is it's ability to quickly adapt libraries and even the language itself so it streamlines towards your business domain. These ideas are not new as C# and Groovy pioneered a couple of these features, but Kotlin streamlines them into a single, seamless platform.

## 3.1 - Extension Functions

Have you ever wanted to "add" functions or properties to an object, even if it's in a library you don't own? You may have done this in Python before, but it can quickly become messy because you are mutating the behaviors and attributes of an object. Kotlin can achieve this for the most part, without actually modifying the object itself.

## 3.1A - Extension Functions

Sometimes it can be helpful to add functions to types we do not own, or we do own but don't want to clutter by actually adding them. Kotlin extension functions allow us to "add" a function to class but it will actually compile it as a helper function, not physically add it to the class.

For instance, say when we are working with dates we are frequently calculating the start date of the week containing that date. This could be helpful for reporting or grouping up items by week.

Rather than creating a helper function, we can use an extension function that will attach itself directly to the `LocalDate` type.

```Kotlin
import java.time.DayOfWeek
import java.time.LocalDate

fun main(args: Array<String>) {

    val myDate = LocalDate.of(2017,8,31)

    val weekStartDate = myDate.startOfWeek()

    println(weekStartDate)
}

fun LocalDate.startOfWeek() = (0..6).asSequence()
            .map { this.minusDays(it.toLong()) }
            .first { it.dayOfWeek == DayOfWeek.MONDAY }

```

The body of the function will refer to the object it has been applied on as `this`, acting as if its scope is inside the class. However, extension functions only have access to code of that type that has been made public and can't access anything that is private.


## 3.1B - Extension Function with Parameters

Extension functions behave just like functions otherwise, other than they are targeting that type they are being applied to. We can therefore use parameters and default parameters.

```Kotlin
import java.time.DayOfWeek
import java.time.LocalDate

fun main(args: Array<String>) {

    val myDate = LocalDate.of(2017,8,31)

    val weekStartDate = myDate.startOfWeek(DayOfWeek.SUNDAY)

    println(weekStartDate)
}

fun LocalDate.startOfWeek(startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY) = (0..6).asSequence()
            .map { this.minusDays(it.toLong()) }
            .first { it.dayOfWeek == startDayOfWeek }
```

## 3.1C - Extension Properties

If your extension function takes arguments or could take arguments, you should keep it a function. But if you never foresee needing any arguments, you can use an extension property instead. Below, we make `startOfWeek` an extension property that will always be starting on Monday.

```kotlin
import java.time.DayOfWeek
import java.time.LocalDate

fun main(args: Array<String>) {

    val myDate = LocalDate.of(2017,8,31)

    val weekStartDate = myDate.startOfWeek

    println(weekStartDate)
}

val LocalDate.startOfWeek get() = (0..6).asSequence()
            .map { this.minusDays(it.toLong()) }
            .first { it.dayOfWeek == DayOfWeek.MONDAY }
```

## 3.2 - Operator and Infix Functions

You can leverage operator symbols and to some degree custom symbols to create more intuitive operator syntaxes for a type.


## 3.2 - Operator Functions

For instance, you cannot add an Integer to a `LocalDate` object to add that many days to it. But if you use the operator `plus()` function, you can achieve this.

```kotlin
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {

    val myDate = LocalDate.of(2017,8,31)

    val tomorrow = myDate + 1

    println(tomorrow)
}

operator fun LocalDate.plus(days: Int) = plusDays(days.toLong())
operator fun LocalDate.minus(days: Int) = minusDays(days.toLong())
```

Here are the operator functions you can leverage:

|Symbol Signature|Operator Function|
|---|---|
|a + b|a.plus(b)|
|a - b|a.minus(b)|
|a / b|a.div(b)|
|a * b|a.times(b)|
|a % b|a.rem(b)|
|a..b|a.rangeTo(b)|
|a -= b|	a.minusAssign(b)
|a *= b|	a.timesAssign(b)
|a /= b|	a.divAssign(b)
|a %= b|	a.remAssign(b), a.modAssign(b) (deprecated)
|a in b|b.contains(a)|
|a !in b|!b.contains(a)|
|a[i]|	a.get(i)
|a[i, j]|	a.get(i, j)
|a[i_1, ..., i_n]|	a.get(i_1, ..., i_n)
|a[i] = b|	a.set(i, b)
|a[i, j] = b|	a.set(i, j, b)
|a[i_1, ..., i_n] = b|	a.set(i_1, ..., i_n, b)



## 3.3 - DSL's and Builders

A cool feature that you can leverage with Kotlin are DSL's (domain specific languages). This essentially allows you to cleverly use Kotlin to create a programming language specific to your business. This can be a helpful way to parameterize models and inputs, even accessible to nontechnical users.

Below is a DSL to build the inputs for a hotel pricing model.

```kotlin
import java.math.BigDecimal

fun main(args: Array<String>) {

    hotel {

        rooms {
            queen(quantity = 60)
            doublequeen(quantity = 60)

            king(quantity = 20)
            doubleking(quantity = 20)
        }

        prices {
            price(daysBeforeStay = 0..4, priceRange = 170.01..200.00)
            price(daysBeforeStay = 5..10, priceRange = 150.01..170.00)
            price(daysBeforeStay = 11..20, priceRange = 110.01..150.00)
            price(daysBeforeStay = 21..60, priceRange = 75.00..110.00)
        }
    }
}
```

And here is the backing implementation. We are using a special lambda type called a receiver function, which is a lambda that targets a "receiving object". For instance `PriceBuilder.() -> Unit` is a parameterless function that targets actions against a `PriceBuilder`. The function will have a `this` scope against the `PriceBuilder`.

```kotlin

fun hotel(op: HotelBuilder.() -> Unit): HotelBuilder {
    val newHotelModel = HotelBuilder()

    newHotelModel.op()

    return newHotelModel
}


class HotelBuilder {

    private val availableRooms = mutableListOf<Room>()
    private val availablePrices = mutableListOf<Price>()

    fun rooms(op: RoomBuilder.() -> Unit) {
        val builder = RoomBuilder()
        builder.op()
        availableRooms += builder.roomQueue
    }

    fun prices(op: PriceBuilder.() -> Unit) {
        val builder = PriceBuilder()
        builder.op()
        availablePrices += builder.prices
    }

    fun executeOptimization() {
        println("Input contains ${availableRooms.size} rooms and ${availablePrices.size} price ranges.")
        println("Executing optimization operations...")
    }
}

class Room(val bedType: BedType, val isDouble: Boolean = false)

class Price(val daysBeforeStayRange: IntRange, val priceRange: ClosedRange<BigDecimal>)

enum class BedType {
    KING,
    QUEEN
}

class RoomBuilder {
    val roomQueue = mutableListOf<Room>()

    fun queen(quantity: Int) = add(BedType.QUEEN, quantity, false)
    fun king(quantity: Int) = add(BedType.KING, quantity, false)
    fun doublequeen(quantity: Int) = add(BedType.QUEEN, quantity, true)
    fun doubleking(quantity: Int) = add(BedType.KING, quantity, true)

    fun add(bedType: BedType, quantity: Int, isDouble: Boolean)  =
            (1..quantity).asSequence()
                .map { Room(bedType, isDouble) }
                .forEach { roomQueue += it }
}

class PriceBuilder {
    val prices = mutableListOf<Price>()

    fun price(daysBeforeStay: IntRange, priceRange: ClosedRange<Double>) {
        prices += Price(
                daysBeforeStay,
                BigDecimal.valueOf(priceRange.start)..BigDecimal.valueOf(priceRange.endInclusive)
        )
    }
}
```

It is easy to get carried away with DSL's, and they shouldn't be used for the sake of. Only use them if they significantly streamline the creation of complex structures that often serve as inputs. They can also be helpful to streamline certain API's and libraries that often have nested items, [like user interfaces with nested controls (see the TornadoFX library)](https://github.com/edvin/tornadofx) or [defining static data structures like HTML](https://github.com/Kotlin/kotlinx.html).
