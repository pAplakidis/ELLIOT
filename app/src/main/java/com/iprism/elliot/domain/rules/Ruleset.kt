package com.iprism.elliot.domain.rules

import io.insource.framework.rule.rule

class Ruleset {
    companion object Rules {
        val proteinMinRuleWeek = rule("Check Minimum Percentage of Protein") {
            given {
                anyFloat()
            } and {
                it < 0.2
            } thenReturn {
                "You should consume more protein this week."
            } otherwiseReturn {
                ""
            }
        }

        val proteinMaxRuleWeek = rule("Check Maximum Percentage of Protein") {
            given {
                anyFloat()
            } and {
                it > 0.35
            } thenReturn {
                "You should consume less protein this week."
            } otherwiseReturn {
                ""
            }
        }

        val fatMinRuleWeek = rule("Check Minimum Percentage of Fat") {
            given {
                anyFloat()
            } and {
                it < 0.2
            } thenReturn {
                "You should consume more fat this week."
            } otherwiseReturn {
                ""
            }
        }

        val fatMaxRuleWeek = rule("Check Maximum Percentage of Fat") {
            given {
                anyFloat()
            } and {
                it > 0.35
            } thenReturn {
                "You should consume less fat this week."
            } otherwiseReturn {
                ""
            }
        }

        val saturatedFatsRuleWeek = rule("Check Fat to Give Warning for Saturated Fat") {
            given {
                anyFloat()
            } and {
                it > 0.10
            } thenReturn {
                "You should consume less saturated fat this week, if you did so."
            } otherwiseReturn {
                ""
            }
        }

        val carbsMinRuleWeek = rule("Check Minimum Percentage of Carbs") {
            given {
                anyFloat()
            } and {
                it < 0.55
            } thenReturn {
                "You should consume more carbs this week."
            } otherwiseReturn {
                ""
            }
        }

        val carbsMaxRuleWeek = rule("Check Maximum Percentage of Carbs") {
            given {
                anyFloat()
            } and {
                it > 0.65
            } thenReturn {
                "You should consume less carbs this week."
            } otherwiseReturn {
                ""
            }
        }

        val sugarRuleWeek = rule("Check Percentage of Sugar") {
            given {
                anyFloat()
            } and {
                it > 0.10
            } thenReturn {
                "You should consume less sugar this week."
            } otherwiseReturn {
                ""
            }
        }

        val proteinMinRuleDay = rule("Check Minimum Percentage of Protein") {
            given {
                anyFloat()
            } and {
                it < 0.2
            } thenReturn {
                "You should consume more protein today."
            } otherwiseReturn {
                ""
            }
        }

        val proteinMaxRuleDay = rule("Check Maximum Percentage of Protein") {
            given {
                anyFloat()
            } and {
                it > 0.35
            } thenReturn {
                "You should consume less protein today."
            } otherwiseReturn {
                ""
            }
        }

        val fatMinRuleDay = rule("Check Minimum Percentage of Fat") {
            given {
                anyFloat()
            } and {
                it < 0.2
            } thenReturn {
                "You should consume more fat today."
            } otherwiseReturn {
                ""
            }
        }

        val fatMaxRuleDay = rule("Check Maximum Percentage of Fat") {
            given {
                anyFloat()
            } and {
                it > 0.35
            } thenReturn {
                "You should consume less fat today."
            } otherwiseReturn {
                ""
            }
        }

        val saturatedFatsRuleDay = rule("Check Fat to Give Warning for Saturated Fat") {
            given {
                anyFloat()
            } and {
                it > 0.10
            } thenReturn {
                "You should consume less saturated fat today, if you did so."
            } otherwiseReturn {
                ""
            }
        }

        val carbsMinRuleDay = rule("Check Minimum Percentage of Carbs") {
            given {
                anyFloat()
            } and {
                it < 0.55
            } thenReturn {
                "You should consume more carbs today."
            } otherwiseReturn {
                ""
            }
        }

        val carbsMaxRuleDay = rule("Check Maximum Percentage of Carbs") {
            given {
                anyFloat()
            } and {
                it > 0.65
            } thenReturn {
                "You should consume less carbs today."
            } otherwiseReturn {
                ""
            }
        }

        val sugarRuleDay = rule("Check Percentage of Sugar") {
            given {
                anyFloat()
            } and {
                it > 0.10
            } thenReturn {
                "You should consume less sugar today."
            } otherwiseReturn {
                ""
            }
        }

        val fiberRuleDay = rule("Check Percentage of Fiber") {
            given {
                anyFloat()
            } and {
                it < 30
            } thenReturn {
                "You should consume more fiber today."
            } otherwiseReturn {
                ""
            }
        }

        val sodiumRule = rule("Check Percentage of Sodium") {
            given {
                anyFloat()
            } and {
                it > 1.5
            } thenReturn {
                "You should consume less sodium today."
            } otherwiseReturn {
                ""
            }
        }

        val futureFatRule = rule("Check Percentage of fat for next week") {
            given {
                anyFloat()
            } and {
                it > 0.40
            } thenReturn {
                "You should consume less fat next week."
            } otherwiseReturn {
                ""
            }
        }

        val futureSaturatedFatRule = rule("Check Percentage of Saturated fat for next week") {
            given {
                anyFloat()
            } and {
                it > 0.10
            } thenReturn {
                "You should consume less saturated fats next week, if you did so."
            } otherwiseReturn {
                ""
            }
        }

        val futureSugarRule = rule("Check Percentage of Sugar for next week") {
            given {
                anyFloat()
            } and {
                it > 0.20
            } thenReturn {
                "You should consume less sugar next week."
            } otherwiseReturn {
                ""
            }
        }


    }
}