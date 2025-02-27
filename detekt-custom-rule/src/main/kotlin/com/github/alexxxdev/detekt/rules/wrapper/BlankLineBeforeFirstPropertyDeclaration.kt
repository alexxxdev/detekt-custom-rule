package com.github.alexxxdev.detekt.rules.wrapper

import com.github.alexxxdev.detekt.RULE_ABOUT
import com.pinterest.ktlint.rule.engine.core.api.ElementType
import com.pinterest.ktlint.rule.engine.core.api.ElementType.CLASS
import com.pinterest.ktlint.rule.engine.core.api.ElementType.CLASS_BODY
import com.pinterest.ktlint.rule.engine.core.api.ElementType.COMPANION_KEYWORD
import com.pinterest.ktlint.rule.engine.core.api.ElementType.IDENTIFIER
import com.pinterest.ktlint.rule.engine.core.api.ElementType.MODIFIER_LIST
import com.pinterest.ktlint.rule.engine.core.api.ElementType.PROPERTY
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.findCompositeParentElementOfType
import com.pinterest.ktlint.rule.engine.core.api.indent
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpace
import com.pinterest.ktlint.rule.engine.core.api.prevLeaf
import com.pinterest.ktlint.rule.engine.core.api.upsertWhitespaceBeforeMe
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

class BlankLineBeforeFirstPropertyDeclaration : Rule(
    ruleId = RuleId("code-style:blank-line-before-first-property-declaration"),
    about = RULE_ABOUT,
) {

    private var identifier: String? = null

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        node.takeIf { it.elementType == PROPERTY }?.takeIf {
            if (it.treeParent.treeParent?.findChildByType(MODIFIER_LIST) ?.findChildByType(COMPANION_KEYWORD) != null) {
                return@takeIf false
            }
            if (it.treeParent != null && it.treeParent.elementType == CLASS_BODY) {
                val tmpIidentifier =
                    it.findCompositeParentElementOfType(CLASS)?.findChildByType(IDENTIFIER)?.text
                        ?: it.findCompositeParentElementOfType(ElementType.OBJECT_DECLARATION)
                            ?.findChildByType(IDENTIFIER)?.text
                if (tmpIidentifier == identifier) return@takeIf false
                identifier = tmpIidentifier
                val prevLeaf = it.prevLeaf()
                prevLeaf != null && (!prevLeaf.isWhiteSpace() || !prevLeaf.text.replace(" ", "").startsWith("\n\n"))
            } else {
                false
            }
        }?.let { insertBeforeNode ->
            emit(
                insertBeforeNode.startOffset,
                "Requires a blank line before first property declaration",
                true
            )
            if (autoCorrect) {
                insertBeforeNode.upsertWhitespaceBeforeMe("\n".plus(node.indent()))
            }
        }
    }
}
