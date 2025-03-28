package org.example;

import java.util.Arrays;


public class SimplexMethod {

    public static void calculate(int[] target, int[][] source) {





        int n = source[1].length - 2;               // кол-во переменных (-2 тк там еще значение за знаком и сам знак)
        int m = source.length;                      // кол-во ограничений
        int l = m + n;

        int[] basisIndexes = new int[m];            // Хранятся индексы базисов из таблицы
        double[] targetBasis = new double[m];       // Вектор базисов ЦФ (коэф С)
        double[] vectorB = new double[m + 1];          // Вектор Значений за знаком (коэф b) + 1 тк включена строка оценок
        double[] newVectorB;
        double[] newTarget = new double[l];
        double[] estimation = new double[l];        // Строка оценок
        double[][] table = new double[m + 1][l];    // Последняя строка - строка оценок
        double[][] new_table;

        // Блок 1 (инициализация рабочих таблиц, векторов, строк)

        for (int i = 0; i < m; i++) {
            boolean addBasis = source[i][source[i].length - 1] != 0;

            int invertor = 1;
            if (source[i][source[i].length - 2] < 0) {
                invertor = -1;
            }
            basisIndexes[i] = -1;
            for (int j = 0; j < l; j++) {
                if (j < n) {
                    table[i][j] = source[i][j] * invertor;
                    newTarget[j] = target[j];
                } else {
                    if ((j == n + i)) {
                        if (addBasis) {
                            table[i][j] = -source[i][source[i].length - 1] * invertor;
                            basisIndexes[i] = i + n;
                        } else {
                            basisIndexes[i] = i + n;
                        }
                    }
                }
            }
            vectorB[i] = source[i][source[i].length - 2] * invertor;
        }

        for (int i = 0; i < l; i++) {
            estimation[i] = -newTarget[i];
        }
        table[m] = estimation;

        for (int i = 0; i < m; i++) {
            vectorB[m] += vectorB[i] * targetBasis[i];
        }


        while (!(Arrays.stream(table[m]).allMatch(w -> w >= 0) & vectorB[m] >= 0)) {    //TODO < - минимум
            //Блок StopCriterion


            // Блок 2
            int mainCol = 0;
            double ctrlCol = Integer.MIN_VALUE;
            for (int i = 0; i < l; i++) {
                if (table[m][i] < 0 & Math.abs(table[m][i]) > ctrlCol) {                    //TODO >> - min, <> - max
                    mainCol = i;
                    ctrlCol = Math.abs(table[m][i]);
                }
            }

            // Блок 3
            int mainRow = 0;
            double ctrlRow = Double.MAX_VALUE;
            for (int i = 0; i < m; i++) {
                double ctrl = vectorB[i] / table[i][mainCol];
                if (ctrl >= 0 & ctrl < ctrlRow) {
                    ctrlRow = ctrl;
                    mainRow = i;
                }
            }
            if (ctrlRow == Double.MAX_VALUE) {
                System.err.println("Solve not found!");
            }

            // Алгоритм изменения базиса
            basisIndexes[mainRow] = mainCol;
            for (int i = 0; i < targetBasis.length; i++) {
                if (basisIndexes[i] != -1) {
                    targetBasis[i] = newTarget[basisIndexes[i]];
                }
            }
            basisIndexes[mainRow] = mainCol;
            targetBasis[mainRow] = newTarget[mainCol];

            // Блок 4 - Правило прямоугольника
            new_table = new double[m + 1][l];
            newVectorB = new double[m + 1];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < l; j++) {
                    if (i == mainRow) {
                        new_table[i][j] = table[i][j] / table[i][mainCol];
                    } else {
                        new_table[i][j] =
                                (table[i][j] - table[i][mainCol] * table[mainRow][j] / table[mainRow][mainCol]);

                    }
                }
                if (i == mainRow) {
                    newVectorB[i] = vectorB[i] / table[i][mainCol];
                } else {
                    newVectorB[i] =
                            (vectorB[i] - table[i][mainCol] * vectorB[mainRow] / table[mainRow][mainCol]);
                }

            }
            table = new_table;

            // Заполняем ряд оценок
            for (int i = 0; i < estimation.length; i++) {
                double scalarMultOfCnB = 0;
                for (int j = 0; j < m; j++) {
                    scalarMultOfCnB += targetBasis[j] * table[j][i];
                }
                estimation[i] = scalarMultOfCnB - newTarget[i];
            }
            table[m] = estimation;
            for (int i = 0; i < m; i++) {
                newVectorB[m] += newVectorB[i] * targetBasis[i];
            }
            vectorB = newVectorB;
        }
        // TODO Алгоритм вывода ответа
        double[] tempVB = new double[l + 1];
        for (int i =0; i < vectorB.length; i++) {
            if (i == vectorB.length - 1) {
                tempVB[tempVB.length-1] = vectorB[i];
            } else {
                tempVB[i] = vectorB[i];
            }
        }
        double sum = 0;
        StringBuilder output = new StringBuilder();
//        System.out.println(Arrays.toString(vectorB));
//        System.out.println(Arrays.toString(basisIndexes));

        for (int i = 0; i < basisIndexes.length; i++) {
            sum += newTarget[basisIndexes[i]] * tempVB[i];
            if (basisIndexes[i] < n) {

                output.append("x_")
                        .append(basisIndexes[i] + 1)
                        .append(" = ")
                        .append(tempVB[i])
                        .append("\n");
            }
        }
        output.append("Target func value = ")
                .append(sum);
        System.out.println(output);

    }
}
