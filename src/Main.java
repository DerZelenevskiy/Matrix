import complex.ComplexNumber;
import matrix.ComplexMatrix;
import matrix.MatrixArithmetic;

import java.text.ParseException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Main {
    static final Scanner sc = new Scanner(System.in);
    static ComplexMatrix prev = null;

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

    static ComplexMatrix getMatrix(String label, ComplexMatrix prev) {
        //Checking the availability of the previous res
        System.out.println("Enter matrix %s:".formatted(label));
        if (prev != null) {
            System.out.print("Use the previous result? (y/n) ");
            var input = sc.nextLine();
            if (input.equals("y")) return prev;
            //RuntimeException in this method is used as quit marker
            else if (input.equals("q")) throw new RuntimeException();
        }
        int n, m;
        while (true) {
            try {
                var dim = parseDim(label);
                m = dim[0];
                n = dim[1];
                break;
            }
            catch (RuntimeException exc) {
                throw new RuntimeException();
            }
            catch (ParseException exc) {
                System.out.println("Invalid dimensions input");
            }
        }
        ComplexMatrix matrix;
        while (true) {
            try {
                matrix = parseMatrix(label, m, n);
                break;
            }
            catch (RuntimeException exc) {
                throw new RuntimeException();
            }
            catch (ParseException exc) {
                System.out.println("Invalid input");
            }
        }
        return matrix;
    }

    private static int[] parseDim(String label) throws ParseException {
        int n, m;
        System.out.print("dim(%s)=".formatted(label));
        var dim = sc.nextLine().replace("x", " ").replace(",", " ");
        var tk = new StringTokenizer(dim, " ");
        if (tk.countTokens() == 2) {
            try {
                m = Integer.parseInt(tk.nextToken());
                n = Integer.parseInt(tk.nextToken());
                if (n > 0 && m > 0) return new int[] {m, n};
            }
            catch (Exception _) {}
        }
        else if (tk.countTokens() == 1) {
            var token = tk.nextToken();
            if (token.equals("q")) throw new RuntimeException();
            try {
                m = Integer.parseInt(token);
                n = m;
                if (n > 0) return new int[] {m, n};
            }
            catch (Exception _) {}
        }
        throw new ParseException(dim, 0);
    }

    private static ComplexMatrix parseMatrix(String label, int m, int n) throws ParseException {
        System.out.println("%s=".formatted(label));
        var matrix = new ComplexNumber[n*m];
        for (int i = 0; i < m; ++i) {
            var line = sc.nextLine();
            var row = new StringTokenizer(line, " ");
            if (line.equals("q")) throw new RuntimeException();
            else if (row.countTokens() != n) {
                throw new ParseException(line, 0);
            }
            for (int j = 0; j < n; ++j) {
                ComplexNumber num;
                var token = row.nextToken();
                try {
                    num = parseComplexNumber(token);
                }
                catch (ParseException exc) {
                    throw new ParseException(token, 0);
                }
                matrix[i*n+j] = num;
            }
        }
        return new ComplexMatrix(matrix, m, n);
    }

    private static ComplexNumber parseComplexNumber(String token) throws ParseException {
        //Checks match for img part first then for real part in remaining string
        var realPattern = Pattern.compile("[+-]?\\d*\\.\\d+|[+-]?\\d+");
        var imgPattern = Pattern.compile("[+-]?\\d*\\.\\d+i|[+-]?\\d+i|[+-]?i");
        var imgMatcher = imgPattern.matcher(token);
        String realPart = "", imgPart = "", substring = token;
        if (imgMatcher.find()) {
            imgPart = imgMatcher.group();
            substring = token.substring(0, imgMatcher.start()) + token.substring(imgMatcher.end());
        }
        var realMatcher = realPattern.matcher(substring);
        if (realMatcher.find()) {
            realPart = realMatcher.group();
        }
        //Validates if two parts concatenate into token with [+-] between them
        boolean validator = false;
        if (!realPart.isEmpty() && !imgPart.isEmpty()) {
            validator = (token.equals(realPart+imgPart) && (imgPart.startsWith("+") || imgPart.startsWith("-"))) ||
                    (token.equals(imgPart+realPart) && (realPart.startsWith("+") || realPart.startsWith("-")));
        }
        else if (!realPart.isEmpty()) {
            validator = token.equals(realPart);
        }
        else if (!imgPart.isEmpty()) {
            validator = token.equals(imgPart);
        }
        if (!validator) {
            throw new ParseException(token, 0);
        }
        //Parsing real and img parts separately
        double real = 0, img = 0;
        if (!realPart.isEmpty()) {
            real = Double.parseDouble(realPart);
        }
        if (!imgPart.isEmpty()) {
            imgPart = imgPart.replace("i", "");
            if (imgPart.equals("+") || imgPart.isEmpty()) {
                img = 1;
            }
            else if (imgPart.equals("-")) {
                img = -1;
            }
            else {
                img = Double.parseDouble(imgPart);
            }
        }
        return new ComplexNumber(real, img);
    }

    static void determinant() {
        ComplexMatrix a;
        try {a = getMatrix("A", prev);}
        catch (RuntimeException exc) {System.out.println(); return;}
        try {
            System.out.println("det(A)=%s".formatted(a.determinant()));
            prev = null;
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
        System.out.println();
    }

    static void transpose() {
        ComplexMatrix a;
        try {a = getMatrix("A", prev);}
        catch (RuntimeException exc) {System.out.println(); return;}
        a.transpose();
        System.out.println("Transposed A:");
        System.out.println(a);
        System.out.println();
        //Saving the result for further calculations
        prev = a;
    }

    static void add() {
        ComplexMatrix a, b;
        try {a = getMatrix("A", prev); b = getMatrix("B", prev);}
        catch (RuntimeException exc) {System.out.println(); return;}
        try {
            var c = MatrixArithmetic.add(a, b);
            System.out.println("A+B=");
            System.out.println(c);
            prev = c;
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
        System.out.println();
    }

    static void subtract() {
        ComplexMatrix a, b;
        try {a = getMatrix("A", prev); b = getMatrix("B", prev);}
        catch (RuntimeException exc) {System.out.println(); return;}
        try {
            var c = MatrixArithmetic.subtract(a, b);
            System.out.println("A-B=");
            System.out.println(c);
            prev = c;
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
        System.out.println();
    }

    static void multiply() {
        ComplexMatrix a, b;
        try {a = getMatrix("A", prev); b = getMatrix("B", prev);}
        catch (RuntimeException exc) {System.out.println(); return;}
        try {
            var c = MatrixArithmetic.multiply(a, b);
            System.out.println("A*B=");
            System.out.println(c);
            prev = c;
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
        System.out.println();
    }

    static void divide() {
        ComplexMatrix a, b;
        try {a = getMatrix("A", prev); b = getMatrix("B", prev);}
        catch (RuntimeException exc) {System.out.println(); return;}
        try {
            var c = MatrixArithmetic.divide(a, b);
            System.out.println("A/B=");
            System.out.println(c);
            prev = c;
        }
        catch (ArithmeticException exc) {
            System.out.println(exc.getMessage());
        }
        System.out.println();
    }
}
