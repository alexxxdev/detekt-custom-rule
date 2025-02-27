package com.github.alexxxdev.detekt

import com.github.alexxxdev.detekt.rules.NoBlankLineInValueParameterListRule
import com.github.alexxxdev.detekt.rules.wrapper.NoBlankLineInValueParameterList
import com.pinterest.ktlint.test.KtLintAssertThat
import com.pinterest.ktlint.test.LintViolation
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class NoBlankLineInValueParameterListRuleTest : Spek({
    val subject by memoized { NoBlankLineInValueParameterListRule() }
    val assertThat = KtLintAssertThat.assertThatRule { NoBlankLineInValueParameterList() }

    describe("NoBlankLineInValueParameterList Rule") {

        it("variant 0") {
            val code = """
                fun foo( bar1:String, bar2:String)
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 1") {
            val code = """
                class Foo constructor(
                    private val bar1:String,                    
                    private val bar2:String
                )
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 2") {
            val code = """
                class Foo constructor(
                    private val bar1:String,

                    private val bar2:String
                )
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 3") {
            val code = """
                class Foo constructor(

                    private val bar1:String,
                    private val bar2:String
                )
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 4") {
            val code = """
                class Foo constructor(
                
                    private val bar1:String,
                    private val bar2:String
                )
            """.trimIndent()
            val formattedCode = """
                class Foo constructor(
                    private val bar1:String,
                    private val bar2:String
                )
            """.trimIndent()
            assertThat(code)
                .hasLintViolations(
                    LintViolation(3, 5, "Disallow empty lines between any value parameters")
                )
                .isFormattedAs(formattedCode)
        }

        it("variant 5") {
            val code = """
                class Foo constructor(
                    private val bar1:String,
                    
                    private val bar2:String
                )
            """.trimIndent()
            val formattedCode = """
                class Foo constructor(
                    private val bar1:String,
                    private val bar2:String
                )
            """.trimIndent()
            assertThat(code)
                .hasLintViolations(
                    LintViolation(4, 5, "Disallow empty lines between any value parameters")
                )
                .isFormattedAs(formattedCode)
        }

        it("variant 5") {
            val code = """
                class Foo constructor(
                
                    private val bar1:String,
                    
                    private val bar2:String
                )
            """.trimIndent()
            val formattedCode = """
                class Foo constructor(
                    private val bar1:String,
                    private val bar2:String
                )
            """.trimIndent()
            assertThat(code)
                .hasLintViolations(
                    LintViolation(3, 5, "Disallow empty lines between any value parameters"),
                    LintViolation(5, 5, "Disallow empty lines between any value parameters")
                )
                .isFormattedAs(formattedCode)
        }
    }
})
