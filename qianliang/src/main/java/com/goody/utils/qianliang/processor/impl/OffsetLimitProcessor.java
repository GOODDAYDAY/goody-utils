package com.goody.utils.qianliang.processor.impl;

import com.goody.utils.qianliang.processor.BaseProcessor;
import com.google.auto.service.AutoService;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Name;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.util.stream.Stream;

/**
 * {@link OffsetLimit} processor
 *
 * @author Goody
 * @version 1.0, 2022/5/5
 * @since 1.0.0
 */
@SupportedAnnotationTypes({"com.goody.utils.qianliang.processor.impl.OffsetLimit"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class OffsetLimitProcessor extends BaseProcessor<OffsetLimit> {


    @Override
    protected Stream<JCTree> generate() {
        return Stream.of(limit(), offset());
    }

    private JCTree limit() {
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PRIVATE);
        Name limitName = names.fromString("limit");
        return treeMaker.VarDef(modifiers, limitName, treeMaker.TypeIdent(TypeTag.INT), null);
    }

    private JCTree offset() {
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PRIVATE);
        Name limitName = names.fromString("offset");
        return treeMaker.VarDef(modifiers, limitName, treeMaker.TypeIdent(TypeTag.INT), null);
    }
}
