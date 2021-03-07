# SML-vs.-Java-Compiler

## GOAL
The goal of this project is to learn a few things:
* How do compilers work in general?
* How do I code a compiler in a functional programming language (Standard ML) compared to an imperative language (Java)?
* How can I add github support for VS_Code and Eclipse?
* How do I write a clear README.md file?


## GRAMMAR
This language can be used to create logical expressions
1. **Operators:** '∧' (andExp), '->' (arrowExp), '¬' (notExp)
2. **Constants:** "true", "false"
3. **Identifiers:** string that can be assigned to constant
4. **Syntax categories for recursive descent parsing:**
    * arrowExp"\t"    ::= andExp {"->" arrowExp}
    * andExp      ::= {andExp '∧'} notExp
    * notExp      ::= {'¬'} primitives
    * primitives  ::= Id | "true" | "false" | "(" arrowExp ")"


## WHAT I'VE LEARNED
1. **SML_IMPLEMENTATION:**
    * Pattern matching in SML is super useful in recursive procedures, especially for the Lexer
    * I have not figured out yet, how to combine code of several files in one file without copying the files (which leads to large file size)
2. **JAVA_IMPLEMENTATION:**
    * It is incredibly more difficult to match patterns in Java than it is in SML
    * First time using regular expression -> [Regex Cheatsheet](https://medium.com/factory-mind/regex-tutorial-a-simple-cheatsheet-by-examples-649dc1c3f285)
    * _TODO: fix lexer (something wrong with lexing left parenthesis) and improve look of expressions (`toString()`)_