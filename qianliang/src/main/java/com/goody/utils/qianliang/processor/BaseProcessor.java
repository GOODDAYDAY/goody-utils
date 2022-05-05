package com.goody.utils.qianliang.processor;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Stream;

/**
 * base processor and init the com.sun.tools.javac Objects
 *
 * @param <T> The Annotation
 * @author Goody
 * @version 1.0, 2022/5/5
 * @since 1.0.0
 */
public abstract class BaseProcessor<T extends Annotation> extends AbstractProcessor {

    /** annotation type */
    protected Class<T> clazz;
    /** javac trees */
    protected JavacTrees trees;
    /** AST */
    protected TreeMaker treeMaker;
    /** mark name */
    protected Names names;
    /** log */
    protected Messager messager;
    /** filer */
    protected Filer filer;
    /** the jcTrees generated by annotation to add */
    protected List<JCTree> annotationJCTrees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // transfer type T to Class
        final Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            this.clazz = (Class<T>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        } else {
            this.clazz = null;
        }
        this.trees = JavacTrees.instance(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        final Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        // init list
        annotationJCTrees = List.nil();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(this.clazz)
                .stream()
                .map(element -> trees.getTree(element))
                // NOTE(goody): 2022/5/5
                // tree is the class input. Modify the `JCTree` to modify the method or argus
                // `visitClassDef` runs after than `visitAnnotation`, so method `visitAnnotation` can add `annotationJCTrees` to
                // `annotationJCTrees`. `visitClassDef` will prepend all
                .forEach(tree -> tree.accept(new TreeTranslator() {
                    @Override
                    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                        // NOTE(goody): 2022/5/4 https://stackoverflow.com/questions/46874126/java-lang-assertionerror-thrown-by-compiler-when-adding-generated-method-with-pa
                        // setMethod var is a new Object from jcVariable, the pos should be reset to jcClass
                        treeMaker.at(jcClassDecl.pos);

                        // generate the new method or variable or something else
                        final List<JCTree> jcTrees = generate(jcClassDecl);
                        jcClassDecl.defs = jcClassDecl.defs.prependList(jcTrees);

                        // add all elements in `annotationJCTrees`
                        jcClassDecl.defs = jcClassDecl.defs.prependList(annotationJCTrees);

                        super.visitClassDef(jcClassDecl);
                    }

                    @Override
                    public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                        if (isModify(jcMethodDecl)) {
                            super.visitMethodDef(modifyDecl(jcMethodDecl));
                        } else {
                            super.visitMethodDef(jcMethodDecl);
                        }
                    }

                    @Override
                    public void visitVarDef(JCTree.JCVariableDecl jcVariableDecl) {
                        if (isModify(jcVariableDecl)) {
                            super.visitVarDef(modifyDecl(jcVariableDecl));
                        } else {
                            super.visitVarDef(jcVariableDecl);
                        }
                    }

                    @Override
                    public void visitAnnotation(JCTree.JCAnnotation jcAnnotation) {
                        super.visitAnnotation(jcAnnotation);

                        final JCTree.JCAssign[] jcAssigns = jcAnnotation.getArguments()
                                .stream()
                                .filter(argu -> argu.getKind().equals(Tree.Kind.ASSIGNMENT))
                                .map(argu -> (JCTree.JCAssign) argu)
                                .toArray(JCTree.JCAssign[]::new);

                        if (jcAssigns.length > 0) {
                            annotationGenerateJCTree(handleJCAssign(List.from(jcAssigns)));
                        }
                    }
                }));
        return true;
    }

    /**
     * subclass should implement this method to add method or variable or others
     *
     * @param jcClassDecl jcClassDecl
     * @return new JCTree list
     */
    private List<JCTree> generate(JCTree.JCClassDecl jcClassDecl) {
        final JCTree[] trees = generate()
                .toArray(JCTree[]::new);

        // method Trees
        final JCTree[] methodTrees = jcClassDecl.defs
                .stream()
                .filter(k -> k.getKind().equals(Tree.Kind.METHOD))
                .map(tree -> (JCTree.JCMethodDecl) tree)
                .flatMap(jcMethodDecl -> handleDecl(jcMethodDecl))
                .toArray(JCTree[]::new);

        // variable trees
        final JCTree[] variableTrees = jcClassDecl.defs
                .stream()
                .filter(k -> k.getKind().equals(Tree.Kind.VARIABLE))
                .map(tree -> (JCTree.JCVariableDecl) tree)
                .flatMap(jcVariable -> handleDecl(jcVariable))
                .toArray(JCTree[]::new);

        return List.from(trees)
                .prependList(List.from(variableTrees))
                .prependList(List.from(methodTrees));
    }

    /**
     * check if the method need to be modified. default false
     *
     * @param jcMethodDecl jcmethodDecl
     * @return true -> need to be modified ; false -> need not to be
     */
    protected boolean isModify(JCTree.JCMethodDecl jcMethodDecl) {
        return false;
    }

    /**
     * modify the jcMethodDecl input.
     *
     * @param jcMethodDecl metaDecl
     * @return point Decl
     */
    protected JCTree.JCMethodDecl modifyDecl(JCTree.JCMethodDecl jcMethodDecl) {
        return jcMethodDecl;
    }

    /**
     * check if the method need to be modified. default false
     *
     * @param jcVariableDecl jcmethodDecl
     * @return true -> need to be modified ; false -> need not to be
     */
    protected boolean isModify(JCTree.JCVariableDecl jcVariableDecl) {
        return false;
    }

    /**
     * modify the jcVariableDecl input.
     *
     * @param jcVariableDecl metaDecl
     * @return point Decl
     */
    protected JCTree.JCVariableDecl modifyDecl(JCTree.JCVariableDecl jcVariableDecl) {
        return jcVariableDecl;
    }

    /**
     * generate with nothing
     *
     * @return Steam of JCTree to add
     */
    protected Stream<JCTree> generate() {
        return Stream.empty();
    }

    /**
     * every jcMethodDecl will input to
     *
     * @param jcMethodDecl jcMethodDecl
     * @return Steam of JCTree to add
     */
    protected Stream<JCTree> handleDecl(JCTree.JCMethodDecl jcMethodDecl) {
        return Stream.empty();
    }

    /**
     * every jcVariableDecl will input to
     *
     * @param jcVariableDecl jcVariableDecl
     * @return Steam of JCTree to add
     */
    protected Stream<JCTree> handleDecl(JCTree.JCVariableDecl jcVariableDecl) {
        return Stream.empty();
    }

    /**
     * every annotation argu will input to
     *
     * @param jcAssign jcAssign
     */
    protected List<JCTree> handleJCAssign(List<JCTree.JCAssign> jcAssign) {
        return List.nil();
    }

    protected final void annotationGenerateJCTree(JCTree jcTree) {
        this.annotationJCTrees = this.annotationJCTrees.prepend(jcTree);
    }

    protected final void annotationGenerateJCTree(List<JCTree> jcTrees) {
        this.annotationJCTrees = this.annotationJCTrees.prependList(jcTrees);
    }
}
