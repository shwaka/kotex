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

public class Tabular(linePrefix: String = "") :
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
                argument = "c".repeat(this@Tabular.colCount),
            ) {
                addLines(super.toStringList())
            }
        }
        return texDocument.toStringList()
    }

    public companion object {
        public operator fun invoke(linePrefix: String = "", block: Tabular.() -> Unit): Tabular {
            return Tabular(linePrefix).apply(block)
        }
    }
}
