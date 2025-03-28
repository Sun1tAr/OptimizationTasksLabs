package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        int[] target = {-2, 4, -1};     // ЦФ заполняется коэффициентами от 1 до n, если в ЦФ нет базиса - не ставятся
        int[][] source = {  {3,3,0,4,-1},       // Последний столбец - оператор: > "1", < "-1", = "0"
                            {-1,1,1,2,0},
                            {3,4,0,4, 1}};

//        int[] target = {3, 4};     // ЦФ заполняется коэффициентами от 1 до n, если в ЦФ нет базиса - не ставятся
//        int[][] source = {  {4,1,8,-1},       // Последний столбец - оператор: > "1", < "-1", = "0"
//                {1,-1,-3,1}};

        SimplexMethod.calculate(target, source);
    }
}