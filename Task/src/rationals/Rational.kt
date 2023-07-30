package rationals

import java.math.BigInteger


data class Rational(val numerator: BigInteger, val denominator: BigInteger): Comparable<Rational> {
    init {
        if (denominator.equals(0))
            throw IllegalArgumentException()
    }

    override fun toString(): String {
        val normalized = normalize()
        return if (normalized.denominator == BigInteger.ONE) {
            normalized.numerator.toString()
        } else {
            normalized.numerator.toString() + "/" + normalized.denominator.toString()
        }
    }

    fun normalize(): Rational {
        val gcd = numerator.gcd(denominator)
        val res = Rational(numerator / gcd, denominator / gcd)
        return if (res.denominator < BigInteger.ZERO) {
            Rational(-res.numerator, -res.denominator)
        } else {
            Rational(res.numerator, res.denominator)
        }
    }

    override fun compareTo(other: Rational): Int {
        val first = this.normalize()
        val second = other.normalize()
        return (first.numerator * second.denominator).compareTo(second.numerator * first.denominator)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Rational) {
            compareTo(other) == 0
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }
}

class RationalRange(override val start: Rational, override val endInclusive: Rational): ClosedRange<Rational>

operator fun Rational.plus(other: Rational): Rational {
    return Rational(this.numerator * other.denominator + this.denominator * other.numerator,
        this.denominator * other.denominator).normalize()
}

operator fun Rational.minus(other: Rational): Rational {
    return Rational(this.numerator * other.denominator - this.denominator * other.numerator,
        this.denominator * other.denominator).normalize()
}

operator fun Rational.times(other: Rational): Rational {
    return Rational(this.numerator * other.numerator,
        this.denominator * other.denominator).normalize()
}

operator fun Rational.div(other: Rational): Rational {
    return Rational(this.numerator * other.denominator,
        other.numerator * this.denominator).normalize()
}

operator fun Rational.unaryMinus(): Rational {
    return Rational(-this.numerator, this.denominator).normalize()
}

operator fun Rational.rangeTo(other: Rational): ClosedRange<Rational> {
    return RationalRange(this, other)
}

infix fun BigInteger.divBy(denominator: BigInteger): Rational {
    return Rational(this, denominator)
}

infix fun Int.divBy(denominator: Int): Rational {
    return Rational(this.toBigInteger(), denominator.toBigInteger())
}

infix fun Long.divBy(denominator: Long): Rational {
    return Rational(this.toBigInteger(), denominator.toBigInteger())
}

fun String.toRational(): Rational {
    val substrings = this.split("/")
    return if (substrings.size == 1) {
        Rational(this.toBigInteger(), BigInteger.ONE)
    } else {
        Rational(substrings[0].toBigInteger(), substrings[1].toBigInteger())
    }
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}