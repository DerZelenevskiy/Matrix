package Matrix;

import Complex.ComplexArithmetic;
import Complex.ComplexNumber;

public class ComplexMatrix {
    private ComplexNumber[] matrix;
    private final int[] size;

    public ComplexMatrix(ComplexNumber[] nums, int m, int n) {
        if (n*m != nums.length) {
            throw new IllegalArgumentException("Provided array of numerals and size do not match");
        }
        this.size = new int[] {m, n};
        this.matrix = nums.clone();
    }

    public ComplexNumber get(int i, int j) {
        return this.matrix[i*size[1] + j];
    }

    public ComplexNumber get(int[] ptr) {
        return this.get(ptr[0], ptr[1]);
    }

    public ComplexNumber[] get() {
        return this.matrix.clone();
    }

    public int[] getSize() {
        return this.size.clone();
    }

    public void set(int i, int j, ComplexNumber val) {
        this.matrix[i*size[1] + j] = val;
    }

    public String toString() {
        if (this.matrix.length == 0) {
            return "()";
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.size[0]; ++i) {
            for (int j = 0; j < this.size[1]; ++j) {
                s.append(this.get(i, j).toString()).append(" ");
            }
            s.deleteCharAt(s.length()-1).append("%n".formatted());
        }
        s.deleteCharAt(s.length()-1);
        return s.toString();
    }

    public void transpose() {
        var transposed = new ComplexNumber[this.matrix.length];
        int[] ptr = {0, 0};
        for (int i = 0; i < this.matrix.length; ++i){
            transposed[i] = this.get(ptr);
            ++ptr[0];
            if (ptr[0] >= this.size[0]) {
                ptr[0] = 0;
                ++ptr[1];
            }
        }
        this.matrix = transposed;
        var s = this.size[0];
        this.size[0] = this.size[1];
        this.size[1] = s;
    }

    //LUP decomposition (P*A=L*U) where p=det(P)
    public ComplexNumber determinant() {
        if (this.size[0] != this.size[1]) {
            throw new ArithmeticException("Can not calculate the determinant of a non-square matrix");
        }
        var m = this.matrix.clone();
        var p = 1;
        for (int i = 0; i < this.size[0]; ++i) {
            //Seeking pivot row
            var pivotRow = i;
            var max = ComplexArithmetic.abs(m[i*size[0]+i]);
            for (int row = i + 1; row < this.size[0]; ++row) {
                if (ComplexArithmetic.abs(m[row*size[0]+i]) > max) {
                    max = ComplexArithmetic.abs(m[row*size[0]+i]);
                    pivotRow = row;
                }
            }
            //Singularity check
            if (ComplexArithmetic.abs(m[pivotRow*size[0]+i]) == 0) {
                return new ComplexNumber();
            }
            //Swapping pivot row with i-row
            else if (pivotRow != i) {
                for (int el = 0; el < this.size[0]; ++el) {
                    var val = m[i*size[0]+el];
                    m[i*size[0]+el] = m[pivotRow*size[0]+el];
                    m[pivotRow*size[0]+el] = val;
                }
                p *= -1;
            }
            //Default LUP returns M=L+U-E, using Gaussian elimination to make M=U
            for (int row = i + 1; row < this.size[0]; ++row) {
                var f = ComplexArithmetic.divide(m[row*this.size[0]+i], m[i*this.size[0]+i]);
                m[row*this.size[0]+i] = f;
                for (int el = i + 1; el < this.size[0]; ++el) {
                    m[row*this.size[0]+el] = ComplexArithmetic.subtract(m[row*this.size[0]+el], ComplexArithmetic.multiply(f, m[i*this.size[0]+el]));
                }
            }
        }
        //Calculating the determinant as the product of the main diagonal
        var det = new ComplexNumber(1);
        for (int i = 0; i < this.size[0]; ++i) {
            det = ComplexArithmetic.multiply(det, m[i*this.size[0]+i]);
        }
        //Considering the resulting determinant being det(P*A)
        return ComplexArithmetic.multiply(det, new ComplexNumber(p));
    }
}
