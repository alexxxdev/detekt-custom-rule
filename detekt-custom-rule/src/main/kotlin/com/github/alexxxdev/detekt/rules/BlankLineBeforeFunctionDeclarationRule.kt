package com.github.alexxxdev.detekt.rules

import com.github.alexxxdev.detekt.rules.wrapper.BlankLineBeforeFunctionDeclaration
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfigProperty
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.config
import io.gitlab.arturbosch.detekt.api.internal.Configuration
import io.gitlab.arturbosch.detekt.formatting.FormattingRule

class BlankLineBeforeFunctionDeclarationRule(config: Config = Config.empty) : FormattingRule(config) {

    override val wrapping = BlankLineBeforeFunctionDeclaration()

    override val issue = issueFor("Requires a blank line before any function declaration")

    @Configuration("ignore functional interfaces")
    private val ignoreFunctionalInterfaces by config(false)

    @Configuration("ignore anonymous objects")
    private val ignoreAnonymousObjects by config(false)

    override fun overrideEditorConfigProperties(): Map<EditorConfigProperty<*>, String> =
        mapOf(
            BlankLineBeforeFunctionDeclaration.IGNORE_FUNCTIONAL_INTERFACES to ignoreFunctionalInterfaces.toString(),
            BlankLineBeforeFunctionDeclaration.IGNORE_ANONYMOUS_OBJECTS to ignoreAnonymousObjects.toString()
        )
}
