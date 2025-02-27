package com.github.alexxxdev.detekt

import com.github.alexxxdev.detekt.rules.BlankLineBeforeFunctionDeclarationRule
import com.github.alexxxdev.detekt.rules.wrapper.BlankLineBeforeFunctionDeclaration
import com.pinterest.ktlint.test.KtLintAssertThat
import com.pinterest.ktlint.test.LintViolation
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class BlankLineBeforeFunctionDeclarationRuleTest : Spek({
    val subject by memoized { BlankLineBeforeFunctionDeclarationRule() }
    val assertThat = KtLintAssertThat.assertThatRule { BlankLineBeforeFunctionDeclaration() }

    describe("BlankLineBeforeFunctionDeclaration Rule") {

        it("variant 12") {
            val code = """
                fun interface Listener {
                   fun onItemClick(item: Any)
                }
            """.trimIndent()
            assertThat(
                BlankLineBeforeFunctionDeclarationRule(TestConfig("ignoreFunctionalInterfaces" to true)).compileAndLint(
                    code
                )
            ).isEmpty()
        }

        it("variant 1") {
            val code = """
                interface Foo {
                
                    override fun bar()
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 2") {
            val code = """
                interface Foo {                
                    override fun bar()
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 3") {
            val code = """
                interface Foo {                
                    override fun bar()
                    override fun bar()
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(2)
        }

        it("variant 4") {
            val code = """
                interface Foo {                
                
                    override fun bar()
                    override fun bar()
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 5") {
            val code = """
                interface Foo {                
                    override fun bar()
                    
                    override fun bar()
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 6") {
            val code = """
                interface Foo {                
                    override fun bar1()
                    override fun bar2()
                }
            """.trimIndent()
            val formattedCode = """
                interface Foo {
                
                    override fun bar1()

                    override fun bar2()
                }
            """.trimIndent()
            assertThat(code)
                .hasLintViolations(
                    LintViolation(2, 5, "Requires a blank line before any function declaration"),
                    LintViolation(3, 5, "Requires a blank line before any function declaration"),
                )
                .isFormattedAs(formattedCode)
        }

        it("variant 7") {
            val code = """
                interface Foo {                
                    override fun bar1()
                    
                    override fun bar2()
                }
            """.trimIndent()
            val formattedCode = """
                interface Foo {
                
                    override fun bar1()
                    
                    override fun bar2()
                }
            """.trimIndent()
            assertThat(code)
                .hasLintViolations(
                    LintViolation(2, 5, "Requires a blank line before any function declaration")
                )
                .isFormattedAs(formattedCode)
        }

        it("variant 8") {
            val code = """
                interface Foo {
                
                    override fun bar1()
                    override fun bar2()
                }
            """.trimIndent()
            val formattedCode = """
                interface Foo {
                
                    override fun bar1()
                
                    override fun bar2()
                }
            """.trimIndent()
            assertThat(code)
                .hasLintViolations(
                    LintViolation(4, 5, "Requires a blank line before any function declaration")
                )
                .isFormattedAs(formattedCode)
        }

        it("variant 9") {
            val code = """
                fun bar()
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 10") {
            val code = """
                fun interface Listener {
                
                   fun onItemClick(item: Any)
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 11") {
            val code = """
                object : MouseAdapter() {
                                
                   fun onItemClick(item: Any)
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 13") {
            val code = """
                object : MouseAdapter() {                                
                   fun onItemClick(item: Any)
                }
            """.trimIndent()
            assertThat(
                BlankLineBeforeFunctionDeclarationRule(TestConfig("ignoreAnonymousObjects" to true)).compileAndLint(
                    code
                )
            ).isEmpty()
        }
    }
})
