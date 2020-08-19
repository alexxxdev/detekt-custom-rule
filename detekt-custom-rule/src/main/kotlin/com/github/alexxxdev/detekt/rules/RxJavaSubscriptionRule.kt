package com.github.alexxxdev.detekt.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtValueArgument
import org.jetbrains.kotlin.psi.KtValueArgumentList

const val METHOD_NAME = "subscribe"

class RxJavaSubscriptionRule(config: Config = Config.empty) : Rule(config) {

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        val elementName = expression.firstChild
        val elementBody = expression.lastChild
        if (elementName.textMatches(METHOD_NAME)) {
            if (elementBody is KtValueArgumentList &&
                elementBody.children.size == 2 &&
                elementBody.firstChild.nextSibling is KtValueArgument
            ) {
                val firstArgument = elementBody.children[0]
                val lastArgument = elementBody.children[1]
                if (wrongCharactersBetweenArguments(firstArgument, lastArgument) ||
                    lastArgument.nextSibling?.node?.elementType != KtTokens.RPAR ||
                    argumentsNotContainNewlineCharacters(firstArgument, lastArgument)) {
                    report(expression)
                }
            } else {
                report(expression)
            }
        }
    }

    override val issue: Issue
        get() = Issue(
            "RxJavaSubscription",
            Severity.Style,
            "subscribe not in style",
            Debt.FIVE_MINS
        )

    private fun report(expression: KtCallExpression) {
        report(CodeSmell(issue, Entity.from(expression), ""))
    }

    private fun wrongCharactersBetweenArguments(firstArgument: PsiElement?, lastArgument: PsiElement?): Boolean {
        return firstArgument?.nextSibling?.node?.elementType != KtTokens.COMMA ||
                firstArgument?.nextSibling?.nextSibling !is PsiWhiteSpace ||
                firstArgument.nextSibling?.nextSibling?.text?.contentEquals(" ") != true ||
                firstArgument.nextSibling?.nextSibling?.nextSibling != lastArgument
    }

    private fun argumentsNotContainNewlineCharacters(firstArgument: PsiElement?, lastArgument: PsiElement?): Boolean {
        return firstArgument?.textContains('\n') != true ||
                lastArgument?.textContains('\n') != true
    }
}
