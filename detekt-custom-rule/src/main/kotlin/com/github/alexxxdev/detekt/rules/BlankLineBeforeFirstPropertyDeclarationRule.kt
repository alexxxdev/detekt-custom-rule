package com.github.alexxxdev.detekt.rules

import com.github.alexxxdev.detekt.rules.wrapper.BlankLineBeforeFirstPropertyDeclaration
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.config
import io.gitlab.arturbosch.detekt.formatting.FormattingRule

class BlankLineBeforeFirstPropertyDeclarationRule(config: Config = Config.empty) : FormattingRule(config) {

    override val wrapping = BlankLineBeforeFirstPropertyDeclaration()

    override val issue = issueFor("Requires a blank line before first property declaration")
}
