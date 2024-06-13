package org.example;

public class Service {
    public static Matrix multiplyMatrix(Matrix m1, Matrix m2){
        Matrix resultMatrix = new Matrix(m1.getRows(), m1.getCols());
        for(int i=0; i<m1.getRows(); i++){
            for(int j=0; j<m2.getCols(); j++){
                resultMatrix.setElement(i, j, multiplyRowByColumn(m1.getRow(i), m2.getCol(j)));
            }
        }
        return resultMatrix;
    }
    private static Integer multiplyRowByColumn(Integer[] row, Integer[] column){
        Integer result = 0;
        for(int i=0; i<row.length; i++){
            result += row[i] * column[i];
        }
        return result;
    }

    public static Boolean matrixEqual(Matrix m1, Matrix m2){
        for(int i=0; i<m1.getRows(); i++){
            for(int j=0; j<m1.getCols(); j++){
                if(m1.getElement(i,j).intValue()!=m2.getElement(i,j).intValue()) return false;
            }
        }
        return true;
    }

    public static void printMatrix(Integer[][] matrix) {
        for (Integer[] row : matrix) {
            for (int elem : row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }

    public static Integer[] convertStringToRow(String rowString, int size) {
        Integer[] row = new Integer[size];
        String[] elements = rowString.split(",");
        for (int j = 0; j < elements.length; j++) {
            row[j] = Integer.parseInt(elements[j]);
        }
        return row;
    }
    public static String convertRowToString(Integer[] row) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < row.length; j++) {
            sb.append(row[j]);
            if (j < row.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
