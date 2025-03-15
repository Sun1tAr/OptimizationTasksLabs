package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SimplexMethod {

    public static void main(String[] args) {
        int[] target = {-2, 4, -1, 0, 0, 0};
        int[][] source = {{3, 3, 0, 1, 0,4},
                            {-1, 1, 1, 0, 0, 2},
                            {3, 4, 0, 0, -1, 4}};

        int n = source[1].length;
        int m = source.length;
        int l = m + n - 1;
        List<Integer> basis = new ArrayList(4);
        double[] result = new double[source[1].length];
        double[][] new_table = new double[m][l];

        double[][] table = new double[m][l];

        // Блок 1
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < l; j++) {
                if (j < n) {
                    table[i][j] = source[i][j];
                }
            }
            if (n + i < l) {
                table[i][n+1] = 1;
                basis.add(n+i);
            }
        }
//        int counter = 0;

        boolean flag = true;
        while (flag) {
            //Блок StopCriterion
            flag = true;
            for (int j = 0; j < l; j++) {
                if (Arrays.stream(table[m-1]).allMatch(w -> w > 0)) {

                    flag = false;
                }


            }
            // Блок 2
            int mainCol = 1;
            for (int j = 2; j < l; j++) {
                if (table[m-1][j] < table[m-1][mainCol]) {
                    mainCol = j;
                }
            }

            // Блок 3
            int mainRow = 1;
            for (int i = 0; i < m-1; i++) {
                if (table[i][mainCol] > 0) {
                    mainRow = i;
                }
            }

            for (int i = mainRow + 1; i < m - 1; i++) {
                if (table[i][mainCol] > 0) {
                    if (table[i][0] / table[i][mainCol] <
                        table[mainRow][0] / table[mainRow][mainCol]) {
                        mainRow = i;

                    }
                }
            }


            // Блок 4
            for (int j = 0; j < l; j++) {
                new_table[mainRow][j] = table[mainRow][j] / table[mainRow][mainCol];
            }

            for (int i = 0; i < m; i++) {
                if (i != mainRow) {
                    for (int j = 0; j < l; j++) {
                        if (table[i][mainCol]!= 0) {
//                            System.out.println(table[i][mainCol]);
                        }
                        new_table[i][j] =
                                table[i][j] - table[i][mainCol] * table[mainRow][j];
                    }
                }
            }
//            System.out.println(Arrays.deepToString(table));
            table = new_table;





        }

        for (int i = 0; i < result.length; i++) {
            if (basis.contains(i+1)){
                int k = basis.indexOf(i+1);
                result[i] = table[k][0];
            } else {
                result[i] = 0;
            }
        }
        System.out.println(Arrays.toString(result));
        double sum = 0;
        for (int i = 0; i < result.length; i++) {
            sum += result[i] * target[i];
        }
        System.out.println(sum);
//        System.out.println(counter);


    }







}
