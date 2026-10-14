# Matrix Calculator

Calculates real/complex matrices using CLI

### Dimensions input format

- `m n`, `mxn` where `m` is the number of rows and `n` is the number of columns

- `m` for square matrices where m=n

### Numbers input format

Numbers input must follow general mathematics rules for real/complex numbers

Calculator can process any real/complex numbers with both real and imaginary parts within Java `double` range

Examples: `i`, `-i`, `1`, `1+i`, `1-i`, `0.5+0.5i`

### Features

- You can type `q` almost anytime to return to main menu
- Output of every number has a maximum of 3 digits after the floating point
- Due to Java internal arithmetic limitations, each number with absolute value less than 1e-15 is stored as 0 to prevent
inaccuracy of calculations