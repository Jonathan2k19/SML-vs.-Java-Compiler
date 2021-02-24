(*
Due to the fact that "∧" and "¬" are hard to type, let's define them as follows:
    "∧" =: "A"
    "¬" =: "N"
*)

exception Error of string

datatype token = LPAR | RPAR | NEG | AND | IMP | TRUE | FALSE

(*cs/cr: character list*)
fun lex nil = nil
    | lex (#" " :: cr) = lex cr
    | lex (#"\t" :: cr) = lex cr
    | lex (#"\n" :: cr) = lex cr
    | lex (#"(" :: cr) = LPAR :: lex cr
    | lex (#")" :: cr) = RPAR :: lex cr
    | lex (#"-" :: #">" :: cr) = IMP :: lex cr
    | lex (#"t" :: #"r" :: #"u" :: #"e" :: cr) = TRUE :: lex cr
    | lex (#"f" :: #"a" :: #"l" :: #"s" :: #"e" :: cr) = FALSE :: lex cr
    | lex (c :: cr) = if c = #"A" then AND :: lex cr else if c = #"N" then NEG :: lex cr else raise Error "unknown character"

(*CALL THIS FUNCTION*)
fun lexing (s:string) = lex (explode s)

(*
EXAMPLE:
    val test = "false A (N true)"
    lexing test -> [FALSE, AND, LPAR, NEG, TRUE, RPAR]
*)