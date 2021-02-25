(*
IN THE EVALUATION STEP, THE PARSED INPUT GETS EVALUATED, WHICH MEANS IT GETS ASSIGNED TO A VALUE
    See the file "evaluationRules.png" for reference (a bit different to implementation)
*)


(*Datatypes and type synonyms*)
type id = string
type 'a environment = id -> 'a
datatype boolean = True | False
datatype exp = Neg of exp | And of exp*exp | Imp of exp*exp | Not of exp | Constant of boolean | Id of string

(*Exceptions*)
exception Error of string
exception UnboundId of id

(*Environment procedures*)
fun emptyEnvironment x = raise UnboundId x
fun updateEnvironment environment x a y = if y = x then a else environment y

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


(*
EXAMPLE:
1) empty environment
    val test = (And (Id "x", And (Imp (Constant False, Constant True), Neg (Constant True))))
    evaluation emptyEnvironment test --> Uncaught SML exception: UnboundId "x"
2) not empty environment
    val test = (And (Id "x", And (Imp (Constant False, Constant True), Neg (Constant True))))
    val env = updateEnvironment emptyEnvironment ("x") (Constant True)
    evaluation env test --> false: bool
*)