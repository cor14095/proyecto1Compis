grammar Expr;
//KeyWords
CLASS : 'class';
STRUCT : 'struct';
TRUE : 'true';
FALSE : 'false';
INT : 'int';
IF : 'if';
ELSE : 'else';
WHILE : 'while';
RETURN : 'return';
CHAR : 'char';
BOOLEAN : 'boolean';
VOID : 'void';
SCAN : 'scan';
PRINT : 'print';

//Characters
fragment LETTER: ('a'..'z' | 'A'..'Z');
//\f\s las quite de WS
WS: [ \t\r\n]+ ->	channel(HIDDEN);//Whitespace declaration	
fragment DIGIT: ('0'..'9');
CHR : '\''(LETTER|DIGIT|' '|EXC|'"'|'#'|'$'|'%'|'^'|'&'|'*'|LPARENT|RPARENT|PLUS|'_'|MINUS|'?'|'\''|'.'|','|'<'|'>'|':'|';'|'`'|'~'|'@'|'\"') '\''; 
ID: LETTER(DIGIT|LETTER)*;
NUM: (DIGIT)+;
STRING: '\"'(LETTER|DIGIT|' '|EXC|'"'|'#'|'$'|'%'|'^'|'&'|'*'|LPARENT|RPARENT|PLUS|'_'|MINUS|'?'|'\''|'.'|','|'<'|'>'|':'|';'|'`'|'~'|'@'|'\"'|[\\s])+'\"';
COMMENT: '//'(~('\r'|'\n'))*{skip();};
COMA: ','; 
AND: '&&';
OR:'||';
LBRACE: '{';
RBRACE: '}';
DOTCOMMA: ';';
RCORCH: ']';
LCORCH: '[';
LPARENT: '(';
RPARENT: ')';
EQ: '=';
DOT: '.';
PLUS: '+';
MINUS: '-';
EXC: '!';
AST: '*';
SLSH: '/';
PRCNT: '%';
MTHAN: '>';
LTHAN: '<';
EQMTHAN: '>=';
EQLTHAN: '<=';
EQEQ: '==';
NOTEQ: '!=';

program: CLASS ID LBRACE (declaration)* RBRACE; 

declaration: structDeclaration | varDeclaration | methodDeclaration;
			
varDeclaration: varType ID DOTCOMMA | varType ID LCORCH NUM RCORCH DOTCOMMA ;

structDeclaration: STRUCT ID LBRACE (varDeclaration)* RBRACE;//Ambito +1

varType: (INT | CHAR | BOOLEAN | (STRUCT ID) | structDeclaration | VOID);
		
methodDeclaration: methodType ID LPARENT (parameter(COMA parameter)*)? RPARENT block;

methodType: INT | CHAR | BOOLEAN | VOID;
			
parameter: parameterType ID | parameterType ID LCORCH RCORCH;
		 
parameterType: INT | CHAR | BOOLEAN ;
			
block: LBRACE (varDeclaration)* (statement)* RBRACE ;


statement: myIf | returnBlock | whileBlock | methodCall DOTCOMMA | assignation | expression DOTCOMMA | print;

assignation: location EQ (expression | scan ) DOTCOMMA ;
whileBlock:  WHILE LPARENT expression RPARENT block ;
returnBlock: RETURN (nExpression) DOTCOMMA ;

//scan y print 
print: PRINT LPARENT ( STRING | location ) RPARENT DOTCOMMA;
scan: SCAN LPARENT RPARENT;

myIf: IF LPARENT expression RPARENT block(ELSE block)?;
location: declaredVariable | dotLocation;
dotLocation: variable ( DOT location) | arrayVariable ( DOT location);
declaredVariable: variable | arrayVariable;
variable: ID;
arrayVariable: ID LCORCH expression RCORCH ;
expressionInP: LPARENT expression RPARENT ;

//jerarquia de operaciones
nExpression: expression | ;
expression: andExpression | expression OR andExpression;
andExpression: equalsExpression | andExpression AND equalsExpression;
equalsExpression: relationExpression | equalsExpression eq_op relationExpression;
relationExpression: addSubsExpression | relationExpression rel_op addSubsExpression;
addSubsExpression: mulDivExpression | addSubsExpression as_op mulDivExpression;
mulDivExpression: prExpression | mulDivExpression md_op prExpression;
prExpression: basicExpression | prExpression pr_op basicExpression;
basicExpression: LPARENT (INT|CHAR) RPARENT basic | MINUS basic | EXC basic | basic;
basic : expressionInP | location | methodCall | literal;
arg: expression;
methodCall: ID LPARENT (arg(COMA arg)*)? RPARENT;

//operadores
as_op : PLUS | MINUS;
md_op: AST | SLSH ;
pr_op: PRCNT;
rel_op: LTHAN | MTHAN | EQLTHAN | EQMTHAN ;
eq_op: EQEQ | NOTEQ;
cond_op: AND | OR;

literal: int_literal | char_literal | bool_literal;

int_literal: NUM;

char_literal: CHR;

bool_literal: TRUE | FALSE;