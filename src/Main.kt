package processor

import kotlin.math.pow

fun main() {
    while (true) {
        print("1. Add matrices\n" +
                "2. Multiply matrix by a constant\n" +
                "3. Multiply matrices\n" +
                "4. Transpose matrix\n" +
                "5. Calculate a determinant\n" +
                "6. Inverse matrix\n" +
                "0. Exit\n" +
                "Your choice: ")

        when (readLine()!!) {
            "0" -> break
            "1" -> matrixAdd()
            "2" -> matrixMlpByC()
            "3" -> matrixMlp()
            "4" -> matrixTrans()
            "5" -> printMatrixDet()
            "6" -> matrixInv()
        }
    }
}

fun inputMatrix(): Pair<List<Int>, Array<DoubleArray>> {
    print("Enter size of matrix: ")

    val size = readLine()!!.split(" ").map { it.toInt() }

    println("Enter matrix:")

    val matrix = Array(size[0]) { readLine()!!.split(" ").map { it.toDouble() }.toDoubleArray() }

    return Pair(size, matrix)
}

fun printMatrix(matrix: Array<DoubleArray>) {
    println("The result is:")
    matrix.forEach { println(it.joinToString(" ", transform = { d -> String.format("%.3f", d) })) }
    println()
}

fun printMatrixDet() {
    val (size, matrix) = inputMatrix()
    
    if (size[0] != size[1]) {
        println("A determinant is defined only for square matrices!")
    } else println("The result is:\n${matrixDet(size, matrix)}\n")
}

fun matrixAdd() {
    val (size1, matrix1) = inputMatrix()
    val (size2, matrix2) = inputMatrix()

    if (size1 != size2) {
        return println("The matrices must have an equal number of rows and columns to be added!")
    } else {
        for (n in matrix1.indices) {
            for (m in matrix1[0].indices) matrix1[n][m] += matrix2[n][m]
        }

        printMatrix(matrix1)
    }
}

fun matrixMlpByC() {
    val (_, matrix) = inputMatrix()

    print("Enter constant: ")

    val const = readLine()!!.toDouble()

    for (n in matrix.indices) {
        for (m in matrix[0].indices) matrix[n][m] *= const
    }

    printMatrix(matrix)
}

fun matrixMlp() {
    val (size1, matrix1) = inputMatrix()
    val (size2, matrix2) = inputMatrix()
    val result = Array(size1[0]) { DoubleArray(size2[1]) }

    if (size1[1] != size2[0]) {
        return println("The number of columns of the 1st matrix must be equal to the number of rows of the 2nd matrix!")
    } else {
        for (n in result.indices) {
            for (m in result[0].indices) {
                for (k in matrix1[0].indices) result[n][m] += matrix1[n][k] * matrix2[k][m]
            }
        }
    }

    printMatrix(result)
}

fun matrixTrans() {
    print("\n1. Main diagonal\n" +
            "2. Side diagonal\n" +
            "3. Vertical line\n" +
            "4. Horizontal line\n" +
            "Your choice: ")

    val input = readLine()!!
    val (size, matrix) = inputMatrix()
    val result = Array(size[0]) { DoubleArray(size[1]) }

    when (input) {
        "1" -> {
            for (m in matrix[0].indices) {
                for (n in matrix.indices) result[m][n] = matrix[n][m]
            }
        }
        "2" -> {
            for (m in matrix[0].indices) {
                for (n in matrix.indices) result[m][n] = matrix[matrix.lastIndex - n][matrix.lastIndex - m]
            }
        }
        "3" -> {
            for (m in matrix[0].indices) {
                for (n in matrix.indices) result[m][n] = matrix[m][matrix.lastIndex - n]
            }
        }
        "4" -> for (n in matrix.indices) result[n] = matrix[matrix.lastIndex - n]
    }

    printMatrix(result)
}

fun matrixDet(size: List<Int>, matrix: Array<DoubleArray>): Double {
    return when (size) {
        listOf(1, 1) -> matrix[0][0]
        listOf(2, 2) -> matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1]
        else -> matrix[0].foldIndexed(0.0, { i, acc, d -> acc + (d * (-1.0).pow(i + 2) * matrix.minorDet(0, i))})
    }
}

fun Array<DoubleArray>.minorDet(rowIndex: Int, columnIndex: Int): Double {
    val matrix = Array(this.size - 1) { DoubleArray(this[0].size - 1) }

    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            val n = if (i >= rowIndex) i + 1 else i
            val m = if (j >= columnIndex) j + 1 else j
            matrix[i][j] = this[n][m]
        }
    }

    return matrixDet(listOf(matrix.size, matrix[0].size), matrix)
}

fun matrixInv() {
    val (size, matrix) = inputMatrix()
    val result = Array(size[0]) { DoubleArray(size[1]) }
    val det = matrixDet(size, matrix)

    if (det == 0.0) return println("This matrix doesn't have an inverse.\n")

    for (m in matrix[0].indices) {
        for (n in matrix.indices) result[n][m] = (-1.0).pow(n + m + 2) * matrix.minorDet(m, n) * (1 / det)
    }

    printMatrix(result)
}
