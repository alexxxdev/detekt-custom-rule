package com.github.alexxxdev.detekt.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiComment

const val COMMENT_TEMPLATE = "template"

class TodoCommentRule(config: Config = Config.empty) : Rule(config) {

    private val template = Regex(valueOrDefault(COMMENT_TEMPLATE, ""))

    override fun visitComment(comment: PsiComment?) {
        super.visitComment(comment)
        comment?.let {
            val text = comment.text.orEmpty()
            if (template.pattern.isEmpty()) return
            if (template.containsMatchIn(text)) return

            report(CodeSmell(issue, Entity.from(comment), ""))
        }
    }

    override val issue: Issue
        get() = Issue(
            "TodoComment",
            Severity.CodeSmell,
            "needs some comment in accordance with the template: $template",
            Debt.FIVE_MINS
        )
}
