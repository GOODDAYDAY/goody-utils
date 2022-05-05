package com.goody.utils.qianliang.example;

import com.goody.utils.qianliang.processor.BaseProcessor;
import com.google.auto.service.AutoService;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

/**
 * {@link AddVariable} processor
 *
 * @author Goody
 * @version 1.0, 2022/5/5
 * @since 1.0.0
 */
@SupportedAnnotationTypes({"com.goody.utils.qianliang.example.AddVariable"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
@Deprecated
public class AddVariableProcessor extends BaseProcessor<AddVariable> {

    @Override
    @Nonnull
    public List<JCTree> generate(JCTree.JCClassDecl jcClassDecl) {
        JCTree.JCVariableDecl jcVariableDecl = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PRIVATE),
                names.fromString("age"),
                treeMaker.Ident(names.fromString("String")),
                null);

        return List.of(jcVariableDecl);
    }
}
