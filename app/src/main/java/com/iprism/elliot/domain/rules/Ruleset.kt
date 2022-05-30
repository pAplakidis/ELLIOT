package com.iprism.elliot.domain.rules

import io.insource.framework.rule.rule

class Ruleset {
    companion object Rules {
        val emptyRule = rule("Empty string") {
            given {
                anyString()
            } and {
                it.isEmpty()
            } thenDo {
                println("Empty string")
            } otherwiseDo {
                println("Not an empty string")
            }
        }
    }
}