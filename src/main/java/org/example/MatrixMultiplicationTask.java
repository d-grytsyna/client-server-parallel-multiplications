package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MatrixMultiplicationTask extends RecursiveTask<Matrix> {

    private Matrix m1;

    private Matrix m2;
    private int q;

    public MatrixMultiplicationTask(Matrix m1, Matrix m2, int q) {
        this.m1 = m1;
        this.m2 = m2;
        this.q =q;
    }

    @Override
    protected Matrix compute() {
        if(m1.getRows()%q!=0||m1.getRows()<200){
            return Service.multiplyMatrix(m1, m2);
        }else{
            int blocks = q * q;
            //Split Matrix A into blocks
            MatrixBlock matrixBlockM1 = new MatrixBlock(q);
            matrixBlockM1.setBlocks(m1);

            //Split Matrix B into blocks
            MatrixBlock matrixBlockM2 = new MatrixBlock(q);
            matrixBlockM2.setBlocks(m2);

            //Set empty blocks in result matrix
            MatrixBlock resultBlocks = new MatrixBlock(q);
            resultBlocks.setEmptyBlocks(m1);
            for (int t = 0; t < blocks; t++) {
                int startIndex = t;
                int resultRow;
                int resultCol;
                if(startIndex<q){
                    resultRow = 0;
                    resultCol = startIndex;
                }else{
                    resultRow = startIndex/q;
                    resultCol = startIndex%q;
                }
                int i = resultRow;
                int i2 = resultRow;
                int j = resultCol;
                List<RecursiveTask<Matrix>> tasksList = new ArrayList<>();
                for(int c=0; c<q; c++){
                    Matrix a = matrixBlockM1.getMatrix(i, i2);
                    Matrix b = matrixBlockM2.getMatrix(i2, j);
                    MatrixMultiplicationTask task = new MatrixMultiplicationTask(a,b,q);
                    tasksList.add(task);
                    task.fork();
                    i2++;
                    if(i2>=q) i2 = 0;
                }

                for(int c=0; c<q; c++){
                    Matrix subResult = tasksList.get(c).join();
                    resultBlocks.addElements(resultRow, resultCol, subResult);
                }

            }
            Matrix finalResult  = resultBlocks.blocksToMatrix(q);
            return finalResult;


        }

    }

}
