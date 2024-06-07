package cn.quibbler.codetop.link;

public class FindNumInMatrix {

    public static void main(String[] args) {
        int[][] matrix = new int[][]{{1, 2, 8, 9}, {2, 4, 9, 12}, {4, 7, 10, 13}, {6, 8, 11, 15}};
        boolean found = findNumInMatrix(matrix, 1);
        System.out.println("found = " + found);
    }

    public static boolean findNumInMatrix(int[][] matrix, int num) {
        boolean found = false;

        int rows = matrix.length, columns = matrix[0].length;

        int row = 0, column = columns - 1;
        while (row < rows && column >= 0) {
            if (matrix[row][column] == num) {
                found = true;
                break;
            }
            if (matrix[row][column] > num) {
                --column;
            } else {
                ++row;
            }
        }

        return found;
    }

}
