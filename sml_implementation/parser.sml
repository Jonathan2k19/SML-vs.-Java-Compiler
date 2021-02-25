(*
adjusting grammar a bit:
    arrowExp    ::= andExp {"->" arrowExp}
    andExp      ::= notExp andExpHelp
    andExpHelp  ::= {'∧' notExp andExpHelp}
    notExp      ::= {'¬'} primitives
    primitives  ::= Id | "true" | "false" | "(" arrowExp ")"
*)

exception Error of string

datatype token = LPAR | RPAR | NEG | AND | IMP | TRUE | FALSE | ID of string
datatype exp = Neg of exp | And of exp*exp | Imp of exp*exp | Not of exp | True | False | Id of string

(*ts: token list, e: parsed stuff*)
fun arrowExp ts = (case (andExp ts) of (e, IMP :: tr) 
    => let val (e', tr') = arrowExp tr in (Imp (e, e'), tr') end
    | x => x)
and andExp ts = andExpHelp (notExp ts)
and andExpHelp (e, AND :: tr) = let val (e', tr') = notExp tr in andExpHelp (And (e,e'), tr') end
    | andExpHelp x = x
and  notExp (NEG :: tr) = let val (e, ts) = primitives tr in (Neg e, ts) end 
    | notExp ts = primitives ts
and primitives (TRUE :: tr) = (True, tr)
    | primitives (FALSE :: tr) = (False, tr)
    | primitives (ID i :: tr) = (Id i, tr)
    | primitives (LPAR :: tr) = (case (arrowExp tr) of (e, RPAR :: tr') => (e, tr') | _ => raise Error "missing right parenthesis")


(*
Throws an exception if not everything could be parsed, otherwise return parsed input
EXAMPLE: 
    val test = [ID "x", AND, LPAR, LPAR, FALSE, IMP, TRUE, RPAR, AND, LPAR, NEG, TRUE, RPAR, RPAR]: token list
    parse test -> And (Id "x", And (Imp (False, True), Neg True)): exp
*)
fun parse ts = case arrowExp ts of (e, nil) => e | _ => raise Error "something could not be parsed"
