// Generated from Expr.g4 by ANTLR 4.6
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExprParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(ExprParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(ExprParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(ExprParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#structDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDeclaration(ExprParser.StructDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarType(ExprParser.VarTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(ExprParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#methodType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodType(ExprParser.MethodTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDeclaration(ExprParser.ParameterDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#parameterType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterType(ExprParser.ParameterTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(ExprParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(ExprParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#assignation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignation(ExprParser.AssignationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#whileBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileBlock(ExprParser.WhileBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#returnBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnBlock(ExprParser.ReturnBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#print}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(ExprParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#scan}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScan(ExprParser.ScanContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#ifBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfBlock(ExprParser.IfBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#elseBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseBlock(ExprParser.ElseBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#location}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocation(ExprParser.LocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#dotLocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotLocation(ExprParser.DotLocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#declaredVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaredVariable(ExprParser.DeclaredVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(ExprParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#arrayVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVariable(ExprParser.ArrayVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#expressionInP}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionInP(ExprParser.ExpressionInPContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#nExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNExpression(ExprParser.NExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#orExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(ExprParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(ExprParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#equalsExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualsExpression(ExprParser.EqualsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationExpression(ExprParser.RelationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#addSubsExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubsExpression(ExprParser.AddSubsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#mulDivExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivExpression(ExprParser.MulDivExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#prExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrExpression(ExprParser.PrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#basicExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicExpression(ExprParser.BasicExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#basic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasic(ExprParser.BasicContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(ExprParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#declaredMethodCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaredMethodCall(ExprParser.DeclaredMethodCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#as_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAs_op(ExprParser.As_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#md_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMd_op(ExprParser.Md_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#pr_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPr_op(ExprParser.Pr_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#rel_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRel_op(ExprParser.Rel_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#eq_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq_op(ExprParser.Eq_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#cond_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond_op(ExprParser.Cond_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(ExprParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#int_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt_literal(ExprParser.Int_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#char_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChar_literal(ExprParser.Char_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#bool_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_literal(ExprParser.Bool_literalContext ctx);
}