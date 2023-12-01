package com.github.shwaka.kotex

import kotlin.math.max

public interface TabularInterface<T : TexScriptInterface<T>> : TexScriptInterface<T> {
    public var colCount: Int

    public fun addRow(cells: List<String>) {
        this.colCount = max(this.colCount, cells.size)
        val line: String = cells.joinToString(" & ") + " \\\\"
        this.addLines(line)
    }

    public fun hline() {
        this.addLines("\\hline")
    }
}

public class Tabular(
    linePrefix: String = "",
    public val verticalLines: List<Int> = emptyList(),
) :
    ScriptBase<Tabular>(linePrefix),
    TabularInterface<Tabular> {

    override var colCount: Int = 0

    override fun newScript(linePrefix: String): Tabular {
        return Tabular(linePrefix)
    }

    override fun toStringList(): List<String> {
        val texDocument = TexDocument {
            begin(
                "tabular",
                argument = Tabular.getPositionSpecification(this@Tabular.colCount, this@Tabular.verticalLines),
            ) {
                addLines(super.toStringList())
            }
        }
        return texDocument.toStringList()
    }

    public companion object {
        public operator fun invoke(
            linePrefix: String = "",
            verticalLines: List<Int> = emptyList(),
            block: Tabular.() -> Unit,
        ): Tabular {
            return Tabular(linePrefix, verticalLines).apply(block)
        }

        private fun getPositionSpecification(colCount: Int, verticalLines: List<Int>): String {
            require(
                verticalLines.all { it <= colCount }
            )
            val specAsList = (0 until colCount).map { i ->
                if (verticalLines.contains(i)) {
                    "|c"
                } else {
                    "c"
                }
            } + listOf(
                if (verticalLines.contains(colCount)) {
                    "|"
                } else {
                    ""
                }
            )
            return specAsList.joinToString("")
        }
    }
}
