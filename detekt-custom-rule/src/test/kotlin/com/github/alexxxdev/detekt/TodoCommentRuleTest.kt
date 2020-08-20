package com.github.alexxxdev.detekt

import com.github.alexxxdev.detekt.rules.COMMENT_TEMPLATE
import com.github.alexxxdev.detekt.rules.TodoCommentRule
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class TodoCommentRuleTest : Spek({
    val subjectDefault by memoized { TodoCommentRule() }
    val subjectWithTemplate by memoized {
        val config = TestConfig(mapOf(COMMENT_TEMPLATE to "https://jira.com/browse/ABC-(\\d+)"))
        TodoCommentRule(config)
    }

    describe("TodoComment rule") {
        it("does not report with default config") {
            val code = """
                // Todo
            """.trimIndent()
            assertThat(subjectDefault.compileAndLint(code)).isEmpty()
        }
        it("does not report with template") {
            val code = """
                // Todo https://jira.com/browse/ABC-12334
            """.trimIndent()
            assertThat(subjectWithTemplate.compileAndLint(code)).isEmpty()
        }
        it("report, need comment") {
            val code = """
                // Todo
            """.trimIndent()
            assertThat(subjectWithTemplate.compileAndLint(code)).hasSize(1)
        }
        it("report, need true comment") {
            val code = """
                // Todo https://jira.com/browse/ABCD-12334
            """.trimIndent()
            assertThat(subjectWithTemplate.compileAndLint(code)).hasSize(1)
        }
    }
})
