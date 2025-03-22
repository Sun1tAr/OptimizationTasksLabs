package org.example;

import java.util.Arrays;


public class SimplexMethod {

    public static void main(String[] args) {
        int[] target = {-2, 4, -1};     // ЦФ заполняется коэффициентами от 1 до n, если в ЦФ нет базиса - не ставятся
//        int[] target = {1, -2};
        int mode = 1;  // "1" - максимум, "-1" - минимум

        int[][] source = {  {3,3,0,4,-1},       // Последний столбец - оператор: > "1", < "-1", = "0"
                            {-1,1,1,2,0},
                            {3,4,0,4, 1}};

//        int[][] source = {  {1, -1, 3, -1},
//                {3, -4, -12, 1},
//                {1, 0, 5, -1}};



        int n = source[1].length - 2;               // кол-во переменных (-2 тк там еще значение за знаком и сам знак)
        int m = source.length;                      // кол-во ограничений
        int l = m + n;

        int[] basisIndexes = new int[m];            // Хранятся индексы базисов из таблицы
        double[] targetBasis = new double[m];       // Вектор базисов ЦФ (коэф С)
        double[] vectorB = new double[m + 1];          // Вектор Значений за знаком (коэф b) + 1 тк включена строка оценок
        double[] newTarget = new double[l];
        double[] estimation = new double[l];        // Строка оценок
        double[][] table = new double[m + 1][l];    // Последняя строка - строка оценок
        double[][] new_table = new double[m + 1][l];

        // Блок 1 (инициализация рабочих таблиц, векторов, строк)

        for (int i = 0; i < target.length; i++) {
            target[i] = target[i] * mode * -1;
        }


        for (int i = 0; i < m; i++) {
            int multiplier;
            boolean addBasis;
            switch (source[i][source[i].length - 1]) {
                case 1:
                    multiplier = -1;
                    addBasis = true;
                    break;
                case -1:
                    multiplier = 1;
                    addBasis = true;
                    break;
                default:
                    multiplier = 1;
                    addBasis = false;
            }
            basisIndexes[i] = -1;
            for (int j = 0; j < l; j++) {

                if (j < n) {
                    table[i][j] = multiplier * source[i][j];
                    newTarget[j] = target[j];
                } else {
                    if ((j == n + i) & (addBasis)) {
                        table[i][j] = 1;
                        basisIndexes[i] = i + n;
                    }
                }
            }
            vectorB[i] = source[i][source[i].length - 2] * multiplier;

        }

        vectorB[m] = 0;
        while (true) {
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
                vectorB[m] += vectorB[i] * targetBasis[i];
            }


            System.out.println(Arrays.deepToString(table));
            //Блок StopCriterion
            if (Arrays.stream(table[m]).allMatch(w -> w <= 0) & vectorB[m] <= 0){
                break;
            }


            // Блок 2
            int mainCol = 0;
            for (int i = 0; i < table[m].length; i++) {
                if (table[m][i] > table[m][mainCol]) {
                    mainCol = i;
                }
            }

            // Блок 3
            int mainRow = 0;
            for (int i = 0; i < m; i++) {
                if (table[i][mainCol] <= 0 || vectorB[i] < 0) {
                    continue;
                }

                if (vectorB[i] / table[i][mainCol] <  vectorB[i] / table[mainRow][mainCol] ) {
                    mainRow = i;
                }
            }



            // Блок 4 - Правило прямоугольника
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
                    vectorB[i] = vectorB[i] / table[i][mainCol];
                } else {
                    vectorB[i] =
                            (vectorB[i] - table[i][mainCol] * vectorB[mainRow] / table[mainRow][mainCol]);
                }

            }
            table = new_table;

            // TODO Алгоритм изменения базиса
            basisIndexes[mainRow] = mainCol;
            for (int i = 0; i < targetBasis.length; i++) {
                if (basisIndexes[i] != -1) {
                    targetBasis[i] = newTarget[basisIndexes[i]];
                }
            }

        }

        System.out.println(Arrays.toString(vectorB));



        // Приводим к канонич виду ЦФ
//        if (mode == 1) {
//            for (int t : target) {
//                t = -t;
//            }
//        }

//        for (int i = 0; i < result.length; i++) {
//            if (basis.contains(i + 1)) {
//                int k = basis.indexOf(i + 1);
//                result[i] = table[k][0];
//            } else {
//                result[i] = 0;
//            }
//        }
//        System.out.println(Arrays.toString(result));
//        double sum = 0;
//        for (int i = 0; i < result.length; i++) {
//            if (i < target.length) {
//                sum += result[i] * target[i];
//            }
//        }
//        System.out.println(sum);


    }


}
