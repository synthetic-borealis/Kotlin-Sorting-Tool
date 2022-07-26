package sorting

import java.io.File
import java.util.Scanner
import kotlin.math.roundToInt

val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    val settings = mutableMapOf(
        "sortingType" to "natural",
        "dataType" to "numbers"
    )

    var inputFileName = ""
    var outputFileName = ""

    for (i in 0 until args.lastIndex) {
        try {
            if (args[i].startsWith('-')) {
                when (args[i]) {
                    "-sortingType" -> settings["sortingType"] = when (args[i + 1]) {
                        "natural" -> "natural"
                        "byCount" -> "byCount"
                        else -> ""
                    }
                    "-dataType" -> settings["dataType"] = when (args[i + 1]) {
                        "word" -> "words"
                        "line" -> "lines"
                        "long" -> "numbers"
                        else -> ""
                    }
                    "-inputFile" -> inputFileName = args[i + 1]
                    "-outputFile" -> outputFileName = args[i + 1]
                    else -> throw Exception("\"${args[i]}\" is not a valid parameter. It will be skipped.")
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    try {
        when (settings["dataType"]) {
            "words" -> when (settings["sortingType"]) {
                "byCount" -> sortWordsByCount(inputFileName, outputFileName)
                "natural" -> sortWordsNatural(inputFileName, outputFileName)
                else -> throw Exception("No sorting type defined!")
            }
            "lines" -> when (settings["sortingType"]) {
                "byCount" -> sortLinesByCount(inputFileName, outputFileName)
                "natural" -> sortLinesNatural(inputFileName, outputFileName)
                else -> throw Exception("No sorting type defined!")
            }
            "numbers" -> when (settings["sortingType"]) {
                "byCount" -> sortIntegersByCount(inputFileName, outputFileName)
                "natural" -> sortIntegersNatural(inputFileName, outputFileName)
                else -> throw Exception("No sorting type defined!")
            }
            else -> throw Exception("No data type defined!")
        }
    } catch (e: Exception) {
        println(e.message)
    }
}

fun sortWordsByCount(inputFileName: String, outputFileName: String) {
    val words = readWords(inputFileName)

    val countMap = words.sortedBy { it }.groupingBy { it }.eachCount().toList()
        .sortedWith(compareBy<Pair<String, Int>> { it.second }.thenBy { it.first })
        .toMap()

    printLine(outputFileName, "Total words: ${words.size}")

    countMap.forEach {
        val percentage = (it.value.toDouble() / words.size.toDouble() * 100.0).roundToInt()
        val output = "${it.key}: ${it.value} time(s), ${percentage}%"

        printLine(outputFileName, output)
    }
}

fun sortWordsNatural(inputFileName: String, outputFileName: String) {
    val words = readWords(inputFileName)

    printLine(outputFileName, "Total words: ${words.size}.")
    printLine(outputFileName, "Sorted data: ${words.sorted().joinToString(" ")}")
}

fun readWords(fileName: String): MutableList<String> {
    return try {
        readWordsFromFile(fileName)
    } catch (e: Exception) {
        readWordsFromKeyboard()
    }
}

fun readWordsFromFile(fileName: String): MutableList<String> {
    val words = mutableListOf<String>()
    val file = File(fileName)
    val lines = file.readLines()

    for (line in lines) {
        words.addAll(line.split(" ").filterNot { it.isBlank() })
    }

    return words
}

fun readWordsFromKeyboard(): MutableList<String> {
    val words = mutableListOf<String>()

    while (scanner.hasNext()) {
        words.addAll(scanner.next().split(" ").filterNot { it.isBlank() })
    }

    return words
}

fun sortLinesByCount(inputFileName: String, outputFileName: String) {
    val lines = readLines(inputFileName)

    val countMap = lines.sortedBy { it }.groupingBy { it }.eachCount().toList()
        .sortedWith(compareBy<Pair<String, Int>> { it.second }.thenBy { it.first })
        .toMap()

    printLine(outputFileName, "Total lines: ${lines.size}")

    countMap.forEach {
        val percentage = (it.value.toDouble() / lines.size * 100.0).roundToInt()

        printLine(outputFileName, "${it.key}: ${it.value} time(s), $percentage%")
    }
}

fun sortLinesNatural(inputFileName: String, outputFileName: String) {
    val lines = readLines(inputFileName)

    while (scanner.hasNext()) {
        lines.add(scanner.nextLine())
    }

    printLine(outputFileName, "Total lines: ${lines.size}")

    lines.sorted().forEach {
        printLine(outputFileName, it)
    }
}

fun readLines(fileName: String): MutableList<String> {
    return try {
        readLinesFromFile(fileName)
    } catch (e: Exception) {
        readLinesFromKeyboard()
    }
}

fun readLinesFromFile(fileName: String): MutableList<String> {
    val file = File(fileName)

    return file.readLines().toMutableList()
}

fun readLinesFromKeyboard(): MutableList<String> {
    val lines = mutableListOf<String>()

    while (scanner.hasNext()) {
        lines.add(scanner.nextLine())
    }

    return lines
}

fun sortIntegersByCount(inputFileName: String, outputFileName: String) {
    val numbersList = readNumbers(inputFileName)

    val countMap = numbersList.sortedBy { it }.groupingBy { it }.eachCount().toList()
        .sortedWith(compareBy<Pair<Long, Int>> { it.second }.thenBy { it.first })
        .toMap()

    printLine(outputFileName, "Total numbers: ${numbersList.size}.")

    countMap.forEach {
        val percentage = (it.value.toDouble() / numbersList.size.toDouble() * 100.0).roundToInt()

        printLine(outputFileName, "${it.key}: ${it.value} time(s), $percentage%")
    }
}

fun sortIntegersNatural(inputFileName: String, outputFileName: String) {
    var numbersList = readNumbers(inputFileName)

    numbersList = mergeSort(numbersList)

    printLine(outputFileName, "Total numbers: ${numbersList.size}.")
    printLine(outputFileName, "Sorted data: ${numbersList.joinToString(" ")}")
}

fun readNumbers(fileName: String): MutableList<Long> {
    return try {
        readNumbersFromFile(fileName)
    } catch (e: Exception) {
        readNumbersFromKeyboard()
    }
}

fun readNumbersFromFile(fileName: String): MutableList<Long> {
    val numbersList = mutableListOf<Long>()
    val inputFile = File(fileName)
    val lines = inputFile.readLines()

    for (line in lines) {
        val tokens = line.split(" ").filter { it.isNotEmpty() }
        tokens.forEach {
            try {
                numbersList.add(it.toLong())
            } catch (e: Exception) {
                println("\"$it\" is not a valid parameter. It will be skipped.")
            }
        }
    }

    return numbersList
}

fun readNumbersFromKeyboard(): MutableList<Long> {
    val numbersList = mutableListOf<Long>()

    while (scanner.hasNext()) {
        val tokens = scanner.next().split(" ").filter { it.isNotEmpty() }
        tokens.forEach {
            try {
                numbersList.add(it.toLong())
            } catch (e: Exception) {
                println("\"$it\" is not a valid parameter. It will be skipped.")
            }
        }
    }

    return numbersList
}

fun printLine(fileName: String, message: String) {
    try {
        printLineToFile(fileName, message)
    } catch (e: Exception) {
        println(message)
    }
}

fun printLineToFile(fileName: String, message: String) {
    val file = File(fileName)
    file.appendText("$message\n")
}

fun mergeSort(numList: MutableList<Long>): MutableList<Long> {
    if (numList.size == 1) {
        return numList
    }

    val middle = numList.size / 2
    val left = numList.subList(0, middle)
    val right = numList.subList(middle, numList.size)
    val sortedLeft = mergeSort(left)
    val sortedRight = mergeSort(right)

    return merge(sortedLeft, sortedRight)
}

fun merge(left: MutableList<Long>, right: MutableList<Long>): MutableList<Long> {
    val merged = mutableListOf<Long>()
    var leftIndex = 0
    var rightIndex = 0

    while (leftIndex < left.size && rightIndex < right.size) {
        if (left[leftIndex] < right[rightIndex]) {
            merged.add(left[leftIndex])
            leftIndex++
        } else {
            merged.add(right[rightIndex])
            rightIndex++
        }
    }

    merged.addAll(left.subList(leftIndex, left.size))
    merged.addAll(right.subList(rightIndex, right.size))

    return merged
}
