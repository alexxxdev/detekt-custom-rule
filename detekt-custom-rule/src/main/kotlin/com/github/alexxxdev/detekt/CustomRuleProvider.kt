package com.github.alexxxdev.detekt

import com.github.alexxxdev.detekt.rules.RxJavaSubscriptionRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class CustomRuleProvider : RuleSetProvider {
    override val ruleSetId: String = "CodeStyle"

    override fun instance(config: Config) = RuleSet(ruleSetId, listOf(RxJavaSubscriptionRule(config)))
}
