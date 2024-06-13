package org.example;

import java.io.*;
import java.util.Random;

public class Matrix {
    private Integer[][] matrix;

    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new Integer[rows][cols];
    }

    public Integer[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Integer[][] matrix){
        this.matrix = matrix;
    }

    public void generateMatrix(){
        Random random = new Random();
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                int randomInt = random.nextInt(10);
                matrix[i][j] = randomInt;
            }
        }
    }

    public void writeToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(Integer.toString(matrix[i][j]));
                    writer.write(" ");
                }
                writer.newLine();
            }
            System.out.println("Matrix has been successfully written to file: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing matrix to file: " + e.getMessage());
        }
    }

    public void readFromFile(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < rows) {
                String[] values = line.trim().split("\\s+");
                for (int col = 0; col < cols && col < values.length; col++) {
                    matrix[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }
            System.out.println("Matrix has been successfully read from file: " + fileName);
        } catch (IOException e) {
            System.err.println("Error reading matrix from file: " + e.getMessage());
        }
    }
    public void printMatrix(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void setElement(int row, int col, Integer element){
        matrix[row][col] = element;
    }
    public Integer getElement(int row, int col){
        return matrix[row][col];
    }
    public Integer[] getRow(int row){
        return matrix[row];
    }

    public Integer[] getCol(int col){
        Integer[] column = new Integer[rows];
        for (int i = 0; i < rows; i++) {
            column[i] = matrix[i][col];
        }
        return column;
    }

    public void addElement(int row, int col, Integer element){
        if(matrix[row][col]==null) matrix[row][col] = 0;
        matrix[row][col] += element;
    }
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Matrix getSubMatrix(int iStart, int jStart, int blockSize){
        Matrix subMatrix = new Matrix(blockSize, blockSize);
        for (int i=0; i<blockSize; i++){
            int jCounter = jStart;
            for (int j=0; j<blockSize; j++){
                subMatrix.setElement(i, j, getElement(iStart, jCounter));
                jCounter++;
            }
            iStart++;
        }
        return subMatrix;
    }

}