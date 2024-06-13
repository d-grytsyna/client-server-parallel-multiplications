package org.example;

public class MatrixBlock {
    private Matrix[][] matrix;

    private int q;

    public MatrixBlock(int q) {
        this.q = q;
        this.matrix = new Matrix[q][q];
    }


    public Matrix getMatrix(int row, int col){
        return matrix[row][col];
    }

    public void setBlocks(Matrix splittedMatrix){
        int blockSize = splittedMatrix.getRows()/q;
        for(int i=0; i<q; i++){
            for(int j=0; j<q; j++){
                matrix[i][j] = splittedMatrix.getSubMatrix(i*blockSize, j*blockSize, blockSize);
            }
        }
    }

    public void setEmptyBlocks(Matrix splittedMatrix){
        int blockSize = splittedMatrix.getRows()/q;
        for(int i=0; i<q; i++){
            for(int j=0; j<q; j++){
                Matrix subMatrix = new Matrix(blockSize, blockSize);
                matrix[i][j] = subMatrix;
            }
        }
    }

    public void addElements(int row, int col, Matrix addMatrix){
        for(int i=0; i<addMatrix.getRows(); i++){
            for(int j=0; j<addMatrix.getCols(); j++){
                matrix[row][col].addElement(i, j, addMatrix.getElement(i,j));
            }
        }
    }

    public void printMatrix(){
        for(int i=0; i<q; i++){
            for (int j=0; j<q; j++){
                System.out.println("Matrix block i "+ i + " j " + j);
                matrix[i][j].printMatrix();
            }
        }
    }


    public Matrix blocksToMatrix(int q){
        int blockSize = matrix[0][0].getCols();
        int size = blockSize * q;
        Matrix resultMatrix = new Matrix(size, size);
            for(int i=0; i<q; i++){
            for(int j=0; j<q; j++){
                setBlockElements(matrix[i][j], resultMatrix, i*blockSize, j*blockSize);
            }
        }
        return resultMatrix;
    }

    private void setBlockElements(Matrix blockMatrix, Matrix resultMatrix, int iStart, int jStart){
        for(int i=0; i<blockMatrix.getRows(); i++){
            int jCounter = jStart;
            for(int j=0; j<blockMatrix.getCols(); j++){
                resultMatrix.setElement(iStart, jCounter, blockMatrix.getElement(i, j));
                jCounter++;
            }
            iStart++;
        }
    }

}
