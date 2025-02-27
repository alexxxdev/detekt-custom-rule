package com.github.alexxxdev.detekt.rules

import com.github.alexxxdev.detekt.rules.wrapper.NoBlankLineInValueParameterList
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.formatting.FormattingRule

class NoBlankLineInValueParameterListRule(config: Config = Config.empty) : FormattingRule(config) {

    override val wrapping = NoBlankLineInValueParameterList()

    override val issue = issueFor("Disallow empty lines between any value parameters")
}
