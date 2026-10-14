package Complex;

import java.text.DecimalFormat;

public record ComplexNumber(double real, double img) {
    private static final double epsilon = 1e-15;

    public ComplexNumber(double real, double img) {
        if (Math.abs(real) < epsilon) this.real = 0;
        else this.real = real;
        if (Math.abs(img) < epsilon) this.img = 0;
        else this.img = img;
    }

    public ComplexNumber(double real) {
        this(real, 0);
    }

    public ComplexNumber() {
        this(0, 0);
    }

    public String toString() {
        var df = new DecimalFormat("#.###");
        var s = new StringBuilder();
        if (this.real != 0) {
            s.append(df.format(this.real));
            if (this.img > 0) s.append("+");
        }
        if (this.img < 0) s.append("-");
        if (this.img != 0) {
            if (this.img == 1 || this.img == -1) s.append("i");
            else s.append(df.format(Math.abs(img))).append("i");
        }
        if (this.real == 0 && this.img == 0) s.append("0");
        return s.toString();
    }
}
