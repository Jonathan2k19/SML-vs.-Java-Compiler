(* ############################################################
        EXCEPTION AND DATATYPE DECLARATION
   ############################################################
*)

(*Exceptions*)
exception Error of string
exception UnboundId of string

(*Datatypes and type synonyms*)
datatype boolean = True | False
datatype token = LPAR | RPAR | NEG | AND | IMP | CONSTANT of boolean| ID of string
datatype exp = Neg of exp | And of exp*exp | Imp of exp*exp | Not of exp | Constant of boolean | Id of string
datatype ty = Bool
type id = string
type 'a environment = id -> 'a





(* ############################################################
        LEXER (string -> token list)
   ############################################################
*)

(*
Due to the fact that "∧" and "¬" are hard to type, let's define them as follows:
    "∧" =: "and"
    "¬" =: "not"
Therefore identifiers cannot be named "and" or "not"!
*)

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

(*CALL THIS PROCEDURE*)
fun lexing (s:string) = lex (explode s)





(* ############################################################
        PARSER (token list -> expression)
   ############################################################
*)

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


(*CALL THIS PROCEDURE*)
fun parse ts = case arrowExp ts of (e, nil) => e 
                                |  _ => raise Error "something could not be parsed"





(* ############################################################
        ELABORATION (expression * environment -> type)
   ############################################################
*)

(*
Environment procedures (optional)
EXAMPLE of how to construct a type environment:
    val env = updateEnvironment (updateEnvironment emptyEnvironment ("x") (Constant True)) "y" (Constant False)
    --> env "z" will raise UnboundId "z" exception
    --> env "x" will return Constant True
*)
fun emptyEnvironment x = raise UnboundId x
fun updateEnvironment environment x a y = if y = x then a else environment y

(*Elaboration: Checking if parsed input types are correct*)
fun elaboration env (Constant c) = (case c of True => Bool | False => Bool | _ => raise Error "unknown Constant")
  | elaboration env (And (e1, e2)) = (case (elaboration env e1, elaboration env e2) of (Bool, Bool) => Bool
                                                                                  | _ => raise Error "elaboration: And")
  | elaboration env (Imp (e1, e2)) = (case (elaboration env e1, elaboration env e2) of (Bool, Bool) => Bool 
                                                                                  | _ => raise Error "elaboration: Imp")
  | elaboration env (Neg e) = (case (elaboration env e) of Bool => Bool | _ => raise Error "elaboration: Neg")
  | elaboration env (Id e) = ((elaboration env (env e)) handle UnboundId x => raise Error ("elaboration: unbound identifier '"^x^"'"))
  | elaboration env _ = raise Error "elaboration: unknown pattern"





(* ############################################################
        EVALUATION (expression * environment -> value)
   ############################################################
*)

(*Evaluation: At this point I do not have to worry about type correctness (done in elaboration)*)
fun evaluation env (Constant c) = (case c of True => true | False => false | _ => raise Error "evaluation: unknown constant")
    | evaluation env (And (e1, e2)) = let val val1 = evaluation env e1
                                          val val2 = evaluation env e2
                                      in val1 andalso val2
                                      end
    | evaluation env (Imp (e1, e2)) = let val val1 = evaluation env e1
                                          val val2 = evaluation env e2
                                      in if val1 = false then true else val2
                                      end
    | evaluation env (Neg e) = not (evaluation env e)
    | evaluation env (Id e) = evaluation env (env e)
    | evaluation env _ = raise Error "evaluation: unknown pattern"





(* ############################################################
        EXAMPLE
   ############################################################
*)

val inputString = "x and ((false -> true) and (not true))"

val lexed = lexing inputString
(*--> lexed = [ID "x", AND, LPAR, LPAR, CONSTANT False, IMP, CONSTANT True, RPAR, AND, LPAR, NEG, CONSTANT True, RPAR, RPAR]: token list*)

val parsed = parse lexed
(*--> parsed = And (Id "x", And (Imp (Constant False, Constant True), Neg (Constant True))): exp*)

val exampleEnvironment = updateEnvironment (updateEnvironment emptyEnvironment ("x") (Constant True)) "y" (Constant False)
val elaborated = elaboration exampleEnvironment parsed
(*--> elaborated = Bool: ty*)

val evaluated = evaluation exampleEnvironment parsed
(*--> evaluated = false: bool*)