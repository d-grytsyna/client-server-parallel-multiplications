package org.example;
import java.io.*;
import java.net.*;
public class MatrixClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java MatrixClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        ) {
            Boolean serverSideMatrix = true;

            if(serverSideMatrix) {
                // Send a request to start calculations
                long startTime = System.currentTimeMillis();
                out.println("START_CALCULATION");
                System.out.println("Request to start calculations sent to server. Waiting for response...");
                int size = Integer.parseInt(in.readLine());
                // Read the result matrix from the server

                Integer[][] resultMatrix = new Integer[size][size];
                for (int i = 0; i < size; i++) {
                    String rowLine = in.readLine();
                    resultMatrix[i] = Service.convertStringToRow(rowLine, size);
                }
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                System.out.println("Request time took " + elapsedTime);

                System.out.println("Result matrix received from server:");
//                Service.printMatrix(resultMatrix);

            }else{
            int size = 2000;

            Matrix A = new Matrix(size, size);
            A.generateMatrix();
//            A.writeToFile("A");


            Matrix B = new Matrix(size, size);
            B.generateMatrix();
//            B.writeToFile("B");


            long startTime = System.currentTimeMillis();

            // Send the size first
            out.println(size);


            Integer[][] matrixA = A.getMatrix();
            for (Integer[] row : matrixA) {
                out.println(Service.convertRowToString(row));
            }

            // Send a separator line
            out.println("MATRIX_B_START");

            Integer[][] matrixB = B.getMatrix();
            for (Integer[] row : matrixB) {
                out.println(Service.convertRowToString(row));
            }

            System.out.println("Matrices sent to server. Waiting for response...");

            String serverResponse = in.readLine();
            System.out.println("Server response: " + serverResponse);

            // Read the result matrix
            Integer[][] resultMatrix = new Integer[size][size];
            for (int i = 0; i < size; i++) {
                String rowLine = in.readLine();
                resultMatrix[i] = Service.convertStringToRow(rowLine, size);
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Request time took " + elapsedTime);

            System.out.println("Result matrix received from server:");
//            Service.printMatrix(resultMatrix);
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }


}