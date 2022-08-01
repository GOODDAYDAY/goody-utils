package com.goody.utils.qianliang.processor.impl;

import com.goody.utils.qianliang.processor.BaseProcessor;
import com.google.auto.service.AutoService;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.util.stream.Stream;

/**
 * {@link LogSelf} processor
 *
 * @author Goody
 * @version 1.0, 2022/5/4
 * @since 1.0.0
 */
@SupportedAnnotationTypes({"com.goody.utils.qianliang.processor.impl.LogSelf"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ZLogSelfProcessor extends BaseProcessor<LogSelf> {

    @Override
    protected Stream<JCTree> generate() {
        try {
            return Stream.of(this.logSelf());
        } catch (Exception e) {
            return Stream.empty();
        }
    }

    /**
     * <pre>
     *  <@javax.annotation.PostConstruct()
     *  public void logSelf() {
     *      log.warn("---------- {}", this);
     *  }
     * </pre>
     */
    private JCTree logSelf() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        JCTree.JCAnnotation jcAnnotation = treeMaker.Annotation(chainDots("javax.annotation.PostConstruct"), List.nil());
        JCModifiers jcModifiers = treeMaker.Modifiers(Flags.PUBLIC, List.of(jcAnnotation));
        Name methodName = getNameFromString("logSelf");
        JCExpression returnType = treeMaker.Type((Type) (Class.forName("com.sun.tools.javac.code.Type$JCVoidType").newInstance()));
        List<JCTypeParameter> typeParameters = List.nil();
        List<JCVariableDecl> parameters = List.nil();
        List<JCExpression> throwsClauses = List.nil();
        JCBlock jcBlock = treeMaker.Block(0, List.of(treeMaker.Exec(
            treeMaker.Apply(List.nil(), chainDots("log.warn"), List.of(treeMaker.Literal("---------- {}"), treeMaker.Ident(getNameFromString("this")))))));
        JCMethodDecl method = treeMaker
            .MethodDef(jcModifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
        return method;
    }

    private Name getNameFromString(String s) {
        return names.fromString(s);
    }

    public JCExpression chainDots(String element) {
        JCExpression e = null;
        String[] elems = element.split("\\.");
        for (int i = 0; i < elems.length; i++) {
            e = e == null ? treeMaker.Ident(names.fromString(elems[i]))
                : treeMaker.Select(e, names.fromString(elems[i]));
        }
        return e;
    }
}
