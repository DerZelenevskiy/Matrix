# Matrix Calculator

Calculates real/complex matrices using CLI

### Dimensions input format

- `m n`, `mxn`, `m,n` where `m` is the number of rows and `n` is the number of columns

- `m` for square matrices where m=n

### Matrix input format

Matrices are entered as sets of numbers separated by any number of spaces for each row

### Numbers input format

Numbers input must follow general mathematics rules for real/complex numbers and must not contain spaces

Calculator can process any real/complex numbers with both real and imaginary parts within Java `double` range

Examples: `i`, `-i`, `1`, `1+i`, `i-1`, `0.5+0.5i`

### Features

- Type `q` anytime to return to main menu
- Output of every number has a maximum of 3 digits after the floating point
- Due to Java internal arithmetic limitations, each number with absolute value less than 1e-15 is stored as 0 to prevent
inaccuracy of calculations