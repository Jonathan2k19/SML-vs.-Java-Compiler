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
datatype exp = Neg of exp | And of exp*exp | Imp of exp*exp | Not of exp | Constant of boolean | Id of string;

(*ts: token list, e: parsed stuff*)
fun arrowExp ts = (case (andExp ts) of (e, IMP :: tr) 
    => let val (e', tr') = arrowExp tr in (Imp (e, e'), tr') end
    | x => x)
and andExp ts = andExpHelp (notExp ts)
and andExpHelp (e, AND :: tr) = let val (e', tr') = notExp tr in andExpHelp (And (e,e'), tr') end
    | andExpHelp x = x
and  notExp (NEG :: tr) = let val (e, ts) = primitives tr in (Neg e, ts) end 
    | notExp ts = primitives ts
and primitives (CONSTANT (c) :: tr) = (case c of True => (Constant True, tr) | False => (Constant False, tr) | _ => raise Error "parser: unknown constant")
    | primitives (ID i :: tr) = (Id i, tr)
    | primitives (LPAR :: tr) = (case (arrowExp tr) of (e, RPAR :: tr') => (e, tr') | _ => raise Error "parser: missing right parenthesis")


(*
Throws an exception if not everything could be parsed, otherwise return parsed input
EXAMPLE: 
    val test = [ID "x", AND, LPAR, LPAR, CONSTANT False, IMP, CONSTANT True, RPAR, AND, LPAR, NEG, CONSTANT True, RPAR, RPAR]: token list
    parse test -> And (Id "x", And (Imp (Constant False, Constant True), Neg (Constant True))): exp
*)
fun parse ts = case arrowExp ts of (e, nil) => e | _ => raise Error "something could not be parsed"
