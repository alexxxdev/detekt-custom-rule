package com.github.alexxxdev.detekt

import com.github.alexxxdev.detekt.rules.BlankLineBeforeFirstPropertyDeclarationRule
import com.github.alexxxdev.detekt.rules.wrapper.BlankLineBeforeFirstPropertyDeclaration
import com.pinterest.ktlint.test.KtLintAssertThat
import com.pinterest.ktlint.test.LintViolation
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class BlankLineBeforeFirstPropertyDeclarationRuleTest : Spek({
    val subject by memoized { BlankLineBeforeFirstPropertyDeclarationRule() }
    val assertThat = KtLintAssertThat.assertThatRule { BlankLineBeforeFirstPropertyDeclaration() }

    describe("BlankLineBeforeFunctionDeclaration Rule") {

        it("variant 1") {
            val code = """
                interface Foo {
                                
                    val str = ""
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 2") {
            val code = """
                class Foo() {
                                
                    val str = ""
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 3") {
            val code = """
                object Foo {  
                                              
                    val str = ""
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 4") {
            val code = """
                fun Foo(node: ASTNode):Boolean {                                
                    val str = ""
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 5") {
            val code = """
                class Foo() {
                    companion object {            
                        val str = ""
                    }
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 6") {
            val code = """
                interface Foo {                                
                    val str = ""
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 7") {
            val code = """
                class Foo() {                                
                    val str = ""
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 8") {
            val code = """
                object Foo {                                
                    val str = ""
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 9") {
            val code = """
                interface Foo {
                    val str = ""
                }
            """.trimIndent()
            val formattedCode = """
                interface Foo {
                
                    val str = ""
                }
            """.trimIndent()
            assertThat(code)
                .hasLintViolations(
                    LintViolation(2, 5, "Requires a blank line before first property declaration")
                )
                .isFormattedAs(formattedCode)
        }

        it("variant 10") {
            val code = """
                private val str = ""
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 11") {
            val code = """
                @SuppressLint("SupportAnnotationUsage")
                interface AlertBuilder<out D : DialogInterface> {                
                    val ctx: Context
                    private val str = ""
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 12") {
            val code = """
                @SuppressLint("SupportAnnotationUsage")
                interface AlertBuilder<out D : DialogInterface> {
                    val ctx: Context
                }
            """.trimIndent()
            val formattedCode = """
                @SuppressLint("SupportAnnotationUsage")
                interface AlertBuilder<out D : DialogInterface> {
                
                    val ctx: Context
                }
            """.trimIndent()
            assertThat(code)
                .hasLintViolations(
                    LintViolation(3, 5, "Requires a blank line before first property declaration")
                )
                .isFormattedAs(formattedCode)
        }

        it("variant 13") {
            val code = """
                class Foo(): FormattingRule {                                
                    val str = ""
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("variant 14") {
            val code = """
                fun Foo(): Boolean {                                
                    object : Adapter {
                        override val foo = false
                    }
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("variant 15") {
            val code = """
                sealed class Foo(): Boolean {                                
                    val foo = false
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }
    }
})
