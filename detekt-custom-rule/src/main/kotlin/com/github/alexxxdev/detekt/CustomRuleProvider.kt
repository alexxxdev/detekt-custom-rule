package com.github.alexxxdev.detekt

import com.github.alexxxdev.detekt.rules.BlankLineBeforeFirstPropertyDeclarationRule
import com.github.alexxxdev.detekt.rules.BlankLineBeforeFunctionDeclarationRule
import com.github.alexxxdev.detekt.rules.NoBlankLineInValueParameterListRule
import com.github.alexxxdev.detekt.rules.RxJavaSubscriptionRule
import com.pinterest.ktlint.rule.engine.core.api.Rule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class CustomRuleProvider : RuleSetProvider {

    override val ruleSetId: String = "CodeStyle"

    override fun instance(config: Config) = RuleSet(
        ruleSetId,
        listOf(
            RxJavaSubscriptionRule(config),
            BlankLineBeforeFunctionDeclarationRule(config),
            NoBlankLineInValueParameterListRule(config),
            BlankLineBeforeFirstPropertyDeclarationRule(config)
        )
    )
}

internal val RULE_ABOUT =
    Rule.About(
        maintainer = "alexxxdev",
        repositoryUrl = "https://github.com/alexxxdev/detekt-custom-rule",
        issueTrackerUrl = "https://github.com/alexxxdev/detekt-custom-rule/issues",
    )
