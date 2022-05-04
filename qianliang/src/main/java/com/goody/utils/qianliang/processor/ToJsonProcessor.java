package com.goody.utils.qianliang.processor;

import com.goody.utils.qianliang.annotation.ToJson;
import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.google.common.base.Throwables;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link com.goody.utils.qianliang.annotation.ToJson} processor
 *
 * @author Goody
 * @version 1.0, 2022/5/4
 * @since 1.0.0
 */
@SupportedAnnotationTypes({"com.goody.utils.qianliang.annotation.ToJson"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ToJsonProcessor extends AbstractProcessor {

    /** javac trees */
    private JavacTrees trees;
    /** AST */
    private TreeMaker treeMaker;
    /** mark name */
    private Names names;
    /** log */
    private Messager messager;
    /** filer */
    private Filer filer;

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.trees = JavacTrees.instance(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        final Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotation = roundEnv.getElementsAnnotatedWith(ToJson.class);
        annotation.stream().map(element -> trees.getTree(element))
                // tree is the class input. Modify the JCTree to modify the method or argus
                .forEach(tree -> tree.accept(new TreeTranslator() {
                    @Override
                    public void visitClassDef(JCTree.JCClassDecl jcClass) {
                        // NOTE(goody): 2022/5/4 https://stackoverflow.com/questions/46874126/java-lang-assertionerror-thrown-by-compiler-when-adding-generated-method-with-pa
                        // setMethod var is a new Object from jcVariable, the pos should be reset to jcClass
                        treeMaker.at(jcClass.pos);

                        Map<Name, JCTree.JCVariableDecl> treeMap =
                                jcClass.defs.stream().filter(k -> k.getKind().equals(Tree.Kind.VARIABLE))
                                        .map(tree -> (JCTree.JCVariableDecl) tree)
                                        .collect(Collectors.toMap(JCTree.JCVariableDecl::getName, Function.identity()));
                        treeMap.forEach((k, jcVariable) -> {
                            try {
                                // add getter,setter methods
                                final JCMethodDecl getMethod = generateGetterMethod(jcVariable);
                                final JCMethodDecl setMethod = generateSetterMethod(jcVariable);
                                jcClass.defs = jcClass.defs.prependList(List.of(getMethod, setMethod));
                            } catch (Exception e) {
                                messager.printMessage(Diagnostic.Kind.ERROR, Throwables.getStackTraceAsString(e));
                            }
                        });
                        super.visitClassDef(jcClass);
                    }

                    @Override
                    public void visitMethodDef(JCMethodDecl jcMethod) {
                        super.visitMethodDef(jcMethod);
                    }
                }));
        return true;
    }

    private JCMethodDecl generateGetterMethod(JCTree.JCVariableDecl jcVariable) {
        JCTree.JCModifiers jcModifiers = treeMaker.Modifiers(Flags.PUBLIC);
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
