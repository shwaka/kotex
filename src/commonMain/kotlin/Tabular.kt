package com.github.shwaka.kotex

public interface TabularInterface<T : TexScriptInterface<T>> : TexScriptInterface<T> {
    public fun addRow(cells: List<String>) {
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
    public companion object {
        public operator fun invoke(linePrefix: String = "", block: Tabular.() -> Unit): Tabular {
            return Tabular(linePrefix).apply(block)
        }
    }

    override fun newScript(linePrefix: String): Tabular {
        return Tabular(linePrefix)
    }
}
