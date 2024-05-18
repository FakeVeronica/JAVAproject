Testing
Your submission will be tested (in part) by unpacking the .zip file, copying in the `test` package provided in the project template (overwriting any existing directory in your submission), moving into the `src` directory, and compiling and running the tests using commands equivalent to the following (example shows running in a Linux shell):
> cd src
src> javac **/*.java
src> java test.Test
Running the tests will output a summary of the test results, including lines summarizing the number of tests passed for each package, and an estimate of your mark for that section, as well as overall. There is no need for any of your code to print anything, and doing so may interfere with the test output, which may affect your mark.
Since the `test` package will be overwritten, anything you submit in the test package may be erased. There is no need to modify anything in the `test` package in the course of this project. If you do so, you are encouraged to make sure that your code still passes the unmodified tests.


src

├── itertools
│   ├── DoubleEndedIterator.java
│   ├── Itertools.java
│   └── RangeIterator.java


├── studentapi
│   ├── QueryTimedOutException.java
│   ├── Student.java
│   └── StudentList.java



├── studentstats
│   ├── ApiUnreachableException.java
│   ├── StudentListIterator.java
│   └── StudentStats.java


└── test/...


Each directory (itertools, studentapi, etc.) is a Java package of the same name. There are 9 programming tasks spread between itertools and studentstats. You should complete all tasks. You are advised to read all tasks before beginning implementation. You are advised but not required to implement tasks in the order they are given below.


Clarifications
2024-05-07: Added reference to which file to look in for each task.

2024-05-07: Added note about globstar support in bash.

2024-05-13: Added note encouraging students to read the provided code.

2024-05-13: Added note about no third-party dependencies, import from Java standard library only.

2024-05-15: Added note pointing out that the provided `problem.zip` is an example of valid, markable submission format, even if it would not pass any tests.
