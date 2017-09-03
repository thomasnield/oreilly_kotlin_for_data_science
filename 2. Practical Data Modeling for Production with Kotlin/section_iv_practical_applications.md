# IV. Practical Applications

In this section, we will explore a number of applications of Kotlin for data science and data engineering tasks, and also apply our knowledge of Kotlin to streamline libraries for our purposes where it makes sense.

## 6.1 Ranking Mutual Friends in a Social Network

Here is an analytics task: take the existing friendship connections of a social network, and recommend new connections based on the number of mutual friends between people. With Kotlin, we can use a combination of object-oriented and functional programming to achieve this.

```kotlin
fun main(args: Array<String>) {

    //Retrieve users "John" and "Billy"
    val user1 = users.first { it.firstName == "John" }
    val user2 = users.first { it.firstName == "Billy" }

    // find mutual friends between "John" and "Scott"
    user1.mutualFriendsOf(user2)
            .forEach { println(it) }

    //see recommended friends for John
    user1.recommendedFriends()
            .forEach { println(it) }
}

data class SocialUser(
        val userId: Int,
        val firstName: String,
        val lastName: String
) {
    val friends get() = friendships.asSequence()
            .filter { userId == it.first || userId == it.second }
            .flatMap { sequenceOf(it.first, it.second) }
            .filter { it != userId }
            .map { friendId ->
                users.first { it.userId == friendId }
            }.toList()

    fun mutualFriendsOf(otherUser: SocialUser) =
            friends.asSequence().filter { it in otherUser.friends }.toList()

    fun recommendedFriends() = users.asSequence()
            .filter { it.userId != userId } // only look for other users
            .filter { it !in friends } // filter to people not already friends with
            .map { otherUser -> // package up mutual friends
                MutualFriendships(this, otherUser, mutualFriendsOf(otherUser).toList())
            }.filter { it.mutualFriends.count() > 0 } // omit where no MutualFriendships exist
            .sortedByDescending { it.mutualFriends.count() } // sort greatest number of mutual friendships first
}

data class MutualFriendships(val user: SocialUser, val otherUser: SocialUser, val mutualFriends: List<SocialUser>)

val users = listOf(
        SocialUser(1, "John", "Scott"),
        SocialUser(2, "Hannah", "Earley"),
        SocialUser(3, "Timothy", "Tannen"),
        SocialUser(4, "Scott", "Marcum"),
        SocialUser(5, "Sid", "Maddow"),
        SocialUser(6, "Rachel", "Zimmerman"),
        SocialUser(7, "Heather", "Timmers"),
        SocialUser(8, "Casey", "Crane"),
        SocialUser(9, "Billy", "Awesome")
)

val friendships = listOf(
        1 to 6,
        1 to 5,
        1 to 8,
        2 to 3,
        2 to 9,
        2 to 6,
        3 to 8,
        3 to 2,
        3 to 7,
        4 to 1,
        4 to 2,
        4 to 9,
        5 to 7,
        9 to 2,
        8 to 4,
        6 to 9
)
```

Above, we retrieve two users quickly using the `first()` operator, and then demonstrate the mutual friends between them. Finally, we show friendship recommendations for "John" using a ranking of non-friends that he has mutual friendships with.


## 6.2 Kotlin for Apache Spark

To use Apache Spark with a minimal configuration, add the following dependency to your `pom.xml`.

```xml
<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-core_2.11</artifactId>
    <version>2.2.0</version>
</dependency>
```

## 6.2A - Basic Spark Example

Since Apache Spark is a JVM library, there is no special configuration you will need to get it up-and-running.

```kotlin
package com.oreilly

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext

fun main(args: Array<String>) {

    val conf = SparkConf()
            .setMaster("local")
            .setAppName("Kotlin Spark Test")

    val sc = JavaSparkContext(conf)

    val items = listOf("123/643/7563/2134/ALPHA", "2343/6356/BETA/2342/12", "23423/656/343")

    val input = sc.parallelize(items)

    val sumOfNumbers = input.flatMap { it.split("/").iterator() }
            .filter { it.matches(Regex("[0-9]+")) }
            .map { it.toInt() }
            .reduce {total,next -> total + next }

    println(sumOfNumbers)
}
```

## 6.2B - Registering Kotlin Classes to Spark

If you have embraced the content in this video series so far, you will likely be using classes often to keep your data structures organized and refactorable. The recommended way to "register" your classes with Apache Spark, so that they can be used across all nodes, is to use a modern serialization solution like Kryo. Add Kryo as a dependency like so:

``xml
<dependency>
    <groupId>com.esotericsoftware</groupId>
    <artifactId>kryo</artifactId>
    <version>4.0.1</version>
</dependency>
```

Since Apache Spark only recognizes Scala and Java classes, and not Kotlin ones, create a quick extension function to convert Kotlin classes to Java before registering them.

```Kotlin
package com.oreilly

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import kotlin.reflect.KClass

fun main(args: Array<String>) {

    class MyItem(val id: Int, val value: String)

    val conf = SparkConf()
            .setMaster("local")
            .setAppName("Line Counter")

    conf.registerKryoClasses(MyItem::class)

    val sc = JavaSparkContext(conf)

    val items = listOf(MyItem(1,"Alpha"), MyItem(2,"Beta"))

    val input = sc.parallelize(items)

    val letters = input.flatMap { it.value.split(Regex("(?<=.)")).iterator() }
            .map(String::toUpperCase)
            .filter { it.matches(Regex("[A-Z]")) }

    println(letters.collect())
}

//extension function to register Kotlin classes
fun SparkConf.registerKryoClasses(vararg args: KClass<*>) = registerKryoClasses(args.map { it.java }.toTypedArray())
```

## 6.3 Using Kotlin Statistics

Bring in "Kotlin Statistics" as a dependency:

```xml
<dependency>
    <groupId>org.nield</groupId>
    <artifactId>kotlinstatistics</artifactId>
    <version>0.3.0</version>
</dependency>
```

Kotlin statistics is a a helpful library to do data analytics in a Kotlin-esque way, leveraging a combination of object-oriented and fluent functional programming.


## 6.3A - Simple Reductions

It has simple reduction operations such as standard deviations for an entire data set:

```kotlin
import org.nield.kotlinstatistics.standardDeviation
import java.time.LocalDate

data class Patient(val firstName: String,
                   val lastName: String,
                   val gender: Gender,
                   val birthday: LocalDate,
                   val whiteBloodCellCount: Int)


val patients = listOf(
        Patient("John", "Simone", Gender.MALE, LocalDate.of(1989, 1, 7), 4500),
        Patient("Sarah", "Marley", Gender.FEMALE, LocalDate.of(1970, 2, 5), 6700),
        Patient("Jessica", "Arnold", Gender.FEMALE, LocalDate.of(1980, 3, 9), 3400),
        Patient("Sam", "Beasley", Gender.MALE, LocalDate.of(1981, 4, 17), 8800),
        Patient("Dan", "Forney", Gender.MALE, LocalDate.of(1985, 9, 13), 5400),
        Patient("Lauren", "Michaels", Gender.FEMALE, LocalDate.of(1975, 8, 21), 5000),
        Patient("Michael", "Erlich", Gender.MALE, LocalDate.of(1985, 12, 17), 4100),
        Patient("Jason", "Miles", Gender.MALE, LocalDate.of(1991, 11, 1), 3900),
        Patient("Rebekah", "Earley", Gender.FEMALE, LocalDate.of(1985, 2, 18), 4600),
        Patient("James", "Larson", Gender.MALE, LocalDate.of(1974, 4, 10), 5100),
        Patient("Dan", "Ulrech", Gender.MALE, LocalDate.of(1991, 7, 11), 6000),
        Patient("Heather", "Eisner", Gender.FEMALE, LocalDate.of(1994, 3, 6), 6000),
        Patient("Jasper", "Martin", Gender.MALE, LocalDate.of(1971, 7, 1), 6000)
)

enum class Gender {
    MALE,
    FEMALE
}

fun main(args: Array<String>) {

    val averageWbcc =
            patients.map { it.whiteBloodCellCount }.average()

    val standardDevWbcc =
            patients.map { it.whiteBloodCellCount }.standardDeviation()

    println("Average WBCC: $averageWbcc, Std Dev WBCC: $standardDevWbcc")
}
```

## 6.3B Slicing By Properties and Data classes

You can take most of the reduction metrics and use their `xxxBy()` counterparts to slice on an attribute, such as Gender.

```kotlin
fun main(args: Array<String>) {

    val standardDevByGender = patients.standardDeviationBy(
                    keySelector = { it.gender },
                    intMapper = { it.whiteBloodCellCount }
            )

    println(standardDevByGender)
}
```

You can also use a data class to slice on more than one attribute, such as `gender` and birthday `Month`.

```kotlin
fun main(args: Array<String>) {

    data class GenderAndMonth(val gender: Gender, val month: Month)

    val standardDevByGenderAndMonth = patients.standardDeviationBy(
                    keySelector = { GenderAndMonth(it.gender, it.birthday.month) },
                    intMapper = { it.whiteBloodCellCount }
            )

    standardDevByGenderAndMonth.forEach {
        println(it)
    }
}
```
Of course, this data is a little to sparse for breaking up by gender and month.

## 6.3C Getting Percentiles by Gender

You can expressively use Kotlin's different features to manipulate data sets fluently. For instance, you can create a `wbccPercentileCountByGender()` extension function to get a series of percentiles by gender.

```kotlin
fun main(args: Array<String>) {

    fun Collection<Patient>.wbccPercentileByGender(percentile: Double) =
            percentileBy(
                    percentile = percentile,
                    keySelector = { it.gender },
                    doubleMapper = { it.whiteBloodCellCount.toDouble() }
            )

    val percentileQuadrantsByGender = patients.let {
        mapOf(1.0 to it.wbccPercentileByGender(1.0),
                25.0 to it.wbccPercentileByGender(25.0),
                50.0 to it.wbccPercentileByGender(50.0),
                75.0 to it.wbccPercentileByGender(75.0),
                100.0 to it.wbccPercentileByGender(100.0)
        )
    }

    percentileQuadrantsByGender.forEach(::println)
}
```

The patterns behind Kotlin-Statistics have a large amount of potential, and show how Kotlin can bring new approaches to tackling data science and engineering. Follow the project to stay on top of its latest developments and hopefully you'll find its patterns to be useful in your own domain projects.

https://github.com/thomasnield/kotlin-statistics


## 6.4 Doing matrix math with ND4J

ND4J is Java's NumPy, and officially has interfaces for Java, Scala, and Clojure. However, it works with Kotlin out-of-the-box as well. While it may not have convenient fluent interfaces that you hopefully have grown to like in Kotlin, you can easily create your own around it.

Add ND4J to your project by adding this dependency:

```xml
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-native</artifactId>
    <version>0.9.1</version>
</dependency>
```

Below we have a `List` of `Sale` objects. We can create a `toINDArray()` extension function on any `Iterable` type, and have it accept any number of mapping arguments to turn each item into a row of values for a matrix.

```kotlin
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import java.time.LocalDate

fun main(args: Array<String>) {

    val matrix = sales.toINDArray(
            {it.saleDate.year.toDouble()},
            {it.saleDate.monthValue.toDouble()},
            {it.saleDate.dayOfMonth.toDouble()},
            {it.billingAmount}
    )

    println(matrix)
}

data class Sale(val accountId: Int, val saleDate: LocalDate, val billingAmount: Double)

val sales = listOf(
        Sale(1, LocalDate.of(2016,12,3), 180.0),
        Sale(2, LocalDate.of(2016, 7, 4), 140.2),
        Sale(3, LocalDate.of(2016, 6, 3), 111.4),
        Sale(4, LocalDate.of(2016, 1, 5), 192.7),
        Sale(5, LocalDate.of(2016, 5, 4), 137.9),
        Sale(6, LocalDate.of(2016, 3, 6), 125.6),
        Sale(7, LocalDate.of(2016, 12,4), 164.3),
        Sale(8, LocalDate.of(2016, 7,11), 144.2)
)


fun <T> Iterable<T>.toINDArray(vararg valueSelectors: (T) -> Double): INDArray {
    val list = toList()

    val selectedValues = list.asSequence()
            .flatMap { item -> valueSelectors.asSequence().map { it(item) } }
            .toList().toDoubleArray()

    return Nd4j.create(
            selectedValues,
            intArrayOf(list.size, valueSelectors.size)
    )
}
```

ND4J is efficient and great for linear algebra and matrix work. While its interface does not take advantage of Kotlin's features, you can easily create your own extensions to do so. Hopefully it is becoming clear that Kotlin can close a gap between object-oriented programming and tabular data work by exploiting modern functional programming patterns.

You can learn more about ND4J here:
https://github.com/deeplearning4j/nd4j

Another library of interest is Koma, which is a matrix library specifically for Kotlin:
https://github.com/kyonifer/koma

## 6.5 User Interfaces and Visualization with TornadoFX

TornadoFX is a comprehensive user interface library for Kotlin built around JavaFX. User interfaces are traditionally messy to build, but TornadoFX streamlines and reduces the amount of effort significantly.

https://github.com/edvin/tornadofx

An entire eBook guide is availabe covering TornadoFX in detail:

https://www.gitbook.com/book/edvin/tornadofx-guide/details

In this example, we are simply going to display a `TableView` and a `ScatterChart` showing clustering of Patients and their white blood cell count.

Make sure you have Java 1.8 configured for your Kotlin plug in like so, as JavaFX only exists in Java 8 and later.

```xml
<plugins>
    <plugin>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-maven-plugin</artifactId>
        <version>${kotlin.version}</version>
        <configuration>
            <jvmTarget>1.8</jvmTarget>
        </configuration>
...
```

Bring in TornadoFX and Kotlin-Statistics as dependencies:

```xml
<dependency>
    <groupId>org.nield</groupId>
    <artifactId>kotlinstatistics</artifactId>
    <version>0.3.0</version>
</dependency>
<dependency>
    <groupId>no.tornado</groupId>
    <artifactId>tornadofx</artifactId>
    <version>1.7.10</version>
</dependency>
```

For easy launching and integration with Intellij IDEA, you can use the TornadoFX plugin for Intellij IDEA:

https://plugins.jetbrains.com/plugin/8339-tornadofx


Here, we will use Kotlin-Statistics clustering functionality (which is backed by Apache Commons Math) and display it quickly in a `ScatterChart`.


```kotlin
package com.oreilly

import javafx.scene.chart.NumberAxis
import org.nield.kotlinstatistics.multiKMeansCluster
import tornadofx.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class MyApp: App(MyView::class)

class MyView : View() {
    override val root = scatterchart("WBCC Clustering by Age", NumberAxis(), NumberAxis()) {

        patients.multiKMeansCluster(k = 3,
                maxIterations = 10000,
                trialCount = 50,
                xSelector = { it.age.toDouble() },
                ySelector = { it.whiteBloodCellCount.toDouble() }
        )
                .forEachIndexed { index, centroid ->
                    series("Group ${index + 1}") {
                        centroid.points.forEach {
                            data(it.age, it.whiteBloodCellCount)
                        }
                    }
                }
    }
}


data class Patient(val firstName: String,
                   val lastName: String,
                   val gender: Gender,
                   val birthday: LocalDate,
                   val whiteBloodCellCount: Int)  {

    val age = ChronoUnit.YEARS.between(birthday, LocalDate.now())
}

val patients = listOf(
        Patient("John", "Simone", Gender.MALE, LocalDate.of(1989, 1, 7), 4500),
        Patient("Sarah", "Marley", Gender.FEMALE, LocalDate.of(1970, 2, 5), 6700),
        Patient("Jessica", "Arnold", Gender.FEMALE, LocalDate.of(1980, 3, 9), 3400),
        Patient("Sam", "Beasley", Gender.MALE, LocalDate.of(1981, 4, 17), 8800),
        Patient("Dan", "Forney", Gender.MALE, LocalDate.of(1985, 9, 13), 5400),
        Patient("Lauren", "Michaels", Gender.FEMALE, LocalDate.of(1975, 8, 21), 5000),
        Patient("Michael", "Erlich", Gender.MALE, LocalDate.of(1985, 12, 17), 4100),
        Patient("Jason", "Miles", Gender.MALE, LocalDate.of(1991, 11, 1), 3900),
        Patient("Rebekah", "Earley", Gender.FEMALE, LocalDate.of(1985, 2, 18), 4600),
        Patient("James", "Larson", Gender.MALE, LocalDate.of(1974, 4, 10), 5100),
        Patient("Dan", "Ulrech", Gender.MALE, LocalDate.of(1991, 7, 11), 6000),
        Patient("Heather", "Eisner", Gender.FEMALE, LocalDate.of(1994, 3, 6), 6000),
        Patient("Jasper", "Martin", Gender.MALE, LocalDate.of(1971, 7, 1), 6000)
)

enum class Gender {
    MALE,
    FEMALE
}
```

We can also bring in an interactive `TableView`. While we are at it, let's bring in ControlsFX to leverage some Table filtering functionalities.

```xml
<dependency>
    <groupId>org.controlsfx</groupId>
    <artifactId>controlsfx</artifactId>
    <version>8.40.13</version>
</dependency>
```

Now let's create a `TableView` of the Patients, and make them filterable by right-clicking a column.

```kotlin
package com.oreilly

import javafx.geometry.Orientation
import javafx.scene.chart.NumberAxis
import org.controlsfx.control.table.TableFilter
import org.nield.kotlinstatistics.multiKMeansCluster
import tornadofx.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class MyApp: App(MyView::class)

class MyView : View() {
    override val root = splitpane {
        orientation = Orientation.HORIZONTAL

        tableview<Patient> {
            column("FIRST NAME", Patient::firstName)
            column("LAST NAME", Patient::lastName)
            column("GENDER", Patient::gender)
            column("BIRTHDAY", Patient::birthday)
            column("AGE", Patient::age)
            column("WBCC", Patient::whiteBloodCellCount)

            items.setAll(patients)
            TableFilter.forTableView(this).apply()
        }

        scatterchart("WBCC Clustering by Age", NumberAxis(), NumberAxis()) {

            patients.multiKMeansCluster(k = 3,
                    maxIterations = 10000,
                    trialCount = 50,
                    xSelector = { it.age.toDouble() },
                    ySelector = { it.whiteBloodCellCount.toDouble() }
                    )
                    .forEachIndexed { index, centroid ->
                        series("Group ${index + 1}") {
                            centroid.points.forEach {
                                data(it.age, it.whiteBloodCellCount)
                            }
                        }
                    }
        }
    }
}


data class Patient(val firstName: String,
                   val lastName: String,
                   val gender: Gender,
                   val birthday: LocalDate,
                   val whiteBloodCellCount: Int)  {

    val age = ChronoUnit.YEARS.between(birthday, LocalDate.now())
}

val patients = listOf(
        Patient("John", "Simone", Gender.MALE, LocalDate.of(1989, 1, 7), 4500),
        Patient("Sarah", "Marley", Gender.FEMALE, LocalDate.of(1970, 2, 5), 6700),
        Patient("Jessica", "Arnold", Gender.FEMALE, LocalDate.of(1980, 3, 9), 3400),
        Patient("Sam", "Beasley", Gender.MALE, LocalDate.of(1981, 4, 17), 8800),
        Patient("Dan", "Forney", Gender.MALE, LocalDate.of(1985, 9, 13), 5400),
        Patient("Lauren", "Michaels", Gender.FEMALE, LocalDate.of(1975, 8, 21), 5000),
        Patient("Michael", "Erlich", Gender.MALE, LocalDate.of(1985, 12, 17), 4100),
        Patient("Jason", "Miles", Gender.MALE, LocalDate.of(1991, 11, 1), 3900),
        Patient("Rebekah", "Earley", Gender.FEMALE, LocalDate.of(1985, 2, 18), 4600),
        Patient("James", "Larson", Gender.MALE, LocalDate.of(1974, 4, 10), 5100),
        Patient("Dan", "Ulrech", Gender.MALE, LocalDate.of(1991, 7, 11), 6000),
        Patient("Heather", "Eisner", Gender.FEMALE, LocalDate.of(1994, 3, 6), 6000),
        Patient("Jasper", "Martin", Gender.MALE, LocalDate.of(1971, 7, 1), 6000)
)

enum class Gender {
    MALE,
    FEMALE
}
```

![](http://i.imgur.com/z5KFuTZ.png)

![](http://i.imgur.com/3bi2mYk.png)

Amazing, right? You can read about the handful of common chart types that are available in the TornadoFX Guide.

https://edvin.gitbooks.io/tornadofx-guide/content/8.%20Charts.html

While Kotlin's static typing may reduce the amount of flexibility you have in dynamically pivoting and analyzing data, it gives you a lot more safety and control without slowing you down severely. And you can deploy TornadoFX applications safely in production.
