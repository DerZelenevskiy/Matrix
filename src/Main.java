import Complex.ComplexNumber;
import Matrix.ComplexMatrix;
import Matrix.MatrixArithmetic;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("----- Matrix calculator -----");
            System.out.println("1 - det(A)    2 - transpose");
            System.out.println("3 - A+B       4 - A-B");
            System.out.println("5 - A*B       6 - A/B");
            System.out.println("q - quit");
            var com = sc.nextLine();
            switch (com) {
                case "1": determinant(); break;
                case "2": transpose(); break;
                case "3": add(); break;
                case "4": subtract(); break;
                case "5": multiply(); break;
                case "6": divide(); break;
                case "q": return;
                default: System.out.println("Unknown command");
            }
        }
    }
    static ComplexMatrix getMatrix(String label) {
        System.out.println("Enter matrix %s:".formatted(label));
        int n, m;
        while (true) {
            System.out.print("dim(%s)=".formatted(label));
            var dim = sc.nextLine();
            dim = dim.replace("x", " ").replace(",", " ");
            var tk = new StringTokenizer(dim, " ");
            if (tk.countTokens() == 2){
                m = Integer.parseInt(tk.nextToken());
                n = Integer.parseInt(tk.nextToken());
                break;
            }
            else if (tk.countTokens() == 1) {
                m = Integer.parseInt(tk.nextToken());
                n = m;
                break;
            }
            System.out.println("Invalid dimensions input");
        }
        while (true) {
            System.out.println("%s=".formatted(label));
            var matrix = new ComplexNumber[n*m];
            var success1 = true;
            for (int i = 0; i < m; ++i) {
                var row = new StringTokenizer(sc.nextLine(), " ");
                if (row.countTokens() != n) {
                    System.out.println("Invalid input");
                    success1 = false;
                    break;
                }
                var success2 = true;
                for (int j = 0; j < n; ++j) {
                    var token = row.nextToken();
                    ComplexNumber num;
                    if (token.matches("^[+-]?\\d+\\.?\\d*$") || token.matches("^[+-]?0?\\.\\d+$")) {
                        num = new ComplexNumber(Double.parseDouble(token));
                    }
                    else if (token.matches("^[+-]?\\d+\\.?\\d*i$") || token.matches("^[+-]?0?\\.\\d+i$")) {
                        num = new ComplexNumber(0, Double.parseDouble(token.replace("i", "")));
                    }
                    else if (token.equals("i")) {
                        num = new ComplexNumber(0, 1);
                    }
                    else if (token.equals("-i")) {
                        num = new ComplexNumber(0, -1);
                    }
                    else if (token.matches("^[+-]?\\d+\\.?\\d*\\+\\d+\\.?\\d*i$") || token.matches("^[+-]?0?\\.\\d+\\+\\d+\\.?\\d*i$") || token.matches("^[+-]?\\d+\\.?\\d*\\+0?\\.\\d+i$") || token.matches("^[+-]?0?\\.\\d+\\+0?\\.\\d+i$")) {
                        var tk = new StringTokenizer(token, "+");
                        var real = Double.parseDouble(tk.nextToken());
                        var img = Double.parseDouble(tk.nextToken().replace("i", ""));
                        num = new ComplexNumber(real, img);
                    }
                    else if (token.matches("^[+-]?\\d+\\.?\\d*-\\d+\\.?\\d*i$") || token.matches("^[+-]?0?\\.\\d+-\\d+\\.?\\d*i$") || token.matches("^[+-]?\\d+\\.?\\d*-0?\\.\\d+i$") || token.matches("^[+-]?0?\\.\\d+-0?\\.\\d+i$")) {
                        var tk = new StringTokenizer(token, "-");
                        var real = Double.parseDouble(tk.nextToken());
                        var img = Double.parseDouble(tk.nextToken().replace("i", ""))*-1;
                        num = new ComplexNumber(real, img);
                    }
                    else {
                        System.out.println("Invalid input");
                        success2 = false;
                        break;
                    }
                    matrix[i*m+j] = num;
                }
                if (!success2) {
                    success1 = false;
                    break;
                }
            }
            if (success1) return new ComplexMatrix(matrix, m, n);
        }
    }

    static void determinant() {
        var a = getMatrix("A");
        try {
            System.out.println("det(A) = %s".formatted(a.determinant()));
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
    }

    static void transpose() {
        var a = getMatrix("A");
        a.transpose();
        System.out.println("Transposed A:");
        System.out.println(a);
    }

    static void add() {
        var a = getMatrix("A");
        var b = getMatrix("B");
        try {
            var c = MatrixArithmetic.add(a, b);
            System.out.println("A+B=");
            System.out.println(c);
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
    }

    static void subtract() {
        var a = getMatrix("A");
        var b = getMatrix("B");
        try {
            var c = MatrixArithmetic.subtract(a, b);
            System.out.println("A-B=");
            System.out.println(c);
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
    }

    static void multiply() {
        var a = getMatrix("A");
        var b = getMatrix("B");
        try {
            var c = MatrixArithmetic.multiply(a, b);
            System.out.println("A*B=");
            System.out.println(c);
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
    }

    static void divide() {
        var a = getMatrix("A");
        var b = getMatrix("B");
        try {
            var c = MatrixArithmetic.divide(a, b);
            System.out.println("A/B=");
            System.out.println(c);
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
    }
}
