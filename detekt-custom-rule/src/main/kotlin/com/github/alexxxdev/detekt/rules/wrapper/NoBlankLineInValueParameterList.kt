package com.github.alexxxdev.detekt.rules.wrapper

import com.github.alexxxdev.detekt.RULE_ABOUT
import com.pinterest.ktlint.rule.engine.core.api.ElementType.VALUE_PARAMETER
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.indent
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpace
import com.pinterest.ktlint.rule.engine.core.api.prevLeaf
import com.pinterest.ktlint.rule.engine.core.api.upsertWhitespaceBeforeMe
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

class NoBlankLineInValueParameterList : Rule(
    ruleId = RuleId("code-style:no-blank-line-in-value-parameter-list"),
    about = RULE_ABOUT,
) {

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        node.takeIf { it.elementType == VALUE_PARAMETER }?.takeIf {
            val prevLeaf = it.prevLeaf()
            prevLeaf != null && (prevLeaf.isWhiteSpace() && prevLeaf.text.replace(" ", "").startsWith("\n\n"))
        }?.let { insertBeforeNode ->
            emit(insertBeforeNode.startOffset, "Disallow empty lines between any value parameters", true)
            if (autoCorrect) {
                insertBeforeNode.upsertWhitespaceBeforeMe(node.indent())
            }
        }
    }
}
