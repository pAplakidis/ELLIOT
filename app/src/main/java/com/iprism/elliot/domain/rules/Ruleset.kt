package com.iprism.elliot.domain.rules

import android.content.Context
import com.iprism.elliot.R
import io.insource.framework.rule.rule
import javax.inject.Inject

class Ruleset @Inject constructor(
    private val context: Context
) {
    private fun getStringRule(resId: Int) = context.getString(resId)

    val proteinMinRuleWeek = rule("Check Minimum Percentage of Protein") {
        given {
            anyDouble()
        } and {
            it < 0.2
        } thenReturn {
            getStringRule(R.string.protein_min_rule_week)
        } otherwiseReturn {
            ""
        }
    }

    val proteinMaxRuleWeek = rule("Check Maximum Percentage of Protein") {
        given {
            anyDouble()
        } and {
            it > 0.35
        } thenReturn {
            getStringRule(R.string.protein_max_rule_week)
        } otherwiseReturn {
            ""
        }
    }

    val fatMinRuleWeek = rule("Check Minimum Percentage of Fat") {
        given {
            anyDouble()
        } and {
            it < 0.2
        } thenReturn {
            getStringRule(R.string.fat_min_rule_week)
        } otherwiseReturn {
            ""
        }
    }

    val fatMaxRuleWeek = rule("Check Maximum Percentage of Fat") {
        given {
            anyDouble()
        } and {
            it > 0.35
        } thenReturn {
            getStringRule(R.string.fat_max_rule_week)
        } otherwiseReturn {
            ""
        }
    }

    val saturatedFatRuleWeek = rule("Check Fat to Give Warning for Saturated Fat") {
        given {
            anyDouble()
        } and {
            it > 0.10
        } thenReturn {
            getStringRule(R.string.saturated_fat_rule_week)
        } otherwiseReturn {
            ""
        }
    }

    val carbsMinRuleWeek = rule("Check Minimum Percentage of Carbs") {
        given {
            anyDouble()
        } and {
            it < 0.55
        } thenReturn {
            getStringRule(R.string.carbs_min_rule_week)
        } otherwiseReturn {
            ""
        }
    }

    val carbsMaxRuleWeek = rule("Check Maximum Percentage of Carbs") {
        given {
            anyDouble()
        } and {
            it > 0.65
        } thenReturn {
            getStringRule(R.string.carbs_max_rule_week)
        } otherwiseReturn {
            ""
        }
    }

    val sugarRuleWeek = rule("Check Percentage of Sugar") {
        given {
            anyDouble()
        } and {
            it > 0.10
        } thenReturn {
            getStringRule(R.string.sugar_rule_week)
        } otherwiseReturn {
            ""
        }
    }

    val proteinMinRuleDay = rule("Check Minimum Percentage of Protein") {
        given {
            anyDouble()
        } and {
            it < 0.2
        } thenReturn {
            getStringRule(R.string.protein_min_rule_day)
        } otherwiseReturn {
            ""
        }
    }

    val proteinMaxRuleDay = rule("Check Maximum Percentage of Protein") {
        given {
            anyDouble()
        } and {
            it > 0.35
        } thenReturn {
            getStringRule(R.string.protein_max_rule_day)
        } otherwiseReturn {
            ""
        }
    }

    val fatMinRuleDay = rule("Check Minimum Percentage of Fat") {
        given {
            anyDouble()
        } and {
            it < 0.2
        } thenReturn {
            getStringRule(R.string.fat_min_rule_day)
        } otherwiseReturn {
            ""
        }
    }

    val fatMaxRuleDay = rule("Check Maximum Percentage of Fat") {
        given {
            anyDouble()
        } and {
            it > 0.35
        } thenReturn {
            getStringRule(R.string.fat_max_rule_day)
        } otherwiseReturn {
            ""
        }
    }

    val saturatedFatRuleDay = rule("Check Fat to Give Warning for Saturated Fat") {
        given {
            anyDouble()
        } and {
            it > 0.10
        } thenReturn {
            getStringRule(R.string.saturated_fat_rule_day)
        } otherwiseReturn {
            ""
        }
    }

    val carbsMinRuleDay = rule("Check Minimum Percentage of Carbs") {
        given {
            anyDouble()
        } and {
            it < 0.55
        } thenReturn {
            getStringRule(R.string.carbs_min_rule_day)
        } otherwiseReturn {
            ""
        }
    }

    val carbsMaxRuleDay = rule("Check Maximum Percentage of Carbs") {
        given {
            anyDouble()
        } and {
            it > 0.65
        } thenReturn {
            getStringRule(R.string.carbs_max_rule_day)
        } otherwiseReturn {
            ""
        }
    }

    val sugarRuleDay = rule("Check Percentage of Sugar") {
        given {
            anyDouble()
        } and {
            it > 0.10
        } thenReturn {
            getStringRule(R.string.sugar_rule_day)
        } otherwiseReturn {
            ""
        }
    }

    val sodiumRule = rule("Check Percentage of Sodium") {
        given {
            anyDouble()
        } and {
            it > 1.5
        } thenReturn {
            getStringRule(R.string.sodium_rule)
        } otherwiseReturn {
            ""
        }
    }

    val fiberRuleDay = rule("Check Percentage of Fiber") {
        given {
            anyDouble()
        } and {
            it < 30
        } thenReturn {
            getStringRule(R.string.fiber_rule_day)
        } otherwiseReturn {
            ""
        }
    }

    val futureFatRule = rule("Check Percentage of fat for next week") {
        given {
            anyDouble()
        } and {
            it > 0.40
        } thenReturn {
            getStringRule(R.string.future_fat_rule)
        } otherwiseReturn {
            ""
        }
    }

    val futureSaturatedFatRule = rule("Check Percentage of Saturated fat for next week") {
        given {
            anyDouble()
        } and {
            it > 0.10
        } thenReturn {
            getStringRule(R.string.future_saturated_fat_rule)
        } otherwiseReturn {
            ""
        }
    }

    val futureSugarRule = rule("Check Percentage of Sugar for next week") {
        given {
            anyDouble()
        } and {
            it > 0.20
        } thenReturn {
            getStringRule(R.string.future_sugar_rule)
        } otherwiseReturn {
            ""
        }
    }
}