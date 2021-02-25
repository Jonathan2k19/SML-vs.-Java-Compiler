(*
Due to the fact that "∧" and "¬" are hard to type, let's define them as follows:
    "∧" =: "and"
    "¬" =: "not"
Therefore identifiers cannot be named "and" or "not"!
*)

exception Error of string
datatype boolean = True | False;
datatype token = LPAR | RPAR | NEG | AND | IMP | CONSTANT of boolean| ID of string;

(*cs/cr: character list*)
fun lex nil = nil
    | lex (#" " :: cr) = lex cr
    | lex (#"\t" :: cr) = lex cr
    | lex (#"\n" :: cr) = lex cr
    | lex (#"(" :: cr) = LPAR :: lex cr
    | lex (#")" :: cr) = RPAR :: lex cr
    | lex (#"-" :: #">" :: cr) = IMP :: lex cr
    | lex (#"t" :: #"r" :: #"u" :: #"e" :: cr) = CONSTANT (True) :: lex cr
    | lex (#"f" :: #"a" :: #"l" :: #"s" :: #"e" :: cr) = CONSTANT (False) :: lex cr
    | lex (#"a" :: #"n" :: #"d" :: cr) = AND :: lex cr
    | lex (#"n" :: #"o" :: #"t" :: cr) = NEG :: lex cr
    | lex (c :: cr) = if Char.isAlpha c then lexIdentifier [c] cr else raise Error "unknown character"
and lexIdentifier akku cs = if null cs orelse not(Char.isAlpha (hd cs)) then ID(implode(akku)) :: lex cs else lexIdentifier (akku @ [(hd cs)]) (tl cs)

(*CALL THIS FUNCTION*)
fun lexing (s:string) = lex (explode s)

(*
EXAMPLE:
    val test = "x and ((false -> true) and (not true))"
    lexing test -> [ID "x", AND, LPAR, LPAR, CONSTANT False, IMP, CONSTANT True, RPAR, AND, LPAR, NEG, CONSTANT True, RPAR, RPAR]: token list
*)