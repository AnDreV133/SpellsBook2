package com.example.spellsbook.domain.usecase

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.repository.BookRepository
import com.example.spellsbook.domain.repository.SpellRepository
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class ConvertBookToPdfUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val bookRepository: BookRepository,
    private val localeEnum: LocaleEnum
) {
    suspend fun execute(bookId: Long): Result<Pair<String, InputStream>> {
        val book = bookRepository.get(bookId)
        val spellsJson = spellRepository.getSpellsJsonByBook(bookId, localeEnum)

        return withContext(Dispatchers.IO) {
            try {
                Result.success(
                    Pair(
                        "book_${book.name}.pdf",
                        convert(spellsJson)
                    )
                )
            } catch (e: IOException) {
                Result.failure(e)
            }
        }
    }

    private suspend fun convert(spellsJson: List<String>) = withContext(Dispatchers.Default) {
        val paddingOut = 45
        val paddingIn = 10
        val spellsOnPageAmount = 6
        val width = 1754
        val height = 1240
        val colAmount = 3
        val rowAmount = 2
        val cellWidth = (width - paddingOut * 2) / colAmount
        val cellHeight = (height - paddingOut * 2) / rowAmount

        val pdf = PdfDocument()
        val paintText = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        val paintCell = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        val pageInfo = { page: Int -> PdfDocument.PageInfo.Builder(width, height, page).create() }

        var row = 0
        var col = 0
        var page: PdfDocument.Page? = null
        var canvas: Canvas? = null

        spellsJson.forEachIndexed { index, spellJson ->
            if (col == 0 && row == 0) {
                pdf.startPage(pageInfo(index % spellsOnPageAmount)).also {
                    page = it
                    canvas = it.canvas
                }
            }

            val left = (col * cellWidth + paddingOut).toFloat()
            val top = (row * cellHeight + paddingOut).toFloat()
            val right = (left + cellWidth)
            val bottom = (top + cellHeight)
            canvas?.drawRect(
                left, top,
                right, bottom,
                paintCell
            )

            val wrappedText = wrapText(
                jsonToText(spellJson),
                paintText,
                cellWidth.toFloat() - paddingIn * 2,
                cellHeight.toFloat()
            )
            var textY = top + paintText.textSize + paddingIn
            for (line in wrappedText) {
                val textX = left + paddingIn
                canvas?.drawText(line, textX, textY, paintText)
                textY += paintText.textSize
            }

            if (index == spellsJson.size - 1) {
                page?.let { pdf.finishPage(it) }
                return@forEachIndexed
            }

            col++
            if (col == colAmount) {
                col = 0
                row++

                if (row == rowAmount) {
                    row = 0
                    page?.let { pdf.finishPage(it) }
                }
            }
        }

        val tempFile: File =
            withContext(Dispatchers.IO) {
                File.createTempFile("pdf", ".pdf").apply {
                    outputStream().use {
                        pdf.writeTo(it)
                        pdf.close()
                    }
                }
            }

        return@withContext tempFile.inputStream()
    }

    private suspend fun jsonToText(jsonString: String): String = withContext(Dispatchers.Default) {
        val sb = StringBuilder()
        val toLine = { title: String, text: JsonElement -> "${title}: ${text.asString}\n" }
        val titles = listOf(
            "name",
            "level",
            "duration",
            "materials",
        )

        JsonParser.parseString(jsonString).asJsonObject.let { json ->
            titles.forEach {
                sb.append(toLine(it, json[it]))
            }
            sb.append("\n")
                .append(json["description"].asString)
        }

        sb.toString()
    }

    private fun wrapText(
        text: String,
        paint: Paint,
        maxWidth: Float,
        maxHeight: Float
    ): List<String> {
        val words = text
            .replace("\n", " \n ")
            .split(" ")
        val lines = mutableListOf<String>()
        var currentLine = ""

        for (word in words) {
            if (word.isEmpty()) continue
            if (word == "\n") {
                lines.add(currentLine)
                currentLine = ""
                continue
            }

            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val testWidth = paint.measureText(testLine)

            if (testWidth > maxWidth) {
                lines.add(currentLine)
                currentLine = word
            } else {
                currentLine = testLine
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        val endByHeight = maxHeight / paint.textSize
        return if (endByHeight < lines.size) lines.subList(0, endByHeight.toInt()) else lines
    }
}


