package com.goody.utils.qianliang.example;

import com.goody.utils.qianliang.processor.BaseProcessor;
import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.google.common.base.Throwables;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import java.util.stream.Stream;

/**
 * {@link AddMethod} processor
 *
 * @author Goody
 * @version 1.0, 2022/5/4
 * @since 1.0.0
 */
@SupportedAnnotationTypes({"com.goody.utils.qianliang.example.AddMethod"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
@Deprecated
public class AddMethodProcessor extends BaseProcessor<AddMethod> {

    @Override
    protected Stream<JCTree> handleDecl(JCVariableDecl jcVariableDecl) {
        try {
            return Stream.of(this.generateGetterMethod(jcVariableDecl), this.generateSetterMethod(jcVariableDecl));
        } catch (ReflectiveOperationException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, Throwables.getStackTraceAsString(e));
        }
        return Stream.empty();
    }

    private JCMethodDecl generateGetterMethod(JCVariableDecl jcVariable) {
        JCModifiers jcModifiers = treeMaker.Modifiers(Flags.PUBLIC);
        Name methodName = handleMethodSignature(jcVariable.getName(), "get");
        ListBuffer<JCStatement> jcStatements = new ListBuffer<>();
        jcStatements.append(
                treeMaker.Return(treeMaker.Select(treeMaker.Ident(getNameFromString("this")), jcVariable.getName())));
        JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());
        JCExpression returnType = jcVariable.vartype;
        List<JCTypeParameter> typeParameters = List.nil();
        List<JCVariableDecl> parameters = List.nil();
        List<JCExpression> throwsClauses = List.nil();
        return treeMaker
                .MethodDef(jcModifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
    }

    private JCMethodDecl generateSetterMethod(JCVariableDecl jcVariable) throws ReflectiveOperationException {
        JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        Name variableName = jcVariable.getName();
        Name methodName = handleMethodSignature(variableName, "set");
        ListBuffer<JCStatement> jcStatements = new ListBuffer<>();
        jcStatements.append(treeMaker.Exec(treeMaker
                .Assign(treeMaker.Select(treeMaker.Ident(getNameFromString("this")), variableName),
                        treeMaker.Ident(variableName))));
        JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());
        JCExpression returnType =
                treeMaker.Type((Type) (Class.forName("com.sun.tools.javac.code.Type$JCVoidType").newInstance()));
        List<JCTypeParameter> typeParameters = List.nil();
        JCVariableDecl variableDecl = treeMaker
                .VarDef(treeMaker.Modifiers(Flags.PARAMETER), jcVariable.name, jcVariable.vartype, null);
        List<JCVariableDecl> parameters = List.of(variableDecl);
        List<JCExpression> throwsClauses = List.nil();
        return treeMaker
                .MethodDef(modifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
    }

    private Name handleMethodSignature(Name name, String prefix) {
        final String nameStr = prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name.toString());
        return names.fromString(nameStr);
    }

    private Name getNameFromString(String s) {
        return names.fromString(s);
    }

}
