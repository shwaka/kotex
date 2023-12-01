package com.github.shwaka.kotex

import io.kotest.core.NamedTag
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

val tabularTag = NamedTag("Tabular")

class TabularTest : FreeSpec({
    tags(scriptTag, tabularTag)

    "test simple Tabular" {
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

    "test hline" {
        val tabular = Tabular {
            addRow(listOf("a", "b"))
            hline()
            addRow(listOf("c", "d"))
            hline()
        }
        tabular.toString() shouldBe """
            |\begin{tabular}{cc}
            |  a & b \\
            |  \hline
            |  c & d \\
            |  \hline
            |\end{tabular}
        """.trimMargin()
    }

    "test verticalLines" {
        val tabular = Tabular(verticalLines = listOf(1, 2)) {
            addRow(listOf("a", "b"))
            addRow(listOf("c", "d"))
        }
        tabular.toString() shouldBe """
            |\begin{tabular}{c|c|}
            |  a & b \\
            |  c & d \\
            |\end{tabular}
        """.trimMargin()
    }
})
