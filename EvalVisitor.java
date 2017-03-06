import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.io.*;
 
public class EvalVisitor extends ExprBaseVisitor<String> {

	private int scopeCounter = 0;

	private static String newline = System.getProperty("line.separator");
	private String errorMsg = "Syntax error list:";
	Path file = Paths.get("ErrorLog_Grammar.log");

	// HashMap format:
	// - String: Key, generated with NodeName + scopeCounter.
	// - String[]: Data, generated with:
	// - - 0: ID of Node.
	// - - 1: Type of Node.
	// - - 2: Scope of Node.
    public Map<String,String[]> SymbolTable = new HashMap<String,String[]>();

    // Method to write file.
    private void writeErrors(String msgs, Path file) {
    	try {
        	//Files.deleteIfExists(file);
        	Files.write(file, Arrays.asList(msgs), Charset.forName("UTF-8"));
        } catch (IOException e) {
        	System.err.println("Something is wrong.");
        }
    }

	@Override 
	public String visitProgram(ExprParser.ProgramContext ctx) {

				System.out.println("I visited: PROGRAM");

				String errorMsg = "Syntax error list:";
   				file = Paths.get("ErrorLog_Grammar.log");
   				System.out.println("Visiting!");

				String[] data =  {ctx.getChild(1).getText(), "", String.valueOf(scopeCounter)};
				//System.out.println(Arrays.toString(data));

				SymbolTable.put(ctx.getChild(1).getText()+scopeCounter, data);
				scopeCounter += 1;

				return visitChildren(ctx); 
	}
	@Override 
	public String visitDeclaration(ExprParser.DeclarationContext ctx) { 

				//System.out.println("I visited: Declaration");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitVarDeclaration(ExprParser.VarDeclarationContext ctx) { 

				scopeCounter -= 1;

				String[] data = new String[3];

				System.out.println("I visited: VarDeclaration of " + ctx.getChild(1).getText());

				// We have 2 cases here, 1) normal declaration, 2) Array declaration.
				// For the first case it's simple
				if (ctx.getText().indexOf('[') == -1) {
					// If there's not a '[', then it's normal.
					data[0] = ctx.getChild(1).getText();
					data[1] = ctx.getChild(0).getText();
					data[2] = String.valueOf(scopeCounter);

					//System.out.println(Arrays.toString(data));
				} else {
					// If it has one then we need to do a little tweek.
					data[0] = ctx.getChild(1).getText();
					data[1] = "_Array" + ctx.getChild(0).getText() + "," + ctx.getChild(3); // Trick here is that in the table we look for "_Array" and we have all Arrays and after a split we have the size.
					data[2] = String.valueOf(scopeCounter);

					//System.out.println(Arrays.toString(data));
				}

				System.out.println("Key is: " + ctx.getChild(1).getText()+scopeCounter);
				SymbolTable.put(ctx.getChild(1).getText()+scopeCounter, data);
				scopeCounter += 1;
				return visitChildren(ctx); 
	}
	@Override 
	public String visitStructDeclaration(ExprParser.StructDeclarationContext ctx) { 

				System.out.println("I visited: StructDeclaration of " + ctx.getChild(1).getText());

				String[] data =  {ctx.getChild(1).getText(), 
					ctx.getChild(0).getText() + "_" + ctx.getChild(1).getText(), 
					String.valueOf(scopeCounter)};
				//System.out.println(Arrays.toString(data));

				SymbolTable.put(ctx.getChild(1).getText(), data);
				scopeCounter += 1;

				return visitChildren(ctx); 
	}
	@Override 
	public String visitVarType(ExprParser.VarTypeContext ctx) { 
				//System.out.println("I visited: varType");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitMethodDeclaration(ExprParser.MethodDeclarationContext ctx) { 

				String[] data = new String[3];
				String params = "";
				int paramCounter = 3;			// Start the paramDeclaration at 3rd child.

				System.out.println("I visited: MethodDeclaration");

				// Make Method values.
				data[0] = ctx.getChild(1).getText();
				data[1] = ctx.getChild(0).getText();
				data[2] = String.valueOf(scopeCounter);
				//System.out.println(Arrays.toString(data));

				// Add them to the Hash Map.
				SymbolTable.put(data[0] + scopeCounter, data);

				scopeCounter += 1;

				return visitChildren(ctx); 
	}
	@Override 
	public String visitMethodType(ExprParser.MethodTypeContext ctx) { 
				//System.out.println("I visited: MethodType");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitParameterDeclaration(ExprParser.ParameterDeclarationContext ctx) { 

				scopeCounter -= 1;

				String[] data = new String[3];

				System.out.println("I visited: ParameterDeclaration For: " + ctx.getParent().getChild(1).getText());

				// We have 2 cases here, 1) normal declaration, 2) Array declaration.
				// For the first case it's simple
				if (ctx.getText().indexOf('[') == -1) {
					// If there's not a '[', then it's normal.
					data[0] = ctx.getChild(1).getText();
					data[1] = ctx.getChild(0).getText();
					data[2] = String.valueOf(scopeCounter);

					//System.out.println(Arrays.toString(data));
				} else {
					// If it has one then we need to do a little tweek.
					data[0] = ctx.getChild(1).getText();
					data[1] = "_Array" + ctx.getChild(0).getText() + "," + ctx.getChild(3); // Trick here is that in the table we look for "_Array" and we have all Arrays and after a split we have the size.
					data[2] = String.valueOf(scopeCounter);

					//System.out.println(Arrays.toString(data));
				}

				SymbolTable.put(ctx.getChild(1).getText()+scopeCounter, data);
				scopeCounter += 1;
				return visitChildren(ctx); 
	}
	@Override 
	public String visitParameterType(ExprParser.ParameterTypeContext ctx) { 
				//System.out.println("I visited: ParameterType");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitBlock(ExprParser.BlockContext ctx) { 
				//System.out.println("I visited: Block For: " + ctx.getParent().getChild(0).getText() + " " + ctx.getParent().getChild(1).getText());
				return visitChildren(ctx); 
	}
	@Override 
	public String visitStatement(ExprParser.StatementContext ctx) { 
				//System.out.println("I visited: Statement");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitAssignation(ExprParser.AssignationContext ctx) { 

				scopeCounter -= 1;

				System.out.println("I visited: Assignation");
				
				// I need to find the variable type first.
				System.out.println(ctx.getChild(0).getText() + " On scope: " + String.valueOf(scopeCounter));
/*
				String name = ctx.getChild(0).getText();
				String dotName = name.substring(0, name.indexOf(".")) + String.valueOf(scopeCounter);
				int dotScope = Integer.parseInt(SymbolTable.get(SymbolTable.get(dotName)[1])[2]);
				String varName = name.substring(name.indexOf(".") + 1, name.length());
				String varKey = varName + String.valueOf(dotScope);
				String varType = SymbolTable.get(varKey)[1];
*/
				scopeCounter += 1;

				return visitChildren(ctx); 
	}
	@Override 
	public String visitWhileBlock(ExprParser.WhileBlockContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitReturnBlock(ExprParser.ReturnBlockContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitPrint(ExprParser.PrintContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitScan(ExprParser.ScanContext ctx) { 
				//System.out.println("I visited ");
				return "char"; 
	}
	@Override 
	public String visitIfBlock(ExprParser.IfBlockContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitElseBlock(ExprParser.ElseBlockContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitLocation(ExprParser.LocationContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitDotLocation(ExprParser.DotLocationContext ctx) { 
				//System.out.println("I visited ");
				
				scopeCounter -= 1;

				//System.out.println("I visited ");
				String varName = ctx.getChild(2).getText();
				//System.out.println(varName);
				String varKey = ctx.getChild(0).getText() + String.valueOf(scopeCounter);
				varKey = SymbolTable.get(varKey)[1];
				varKey = varName + SymbolTable.get(varKey)[2];
				//System.out.println(varKey);
				String varType = SymbolTable.get(varKey)[1];
				//System.out.println(varType);

				scopeCounter += 1;

				return varType;

	}
	@Override 
	public String visitDeclaredVariable(ExprParser.DeclaredVariableContext ctx) { 
				//System.out.println("I visited ");
				return visit(ctx.getChild(0)); 
	}
	@Override 
	public String visitVariable(ExprParser.VariableContext ctx) { 
				
				scopeCounter -= 1;

				//System.out.println("I visited ");
				String varName = ctx.getText();
				//System.out.println(varName);
				String varKey = varName + String.valueOf(scopeCounter);
				//System.out.println(varKey);
				String varType = SymbolTable.get(varKey)[1];
				//System.out.println(varType);

				scopeCounter += 1;

				return varType;
	}
	@Override 
	public String visitArrayVariable(ExprParser.ArrayVariableContext ctx) { 

				scopeCounter -= 1;

				//System.out.println("I visited ");
				String varName = ctx.getChild(0).getText();
				String varKey = varName + String.valueOf(scopeCounter);
				String varType = SymbolTable.get(varKey)[1];
				varType = varType.split(",")[0];
				varType = varType.substring(6, varType.length());

				//System.out.println(varType);

				scopeCounter += 1;

				// Here I must check that the index exists in my SymbolTable, if not throw a null pointer exception.

				return varType; 
	}
	@Override 
	public String visitExpressionInP(ExprParser.ExpressionInPContext ctx) { 
				//System.out.println("I visited ");
				//System.out.println(visit(ctx.getChild(1)));
				return visit(ctx.getChild(1)); 
	}
	@Override 
	public String visitNExpression(ExprParser.NExpressionContext ctx) { 
				//System.out.println("I visited ");
				return visit(ctx.getChild(0)); 
	}
	@Override 
	public String visitOrExpression(ExprParser.OrExpressionContext ctx) { 
				//System.out.println("I visited ");

				try {
					String left = visit(ctx.getChild(0));
					String right = visit(ctx.getChild(2));

					//System.out.println(left + " - " + right);

					if (left.equals(right)) {
						//System.out.println("bool");
						return "boolean";
					} else {
						// If they ain't bool it's an error.
						errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getText() + ". All expressions must be 'boolean'.";
						writeErrors(errorMsg, file);
						return "boolean";
					}
				} catch (NullPointerException e) {
					// Simple node, no problem.
					return visit(ctx.getChild(0));
				}
	}
	@Override 
	public String visitAndExpression(ExprParser.AndExpressionContext ctx) { 
				//System.out.println("I visited ");
				
				try {
					String left = visit(ctx.getChild(0));
					String right = visit(ctx.getChild(2));

					//System.out.println(left + " - " + right);

					if (left.equals(right)) {
						//System.out.println("bool");
						return "boolean";
					} else {
						// If they ain't bool it's an error.
						errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getText() + ". All expressions must be 'boolean'.";
						writeErrors(errorMsg, file);
						return "boolean";
					}
				} catch (NullPointerException e) {
					// Simple node, no problem.
					return visit(ctx.getChild(0));
				}
	}
	@Override 
	public String visitEqualsExpression(ExprParser.EqualsExpressionContext ctx) { 
				//System.out.println("I visited ");
				
				try {
					String left = visit(ctx.getChild(0));
					String right = visit(ctx.getChild(2));

					//System.out.println(left + " - " + right);

					if (left.equals(right)) {
						//System.out.println("bool");
						return left;
					} else {
						// If they ain't bool it's an error.
						errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getText() + ". All expressions must be " + left + ".";
						writeErrors(errorMsg, file);
						return left;
					}
				} catch (NullPointerException e) {
					// Simple node, no problem.
					return visit(ctx.getChild(0));
				}
	}
	@Override 
	public String visitRelationExpression(ExprParser.RelationExpressionContext ctx) { 
				//System.out.println("I visited ");
				
				try {
					String left = visit(ctx.getChild(0));
					String right = visit(ctx.getChild(2));

					//System.out.println(left + " - " + right);

					if (left.equals(right) && left.equals("int")) {
						//System.out.println("bool");
						return "boolean";
					} else {
						// If they ain't bool it's an error.
						errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getText() + ". All expressions must be " + left + ".";
						writeErrors(errorMsg, file);
						return "boolean";
					}
				} catch (NullPointerException e) {
					// Simple node, no problem.
					return visit(ctx.getChild(0));
				}
	}
	@Override 
	public String visitAddSubsExpression(ExprParser.AddSubsExpressionContext ctx) { 
				//System.out.println("I visited ");

				try {
					String left = visit(ctx.getChild(0));
					String right = visit(ctx.getChild(2));

					//System.out.println(left + " - " + right);

					if (left.equals(right) && left.equals("int")) {
						//System.out.println("bool");
						return "int";
					} else {
						// If they ain't bool it's an error.
						errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getText() + ". All expressions must be int.";
						writeErrors(errorMsg, file);
						return "int";
					}
				} catch (NullPointerException e) {
					// Simple node, no problem.
					return visit(ctx.getChild(0));
				}
	}
	@Override 
	public String visitMulDivExpression(ExprParser.MulDivExpressionContext ctx) { 
				//System.out.println("I visited ");

				try {
					String left = visit(ctx.getChild(0));
					String right = visit(ctx.getChild(2));

					//System.out.println(left + " - " + right);

					if (left.equals(right) && left.equals("int")) {
						//System.out.println("bool");
						return "int";
					} else {
						// If they ain't bool it's an error.
						errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getText() + ". All expressions must be " + left + ".";
						writeErrors(errorMsg, file);
						return "int";
					}
				} catch (NullPointerException e) {
					// Simple node, no problem.
					return visit(ctx.getChild(0));
				}
	}
	@Override 
	public String visitPrExpression(ExprParser.PrExpressionContext ctx) { 
				//System.out.println("I visited ");

				try {
					String left = visit(ctx.getChild(0));
					String right = visit(ctx.getChild(2));

					//System.out.println(left + " - " + right);

					if (left.equals(right) && left.equals("int")) {
						//System.out.println("bool");
						return "int";
					} else {
						// If they ain't bool it's an error.
						errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getText() + ". All expressions must be " + left + ".";
						writeErrors(errorMsg, file);
						return "int";
					}
				} catch (NullPointerException e) {
					// Simple node, no problem.
					return visit(ctx.getChild(0));
				}
	}
	@Override 
	public String visitBasicExpression(ExprParser.BasicExpressionContext ctx) { 
				//System.out.println("I visited: " + ctx.getText() );
				try {
					// Basic expression can have up to 3 outputs, in any case I know how they look and the type of each.
					// First case is a casting, so return value is "integer", or "char".
					if (ctx.getChild(0).getText().equals("(")) {
						// If they're casting then return casting type.
						//System.out.println(ctx.getChild(1).getText() + " <-> " + ctx.getParent().getChild);
						return ctx.getChild(1).getText();
					} else if (ctx.getChild(0).getText().equals("-") || ctx.getChild(0).getText().equals("!")) {
						// If it's an unary operator i have to check if they are valid expressions. 
						if (ctx.getChild(0).getText().equals("-")) {
							// Minus operator can only be aplied to a integer.
							//System.out.println(ctx.getText());
							if (!visit(ctx.getChild(1)).equals("int")) {
								// It's and error.
								errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getText() + ". Invalid use of '-' type must be 'int'.";
								writeErrors(errorMsg, file);
							} 
							return "int";
						}

						if (ctx.getChild(0).getText().equals("!")) {
							// not operator can only be aplied to a boolean.
							//System.out.println(ctx.getChild(1).getText());
							//System.out.println(visit(ctx.getChild(1)));
							if (!visit(ctx.getChild(1)).equals("boolean")) {
								// It's and error.
								errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getText() + ". Invalid use of '!' type must be 'boolean'.";
								writeErrors(errorMsg, file);
							} 
							return "boolean";
						}

					} else {
						// If not one ob the above cases then it's just a basic expression and I return the visit child basic.
						return visit(ctx.getChild(0));
					}
				} catch (NullPointerException e) {
					errorMsg = errorMsg + newline + "Grammar Error - At line: " + ctx.getParent().getText() + ". Invalid invalid syntax for this Grammar.";
					writeErrors(errorMsg, file);
					return visitChildren(ctx);
				}

				return visitChildren(ctx);
	}
	@Override 
	public String visitBasic(ExprParser.BasicContext ctx) {
				if ( visit(ctx.getChild(0)) == null ) {
					return visitChildren(ctx);
				} else {
					//System.out.println("I visited: " + visit(ctx.getChild(0)) + " \nExp -> " + ctx.getParent().getChild(0).getText());
					return visit(ctx.getChild(0));
				}
	}
	@Override 
	public String visitArg(ExprParser.ArgContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitDeclaredMethodCall(ExprParser.DeclaredMethodCallContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitAs_op(ExprParser.As_opContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitMd_op(ExprParser.Md_opContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitPr_op(ExprParser.Pr_opContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitRel_op(ExprParser.Rel_opContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitEq_op(ExprParser.Eq_opContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitCond_op(ExprParser.Cond_opContext ctx) { 
				//System.out.println("I visited ");
				return visitChildren(ctx); 
	}
	@Override 
	public String visitLiteral(ExprParser.LiteralContext ctx) { 
				//System.out.println("I visited: " + visit(ctx.getChild(0)));
				return visit(ctx.getChild(0)); 
	}
	@Override 
	public String visitInt_literal(ExprParser.Int_literalContext ctx) { 
				//System.out.println("I visited ");
				return "int"; 
	}
	@Override 
	public String visitChar_literal(ExprParser.Char_literalContext ctx) { 
				//System.out.println("I visited ");
				return "char"; 
	}
	@Override 
	public String visitBool_literal(ExprParser.Bool_literalContext ctx) { 
				//System.out.println("I visited ");
				return "boolean"; 
	}
}