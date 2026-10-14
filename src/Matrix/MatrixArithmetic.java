package Matrix;

import Complex.ComplexArithmetic;
import Complex.ComplexNumber;

public class MatrixArithmetic {
    public static ComplexMatrix add(ComplexMatrix ... matrices) {
        if (matrices.length == 0) {
            return new ComplexMatrix(new ComplexNumber[0], 0, 0);
        }
        var res = new ComplexMatrix(matrices[0].get(), matrices[0].getSize()[0], matrices[0].getSize()[1]);
        for (int m = 1; m < matrices.length; ++m) {
            if (matrices[m].getSize()[0] != matrices[0].getSize()[0] || matrices[m].getSize()[1] != matrices[0].getSize()[1]) {
                throw new ArithmeticException("Can not add matrices with different dimensions");
            }
            for (int i = 0; i < matrices[m].getSize()[0]; ++i) {
                for (int j = 0; j < matrices[m].getSize()[1]; ++j) {
                    res.set(i, j, ComplexArithmetic.add(res.get(i, j), matrices[m].get(i, j)));
                }
            }
        }
        return res;
    }

    public static ComplexMatrix subtract(ComplexMatrix a, ComplexMatrix b) {
        var res = new ComplexMatrix(a.get(), a.getSize()[0], a.getSize()[1]);
        if (a.getSize()[0] != b.getSize()[0] || a.getSize()[1] != b.getSize()[1]) {
            throw new ArithmeticException("Can not subtract matrices with different dimensions");
        }
        for (int i = 0; i < a.getSize()[0]; ++i) {
            for (int j = 0; j < a.getSize()[1]; ++j) {
                res.set(i, j, ComplexArithmetic.subtract(res.get(i, j), b.get(i, j)));
            }
        }
        return res;
    }

    public static ComplexMatrix multiply(ComplexMatrix ... matrices) {
        if (matrices.length == 0) {
            return new ComplexMatrix(new ComplexNumber[0], 0, 0);
        }
        var res = new ComplexMatrix(matrices[0].get(), matrices[0].getSize()[0], matrices[0].getSize()[1]);
        for (int m = 1; m < matrices.length; ++m) {
            if (res.getSize()[1] != matrices[m].getSize()[0]) {
                throw new ArithmeticException("Can not multiply matrices with mismatching dimensions");
            }
            var nextRes = new ComplexMatrix(new ComplexNumber[res.getSize()[0]*matrices[m].getSize()[1]], res.getSize()[0], matrices[m].getSize()[1]);
            for (int i = 0; i < nextRes.getSize()[0]; ++i) {
                for (int j = 0; j < nextRes.getSize()[1]; ++j) {
                    var sum = new ComplexNumber();
                    for (int k = 0; k < res.getSize()[1]; ++k) {
                        sum = ComplexArithmetic.add(sum, ComplexArithmetic.multiply(res.get(i, k), matrices[m].get(k, j)));
                    }
                    nextRes.set(i, j, sum);
                }
            }
            res = nextRes;
        }
        return res;
    }

    //Calculating the inverse matrix via LUP decomposition
    public static ComplexMatrix divide(ComplexMatrix a, ComplexMatrix b) {
        if (b.getSize()[0] != b.getSize()[1]) {
            throw new ArithmeticException("Can not divide by a non-square matrix");
        }
        else if (a.getSize()[1] != b.getSize()[0]) {
            throw new ArithmeticException("Can not divide matrices with mismatching dimensions");
        }
        var lu = new ComplexMatrix(b.get(), b.getSize()[0], b.getSize()[1]);
        var p = new int[b.getSize()[0]];
        for (int i = 0; i < b.getSize()[0]; ++i) {
            p[i] = i;
        }
        for (int i = 0; i < b.getSize()[0]; ++i) {
            //Seeking pivot row
            var pivotRow = i;
            var max = ComplexArithmetic.abs(lu.get(i, i));
            for (int row = i + 1; row < b.getSize()[0]; ++row) {
                if (ComplexArithmetic.abs(lu.get(row, i)) > max) {
                    max = ComplexArithmetic.abs(lu.get(row, i));
                    pivotRow = row;
                }
            }
            //Singularity check
            if (ComplexArithmetic.abs(lu.get(pivotRow, i)) == 0) {
                throw new ArithmeticException("Can not divide by a singular matrix");
            }
            //Swapping pivot row with i-row in lu and in p
            else if (pivotRow != i) {
                for (int el = 0; el < b.getSize()[0]; ++el) {
                    var val = lu.get(i, el);
                    lu.set(i, el, lu.get(pivotRow, el));
                    lu.set(pivotRow, el, val);
                }
                var val = p[i];
                p[i] = p[pivotRow];
                p[pivotRow] = val;
            }
            //Gaussian elimination
            for (int row = i + 1; row < b.getSize()[0]; ++row) {
                var f = ComplexArithmetic.divide(lu.get(row, i), lu.get(i, i));
                lu.set(row, i, f);
                for (int el = i + 1; el < b.getSize()[0]; ++el) {
                    lu.set(row, el, ComplexArithmetic.subtract(lu.get(row, el), ComplexArithmetic.multiply(f, lu.get(i, el))));
                }
            }
        }
        //Resolving the equations for each column
        var bInv = new ComplexMatrix(new ComplexNumber[b.getSize()[0]*b.getSize()[0]], b.getSize()[0], b.getSize()[0]);
        for (int k = 0; k < b.getSize()[0]; ++k) {
            var y = new ComplexNumber[b.getSize()[0]];
            for (int i = 0; i < b.getSize()[0]; ++i) {
                if (p[i] == k) y[i] = new ComplexNumber(1);
                else y[i] = new ComplexNumber();
            }
            //Resolving for L
            for (int i = 0; i < b.getSize()[0]; ++i) {
                for (int j = 0; j < i; ++j) {
                    y[i] = ComplexArithmetic.subtract(y[i], ComplexArithmetic.multiply(lu.get(i, j), y[j]));
                }
            }
            //Resolving for U
            for (int i = b.getSize()[0]-1; i >= 0; --i) {
                for (int j = i + 1; j < b.getSize()[0]; ++j) {
                    y[i] = ComplexArithmetic.subtract(y[i], ComplexArithmetic.multiply(lu.get(i, j), bInv.get(j, k)));
                }
                bInv.set(i, k, ComplexArithmetic.divide(y[i], lu.get(i, i)));
            }
        }
        return multiply(a, bInv);
    }
}
