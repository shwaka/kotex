package com.github.shwaka.kotex

import io.kotest.core.NamedTag
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

val tabularTag = NamedTag("Tabular")

class TabularTest : FreeSpec({
    tags(scriptTag, tabularTag)

    "test Tabular" {
        val tabular = Tabular {
            addRow(listOf("a", "b"))
            addRow(listOf("c", "d"))
        }
        tabular.toString() shouldBe """
            |\begin{tabular}{cc}
            |  a & b \\
            |  c & d \\
            |\end{tabular}
        """.trimMargin()
    }
})
