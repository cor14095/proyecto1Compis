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
	// - - 3: Value of Node.
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

    // Method to print hashMap
    public void printHash(Map<String,String[]> map) {
    	//write to file : "fileone"
	    try {
	    	File fileTwo = new File("SymbolTable.table");
	    	FileOutputStream fos = new FileOutputStream(fileTwo);
	        PrintWriter pw = new PrintWriter(fos);

	        for(Map.Entry<String,String[]> m :map.entrySet()) {
	            pw.println(m.getKey()+"\t=\t"+Arrays.toString(m.getValue()));
	        }

	        pw.flush();
	        pw.close();
	        fos.close();
	    } catch (Exception e) {  }
	}

	@Override
	public String visitProgram(ExprParser.ProgramContext ctx) {

				System.out.println("I visited: PROGRAM");

				String errorMsg = "Syntax error list:";
   				file = Paths.get("ErrorLog_Grammar.log");
   				System.out.println("Visiting!");

				String[] data =  {ctx.getChild(1).getText(), "", String.valueOf(scopeCounter), ""};
				//System.out.println(Arrays.toString(data));

				SymbolTable.put(ctx.getChild(1).getText()+scopeCounter, data);
				scopeCounter += 1;

				visitChildren(ctx);

				printHash(SymbolTable);
				System.out.println("End tree.");

				return "final";
	}
	@Override
	public String visitDeclaration(ExprParser.DeclarationContext ctx) {

				//System.out.println("I visited: Declaration");
				return visitChildren(ctx);
	}
	@Override
	public String visitVarDeclaration(ExprParser.VarDeclarationContext ctx) {

    if (ctx.getParent().getChild(0).getText().equals("if")
      || ctx.getParent().getChild(0).getText().equals("else")
      || ctx.getParent().getChild(0).getText().equals("while")) {

    } else {
          scopeCounter -= 1;
        }
				String[] data = new String[4];

				System.out.println("I visited: VarDeclaration of " + ctx.getChild(1).getText() + " on scope " + String.valueOf(scopeCounter));

				// We have 2 cases here, 1) normal declaration, 2) Array declaration.
				// For the first case it's simple
				if (ctx.getText().indexOf('[') == -1) {
					// If there's not a '[', then it's normal.
					data[0] = ctx.getChild(1).getText();
					data[1] = ctx.getChild(0).getText();
					data[2] = String.valueOf(scopeCounter);
					data[3] = "";

					//System.out.println(Arrays.toString(data));
				} else {
					// If it has one then we need to do a little tweek.
					data[0] = ctx.getChild(1).getText();
					data[1] = "_Array" + ctx.getChild(0).getText() + "," + ctx.getChild(3); // Trick here is that in the table we look for "_Array" and we have all Arrays and after a split we have the size.
					data[2] = String.valueOf(scopeCounter);
					data[3] = "";

					//System.out.println(Arrays.toString(data));
				}

				//System.out.println("Key is: " + ctx.getChild(1).getText()+scopeCounter);
        try {
              String test = SymbolTable.get(ctx.getChild(1).getText()+scopeCounter)[0];
              errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". Double declaration for variable.";
              writeErrors(errorMsg, file);
        } catch (Exception e) {
              SymbolTable.put(ctx.getChild(1).getText()+scopeCounter, data);
        }

        if (ctx.getParent().getChild(0).getText().equals("if")
          || ctx.getParent().getChild(0).getText().equals("else")
          || ctx.getParent().getChild(0).getText().equals("while")) {
        } else {
          scopeCounter += 1;
        }

				return visitChildren(ctx);
	}
	@Override
	public String visitStructDeclaration(ExprParser.StructDeclarationContext ctx) {

				System.out.println("I visited: StructDeclaration of " + ctx.getChild(1).getText());

				String[] data =  {ctx.getChild(1).getText(),
					ctx.getChild(0).getText() + "_" + ctx.getChild(1).getText(),
					String.valueOf(scopeCounter),
					""};
				//System.out.println(Arrays.toString(data));
        try {
              String test = SymbolTable.get(ctx.getChild(1).getText())[0];
              errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". Double declaration for variable.";
              writeErrors(errorMsg, file);
        } catch (Exception e) {
              SymbolTable.put(ctx.getChild(1).getText(), data);
        }
				scopeCounter += 1;

				return visitChildren(ctx);
	}
	@Override
	public String visitVarType(ExprParser.VarTypeContext ctx) {
				//System.out.println("I visited: varType");
				return visit(ctx.getChild(0));
	}
	@Override
	public String visitMethodDeclaration(ExprParser.MethodDeclarationContext ctx) {

				String[] data = new String[4];
				String params = "";
				int paramCounter = 3;			// Start the paramDeclaration at 3rd child.

				System.out.println("I visited: MethodDeclaration of " + ctx.getChild(1).getText());

				// Make Method values.
				data[0] = ctx.getChild(1).getText();
				data[1] = ctx.getChild(0).getText();
				data[2] = String.valueOf(scopeCounter);
        // data[3] will hold all the types of parameters.

        String paramType = "";
				data[3] = "";
        while (!ctx.getChild(paramCounter).getText().equals(")")) {
          // While it does not find the ')', then I have to search for parameters.
          if (!ctx.getChild(paramCounter).getText().equals(",")) {
            // If it's not a coma (','),  then I have to get the parameter type.
            paramType = visit(ctx.getChild(paramCounter));
            data[3] += paramType+",";
          }
          // If it is just move on.
          paramCounter += 1;
        }
        System.out.println(data[3]);
				//System.out.println(Arrays.toString(data));

				// Add them to the Hash Map.
        System.out.println(data[1] + data[0] + data[3]);
        try {
          String test = SymbolTable.get(data[1] + data[0] + data[3])[0];
          errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". Double declaration for method.";
          writeErrors(errorMsg, file);
        } catch (NullPointerException e) {
          SymbolTable.put(data[1] + data[0] + data[3], data);
        }

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
        try {
  				String[] data = new String[4];

  				System.out.println("I visited: ParameterDeclaration for: " + ctx.getParent().getChild(1).getText()
  					+ " of " + ctx.getChild(1).getText());

  				// We have 2 cases here, 1) normal declaration, 2) Array declaration.
  				// For the first case it's simple
  				if (ctx.getText().indexOf('[') == -1) {
  					// If there's not a '[', then it's normal.
  					data[0] = ctx.getChild(1).getText();
  					data[1] = ctx.getChild(0).getText();
  					data[2] = String.valueOf(scopeCounter);
  					data[3] = "";

  					//System.out.println(Arrays.toString(data));
  				} else {
  					// If it has one then we need to do a little tweek.
  					data[0] = ctx.getChild(1).getText();
  					data[1] = "_Array" + ctx.getChild(0).getText() + "," + ctx.getChild(3); // Trick here is that in the table we look for "_Array" and we have all Arrays and after a split we have the size.
  					data[2] = String.valueOf(scopeCounter);
  					data[3] = "";

  					//System.out.println(Arrays.toString(data));
  				}

  				SymbolTable.put(ctx.getChild(1).getText()+scopeCounter, data);
        } catch (NullPointerException e) {
          errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". There are more than 1 equal parameters in this declaration.";
          writeErrors(errorMsg, file);
        }
				scopeCounter += 1;
				return visit(ctx.getChild(0));
	}
	@Override
	public String visitParameterType(ExprParser.ParameterTypeContext ctx) {
				//System.out.println("I visited: ParameterType");
        //System.out.println(ctx.getChild(0).getText());
				return ctx.getChild(0).getText();
	}
	@Override
	public String visitBlock(ExprParser.BlockContext ctx) {
				//System.out.println("I visited: Block For: " +
        //  ctx.getParent().getText() + " on scopeCounter = " +
        //  String.valueOf(scopeCounter));
        return visitChildren(ctx);
	}
	@Override
	public String visitStatement(ExprParser.StatementContext ctx) {
				//System.out.println("I visited: Statement");
				return visitChildren(ctx);
	}
	@Override
	public String visitAssignation(ExprParser.AssignationContext ctx) {

				//scopeCounter -= 1;

				System.out.println("I visited: Assignation on scope: " + String.valueOf(scopeCounter));

				// I need to find the variable type first.
				//System.out.println("------------ 1 ------------");
				String left = visit(ctx.getChild(0));
				//System.out.println("------------ 2 ------------");
				String right = visit(ctx.getChild(2));
				//System.out.println("------------ 3 ------------");
				//System.out.println(left + " - " + right);

				// Now that i have both types I must check if they're the same.
				if (left.equals(right)) {
					// If they're the same I must add the value to the variable on the left.
					String varName = ctx.getChild(0).getText();
					String varValue = ctx.getChild(2).getText();
					System.out.println(varName + " -> " + varValue);
          return left;
				} else {
          errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". Incorrect assignation, cannot assign " + right + " to " + left + ".";
          writeErrors(errorMsg, file);
        }

				//scopeCounter += 1;

				return visitChildren(ctx);
	}
	@Override
	public String visitWhileBlock(ExprParser.WhileBlockContext ctx) {
				//System.out.println("I visited ");
				scopeCounter += 1;
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
				System.out.println(ctx.getChild(2).getText());
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
				scopeCounter += 1;
        return visitChildren(ctx);
	}
	@Override
	public String visitElseBlock(ExprParser.ElseBlockContext ctx) {
				//System.out.println("I visited ");
				scopeCounter += 1;
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
        String varType = "";

        try {
  				//System.out.println("I visited ");
  				String varName = ctx.getChild(2).getText();
  				//System.out.println(varName);
  				String varKey = ctx.getChild(0).getText() + String.valueOf(scopeCounter);
  				varKey = SymbolTable.get(varKey)[1];
  				varKey = varName + SymbolTable.get(varKey)[2];
  				System.out.println(varKey);
  				varType = SymbolTable.get(varKey)[1];
  				System.out.println(varType);
        } catch (NullPointerException e) {
          errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". No dot location variable in this scope.";
          writeErrors(errorMsg, file);
          varType = "perry";
        }

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
        String varType = "";

        try {
  				//System.out.println("I visited ");
  				String varName = ctx.getText();
  				//System.out.println(varName + "<- Var Name!");
  				String varKey = varName + String.valueOf(scopeCounter);
  				//System.out.println(varKey + "<- Var Key!");
  				varType = SymbolTable.get(varKey)[1];
  				//System.out.println(varType + "<- Var Type!");
        } catch (NullPointerException e) {
          errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". No viariable declared in this scope.";
          writeErrors(errorMsg, file);
          varType = "perry";
        }
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
					// Thing is that in the if conndition y need to use the scope of the parent, not the if block scope.
					if (ctx.getParent().getChild(0).getText().equals("if")
						|| ctx.getParent().getChild(0).getText().equals("else")
						|| ctx.getParent().getChild(0).getText().equals("while")) {
						scopeCounter -= 1;
						//System.out.println("Back 1 scope.");
					}

					String left = visit(ctx.getChild(0));
					String right = visit(ctx.getChild(2));

					//System.out.println(left + " - " + right);

					// I visit the childs and then I can go back to If scope.
					if (ctx.getParent().getChild(0).getText().equals("if")
						|| ctx.getParent().getChild(0).getText().equals("else")
						|| ctx.getParent().getChild(0).getText().equals("while")) {
						scopeCounter += 1;
						//System.out.println("Up one scope.");
					}

					if (left.equals(right)) {
						//System.out.println("bool");
						return "boolean";
					} else {
						// If they ain't bool it's an error.
						errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". All expressions must be 'boolean'.";
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
						errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". All expressions must be 'boolean'.";
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
						errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". All expressions must be " + left + ".";
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
						errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". All expressions must be " + left + ".";
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
						errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". All expressions must be int.";
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
						errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". All expressions must be " + left + ".";
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
						errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". All expressions must be " + left + ".";
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
								errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". Invalid use of '-' type must be 'int'.";
								writeErrors(errorMsg, file);
							}
							return "int";
						}

						if (ctx.getChild(0).getText().equals("!")) {
							// not operator can only be aplied to a boolean.
							//System.out.println(ctx.getChild(1).getText() + " <- Child 1.");
							//System.out.println(visit(ctx.getChild(1)) + " <- Child 1 varType.");
							if (!visit(ctx.getChild(1)).equals("boolean")) {
								// It's and error.
								errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getText() + ". Invalid use of '!' type must be 'boolean'.";
								writeErrors(errorMsg, file);
							}
							return "boolean";
						}

					} else {
						// If not one ob the above cases then it's just a basic expression and I return the visit child basic.
						return visit(ctx.getChild(0));
					}
				} catch (NullPointerException e) {
					//System.out.println(ctx.getText() + " <------------------");
					//System.out.println(e.toString());
					errorMsg = errorMsg + newline + "Lexic Error - At line: " + ctx.getParent().getText() + ". Invalid invalid syntax for this Grammar.";
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
				return visit(ctx.getChild(0));
	}
	@Override
	public String visitDeclaredMethodCall(ExprParser.DeclaredMethodCallContext ctx) {
				//System.out.println("I visited ");
				// I have to finish this!

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
