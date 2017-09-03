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

## 6.3
