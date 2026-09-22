package Complex;

public class ComplexArithmetic {
    public static ComplexNumber add(ComplexNumber ... nums) {
        var sumReal = 0d;
        var sumImg = 0d;
        for (ComplexNumber num: nums) {
            sumReal += num.real();
            sumImg += num.img();
        }
        return new ComplexNumber(sumReal, sumImg);
    };

    public static ComplexNumber subtract(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.real() - b.real(), a.img() - b.img());
    }

    public static ComplexNumber multiply(ComplexNumber ... nums) {
        var prodReal = nums[0].real();
        var prodImg = nums[0].img();
        double newProdReal;
        for (int i = 1; i < nums.length; ++i) {
            newProdReal = prodReal*nums[i].real() - prodImg*nums[i].img();
            prodImg = prodReal*nums[i].img() + prodImg*nums[i].real();
            prodReal = newProdReal;
        }
        return new ComplexNumber(prodReal, prodImg);
    }

    public static ComplexNumber divide(ComplexNumber a, ComplexNumber b) {
        var absB = b.real()*b.real() + b.img()*b.img();
        return new ComplexNumber((a.real()*b.real() + a.img()*b.img()) / absB, (a.img()*b.real() - a.real()*b.img()) / absB);
    }
}
