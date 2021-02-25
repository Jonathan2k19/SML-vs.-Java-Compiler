# SML-vs.-Java-Compiler
***
## GOAL
The goal of this project is to learn a few things:
* How do I code a compiler in a functional programming language (SML) compared to an imperative language (Java)?
* How does Git/Github work?
* How do I write a good/not that ugly README.md file?

---
## GRAMMAR
This language supports logical expressions and 
1. **Operators:** '∧' (andExp), '->' (arrowExp), '¬' (notExp)
2. **Constants:** "true", "false"
3. **Identifiers:** string that can be assigned to costant value
4. **Grammar for recursive descent parsing:**
    * arrowExp    ::= andExp {"->" arrowExp}
    * andExp      ::= {andExp '∧'} notExp
    * notExp      ::= {'¬'} primitives
    * primitives  ::= Id | "true" | "false" | "(" arrowExp ")"


---
## WHAT I'VE LEARNED