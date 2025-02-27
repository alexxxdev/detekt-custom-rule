package com.github.alexxxdev.detekt.rules.wrapper

import com.github.alexxxdev.detekt.RULE_ABOUT
import com.pinterest.ktlint.rule.engine.core.api.ElementType.CLASS
import com.pinterest.ktlint.rule.engine.core.api.ElementType.FUN
import com.pinterest.ktlint.rule.engine.core.api.ElementType.FUN_KEYWORD
import com.pinterest.ktlint.rule.engine.core.api.ElementType.MODIFIER_LIST
import com.pinterest.ktlint.rule.engine.core.api.ElementType.OBJECT_DECLARATION
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfig
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfigProperty
import com.pinterest.ktlint.rule.engine.core.api.findCompositeParentElementOfType
import com.pinterest.ktlint.rule.engine.core.api.indent
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpace
import com.pinterest.ktlint.rule.engine.core.api.prevLeaf
import com.pinterest.ktlint.rule.engine.core.api.upsertWhitespaceBeforeMe
import org.ec4j.core.model.PropertyType
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

class BlankLineBeforeFunctionDeclaration : Rule(
    ruleId = RuleId("code-style:blank-line-before-function-declaration"),
    about = RULE_ABOUT,
) {

    private var ignoreFunctionalInterfaces: Boolean = IGNORE_FUNCTIONAL_INTERFACES.defaultValue
    private var ignoreAnonymousObjects: Boolean = IGNORE_ANONYMOUS_OBJECTS.defaultValue

    override fun beforeFirstNode(editorConfig: EditorConfig) {
        super.beforeFirstNode(editorConfig)
        ignoreFunctionalInterfaces = editorConfig.getEditorConfigValueOrNull(
            IGNORE_FUNCTIONAL_INTERFACES.type, IGNORE_FUNCTIONAL_INTERFACES.name
        ) ?: IGNORE_FUNCTIONAL_INTERFACES.defaultValue

        ignoreAnonymousObjects = editorConfig.getEditorConfigValueOrNull(
            IGNORE_ANONYMOUS_OBJECTS.type, IGNORE_ANONYMOUS_OBJECTS.name
        ) ?: IGNORE_ANONYMOUS_OBJECTS.defaultValue
    }

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        node.takeIf { it.elementType == FUN }?.takeIf {
            val prevLeaf = it.prevLeaf()
            val isProblem = prevLeaf != null &&
                (!prevLeaf.isWhiteSpace() || !prevLeaf.text.replace(" ", "").startsWith("\n\n"))
            when {
                isProblem && ignoreAnonymousObjects && it.isParentObjectDeclaration() -> false
                isProblem && ignoreFunctionalInterfaces && it.isFunctionalInterfaces() -> false
                isProblem -> true
                else -> false
            }
        }?.let { insertBeforeNode ->
            emit(
                insertBeforeNode.startOffset,
                "Requires a blank line before any function declaration",
                true
            )
            if (autoCorrect) {
                insertBeforeNode.upsertWhitespaceBeforeMe("\n".plus(node.indent()))
            }
        }
    }

    private fun ASTNode.isParentObjectDeclaration() =
        findCompositeParentElementOfType(OBJECT_DECLARATION) != null

    private fun ASTNode.isFunctionalInterfaces(): Boolean {
        return findCompositeParentElementOfType(CLASS)?.findChildByType(MODIFIER_LIST)
            ?.findChildByType(FUN_KEYWORD) != null
    }

    companion object {
        val IGNORE_FUNCTIONAL_INTERFACES: EditorConfigProperty<Boolean> = EditorConfigProperty(
            type = PropertyType.LowerCasingPropertyType(
                "blank_line_before_function_declaration_ignore_functional_interfaces",
                "ignore functional interfaces",
                PropertyType.PropertyValueParser.BOOLEAN_VALUE_PARSER,
                setOf("true", "false"),
            ),
            defaultValue = false
        )

        val IGNORE_ANONYMOUS_OBJECTS: EditorConfigProperty<Boolean> = EditorConfigProperty(
            type = PropertyType.LowerCasingPropertyType(
                "blank_line_before_function_declaration_ignore_anonymous_objects",
                "ignore anonymous objects",
                PropertyType.PropertyValueParser.BOOLEAN_VALUE_PARSER,
                setOf("true", "false"),
            ),
            defaultValue = false
        )
    }
}
