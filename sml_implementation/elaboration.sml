(*
THIS CODE CHECKS IF THE TYPES ARE CORRECT
    See the file "typeRules.png" for reference
*)

(*Datatypes and type synonyms*)
type id = string
type 'a environment = id -> 'a
datatype boolean = True | False
datatype ty = Bool  (*There is only one type in this language*)
datatype exp = Neg of exp | And of exp*exp | Imp of exp*exp | Not of exp | Constant of boolean | Id of string


(*Exceptions*)
exception UnboundId of id
exception Error of string

(*
Environment procedures (optional)
EXAMPLE of how to construct a type environment:
    val env = updateEnvironment (updateEnvironment emptyEnvironment ("x") Bool) "y" Bool
    --> env "z" will raise UnboundId "z" exception
    --> env "x" will return Bool
*)

fun emptyEnvironment x = raise UnboundId x
fun updateEnvironment environment x a y = if y = x then a else environment y

(*Elaboration: Checking if input (from parser.sml) types are correct*)
fun elaboration env (Constant c) = (case c of True => Bool | False => Bool | _ => raise Error "unknown Constant")
  | elaboration env (And (e1, e2)) = (case (elaboration env e1, elaboration env e2) of (Bool, Bool) => Bool
                                                                                  | _ => raise Error "elaboration: And")
  | elaboration env (Imp (e1, e2)) = (case (elaboration env e1, elaboration env e2) of (Bool, Bool) => Bool 
                                                                                  | _ => raise Error "elaboration: Imp")
  | elaboration env (Neg e) = (case (elaboration env e) of Bool => Bool | _ => raise Error "elaboration: Neg")
  | elaboration env (Id e) = (env e handle UnboundId x =>raise Error ("elaboration: unbound identifier '"^x^"'"))
  | elaboration env _ = raise Error "elaboration: unknown pattern"

(*
EXAMPLE:
1) Empty environment 
    val test = And (Id "x", And (Imp (Constant False, Constant True), Neg (Constant True)))
    elaboration emptyEnvironment test --> Uncaught SML exception: Error "elaboration: unbound identifier 'x'"

2) Not empty environment
    val test = And (Id "x", And (Imp (Constant False, Constant True), Neg (Constant True)))
    val env = updateEnvironment (updateEnvironment emptyEnvironment ("x") Bool) "y" Bool
    elaboration test --> Bool: ty
*)
