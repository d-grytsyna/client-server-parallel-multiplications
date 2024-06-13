package org.example;

import java.net.*;
import java.io.*;
import java.util.concurrent.ForkJoinPool;

public class MatrixServer {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java MatrixServer <port number>");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);

        int sizeServer = 2000;
        int q = 2;

        Matrix Aserver = new Matrix(sizeServer, sizeServer);
        Aserver.generateMatrix();
        Aserver.writeToFile("Aserver");


        Matrix Bserver = new Matrix(sizeServer, sizeServer);
        Bserver.generateMatrix();
        Bserver.writeToFile("Bserver");

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =  new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
        ) {
            System.out.println("Client connected");
            String clientRequest = in.readLine();

            if ("START_CALCULATION".equals(clientRequest)) {
                System.out.println("Received start calculation request");
                out.println(sizeServer);

                long startTime = System.currentTimeMillis();
                ForkJoinPool forkJoinPool = new ForkJoinPool();
                Matrix forkJoinMatrix = forkJoinPool.invoke(new MatrixMultiplicationTask(Aserver, Bserver, q));
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                System.out.println("Fork join avg time " + elapsedTime);

                Integer[][] forkJoinResult = forkJoinMatrix.getMatrix();
                for (Integer[] row : forkJoinResult) {
                    out.println(Service.convertRowToString(row));
                }

            }else{
            int size = Integer.parseInt(clientRequest);
            Integer[][] matrixA = new Integer[size][size];
            Integer[][] matrixB = new Integer[size][size];

            for (int i = 0; i < size; i++) {
                String rowLine = in.readLine();
                matrixA[i] = Service.convertStringToRow(rowLine, size);
            }

            String separatorLine = in.readLine();
            if (!"MATRIX_B_START".equals(separatorLine)) {
                throw new IOException("Expected separator line not found.");
            }

            for (int i = 0; i < size; i++) {
                String rowLine = in.readLine();
                matrixB[i] = Service.convertStringToRow(rowLine, size);
            }

            System.out.println("Matrix A:");
//            Service.printMatrix(matrixA);

            System.out.println("Matrix B:");
//            Service.printMatrix(matrixB);

            Matrix A = new Matrix(size, size);
            Matrix B = new Matrix(size, size);

            A.setMatrix(matrixA);
            B.setMatrix(matrixB);

            out.println("Matrices received and processed");

            long startTime = System.currentTimeMillis();
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            Matrix forkJoinMatrix = forkJoinPool.invoke(new MatrixMultiplicationTask(A, B, q));
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Fork join avg time " + elapsedTime);

            Integer[][] forkJoinResult = forkJoinMatrix.getMatrix();
            for (Integer[] row : forkJoinResult) {
                out.println(Service.convertRowToString(row));
            }
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }



}