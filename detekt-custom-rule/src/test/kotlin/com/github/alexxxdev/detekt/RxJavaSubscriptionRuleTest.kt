package com.github.alexxxdev.detekt

import com.github.alexxxdev.detekt.rules.RxJavaSubscriptionRule
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class RxJavaSubscriptionRuleTest : Spek({
    val subject by memoized { RxJavaSubscriptionRule() }

    describe("RxJavaSubscription rule") {
        it("does not report") {
            val code = """
                fun f() {
                    subscribe({
                    
                    }, {
                    
                    })
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("report subscribe not in style, variant 1") {
            val code = """
                fun f() {
                    subscribe(
                        {},
                        {}
                    )
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 2") {
            val code = """
                fun f() {
                    subscribe({
                    
                    },  {
                    
                    })
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 3") {
            val code = """
                fun f() {
                    subscribe({
                    
                    },
                    {
                    
                    })
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 4") {
            val code = """
                fun f() {
                    subscribe({
                    
                    }, {
                    
                    }
                    )
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 5") {
            val code = """
                fun f() {
                    subscribe( {
                    
                    }, {
                    
                    })
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 6") {
            val code = """
                fun f() {
                    subscribe(
                        {}, {}
                    )
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 7") {
            val code = """
                fun f() {
                    subscribe({}, {})
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 8") {
            val code = """
                fun f() {
                    subscribe({
                    
                    }, {})
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 9") {
            val code = """
                fun f() {
                    subscribe({}, {
                    
                    })
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 10") {
            val code = """
                fun f() {
                    subscribe()
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 11") {
            val code = """
                fun f() {
                    subscribe({
                    
                    })
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("report subscribe not in style, variant 12") {
            val code = """
                fun f() {
                    subscribe {}
                }
            """.trimIndent()
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }
    }
})
