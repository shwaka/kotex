package com.github.shwaka.kotex

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TexDocumentTest : FreeSpec({
    tags(scriptTag)

    "begin() should work" {
        val texDocument = TexDocument {
            addLines("foo")
            begin("equation") {
                addLines("a^2 + b^2 = c^2")
            }
            addLines("bar")
        }
        texDocument.toString() shouldBe """
            |foo
            |\begin{equation}
            |  a^2 + b^2 = c^2
            |\end{equation}
            |bar
        """.trimMargin()
    }

    "test begin() with arguments and options" {
        val texDocument = TexDocument {
            addLines("foo")
            begin("bar", argument = "arg", option = "opt") {
                addLines("baz")
            }
        }
        texDocument.toString() shouldBe """
            |foo
            |\begin{bar}[opt]{arg}
            |  baz
            |\end{bar}
        """.trimMargin()
    }

    "test section()" {
        val texDocument = TexDocument {
            section("foo")
        }
        texDocument.toString() shouldBe "\\section{foo}"
    }

    "test sectionStar()" {
        val texDocument = TexDocument {
            sectionStar("foo")
        }
        texDocument.toString() shouldBe "\\section*{foo}"
    }

    "test subsection()" {
        val texDocument = TexDocument {
            subsection("foo")
        }
        texDocument.toString() shouldBe "\\subsection{foo}"
    }

    "test subsectionStar()" {
        val texDocument = TexDocument {
            subsectionStar("foo")
        }
        texDocument.toString() shouldBe "\\subsection*{foo}"
    }

    "test tabular()" {
        val texDocument = TexDocument {
            tabular {
                addRow(listOf("a", "b"))
                addRow(listOf("c", "d"))
            }
        }
        texDocument.toString() shouldBe """
            |\begin{tabular}{cc}
            |  a & b \\
            |  c & d \\
            |\end{tabular}
        """.trimMargin()
    }

    "test tabular() with table" {
        val texDocument = TexDocument {
            begin("table", option = "ht") {
                tabular {
                    addRow(listOf("a", "b"))
                    addRow(listOf("c", "d"))
                }
            }
        }
        texDocument.toString() shouldBe """
            |\begin{table}[ht]
            |  \begin{tabular}{cc}
            |    a & b \\
            |    c & d \\
            |  \end{tabular}
            |\end{table}
        """.trimMargin()
    }

    "test tabular() with verticalLines" {
        val texDocument = TexDocument {
            tabular(verticalLines = listOf(0, 1)) {
                addRow(listOf("a", "b"))
                addRow(listOf("c", "d"))
            }
        }
        texDocument.toString() shouldBe """
            |\begin{tabular}{|c|c}
            |  a & b \\
            |  c & d \\
            |\end{tabular}
        """.trimMargin()
    }
})
