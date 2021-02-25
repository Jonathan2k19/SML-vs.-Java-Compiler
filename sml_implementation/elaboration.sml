(*
THIS CODE CHECKS IF THE TYPES ARE CORRECT
    See the file "typeRules.png" for reference
*)

(*Datatypes and type synonyms*)
type id = string
type 'a environment = id -> 'a
(*TODO: remove exp (just added it because of generateEnvironment procedure)*)
datatype boolean = True | False
datatype exp = Neg of exp | And of exp*exp | Imp of exp*exp | Not of exp | Constant of boolean | Id of string

datatype operator = Neg | And | Imp
datatype ty = Bool  (*type (but "type" is keyword)*)
datatype expression = 
        Constant of boolean
    |   Id of id
    |   BinaryOperator of operator * expression * expression    (*e. g.: BinaryOperator(And, Id "x", False)*)
    |   UnaryOperator of operator * expression                  (*e. g.: UnaryOperator(Neg (Constant True))*)

(*Exceptions*)
exception UnboundId of id
exception Error of string

(*
Environment procedures
EXAMPLE of how to construct a type environment:
    val env = updateEnvironment (updateEnvironment emptyEnvironment ("x") Bool) "y" Bool
    --> env "z" will raise UnboundId "z" exception
    --> env "x" will return Bool
*)
fun emptyEnvironment x = raise UnboundId x
fun updateEnvironment environment x a y = if y = x then a else environment y
(*TODO: fun generateEnvironment (parserInput: exp list) = *)


(*example of input: And (Id "x", And (Imp (Constant False, Constant True), Neg (Constant True)))*)

(*Elaboration: Checking if input (from parser.sml) types are correct*)
fun elabConstant True = Bool
    | elabConstant False = Bool
    | elabConstant _ = raise Error "elaboration: unknown constant"

fun elabOperatorBinary And Bool Bool = Bool
    | elabOperatorBinary Imp Bool Bool = Bool
    | elabOperatorBinary _ _ _ = raise Error "elaboration: unknown binary operator or wrong operator type"

fun elabOperatorUnary Neg Bool = Bool
    | elabOperatorUnary _ _ = raise Error "elaboration: unknown unary operator or wrong operator type"

fun elaboration env (Constant c) = elabConstant c
    | elaboration env (BinaryOperator (oper, e1, e2)) = elabOperatorBinary oper (elaboration env e1) (elaboration env e2)
    | elaboration env (UnaryOperator (oper, e)) = elabOperatorUnary oper (elaboration env e)
