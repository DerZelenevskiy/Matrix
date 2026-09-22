package Complex;

public record ComplexNumber(double real, double img) {
    public ComplexNumber(double real) {
        this(real, 0);
    }
}
