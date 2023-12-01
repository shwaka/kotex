package com.github.shwaka.kotex

public interface TexDocumentInterface<T : TexScriptInterface<T>> : TexScriptInterface<T> {
    public fun begin(
        name: String,
        argument: String? = null,
        option: String? = null,
        indent: Int = 2,
        block: T.() -> Unit
    ) {
        val argumentString: String = if (argument == null) "" else "{$argument}"
        val optionString: String = if (option == null) "" else "[$option]"
        this.addLines("\\begin{$name}$optionString$argumentString")
        this.withIndent(indent) {
            block()
        }
        this.addLines("\\end{$name}")
    }

    public fun tabular(
        verticalLines: List<Int> = emptyList(),
        block: Tabular.() -> Unit
    ) {
        addScript(Tabular(verticalLines = verticalLines, block = block))
    }

    public fun section(name: String) {
        this.simpleCommand("section", name)
    }
    public fun sectionStar(name: String) {
        this.simpleCommand("section*", name)
    }

    public fun subsection(name: String) {
        this.simpleCommand("subsection", name)
    }
    public fun subsectionStar(name: String) {
        this.simpleCommand("subsection*", name)
    }
}

@TexArticleDslMarker
public class TexDocument(linePrefix: String = "") :
    ScriptBase<TexDocument>(linePrefix),
    TexDocumentInterface<TexDocument> {
    public companion object {
        public operator fun invoke(linePrefix: String = "", block: TexDocument.() -> Unit): TexDocument {
            return TexDocument(linePrefix).apply(block)
        }
    }

    override fun newScript(linePrefix: String): TexDocument {
        return TexDocument(linePrefix)
    }
}
