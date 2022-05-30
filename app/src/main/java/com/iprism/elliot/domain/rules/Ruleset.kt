package com.iprism.elliot.domain.rules

import io.insource.framework.rule.rule

class Ruleset {
    companion object Rules {
        val fatRule = rule("Check Fat") {
            given {
                anyFloat()
            } and {
                it > 3
            } thenReturn {
                "Too much fat"
            } otherwiseReturn {
                ""
            }
        }

        val proteinRule = rule("Check Protein") {
            given {
                anyFloat()
            } and {
                it < 3
            } thenReturn {
                "Too little protein"
            } otherwiseReturn {
                ""
            }
        }
    }
}