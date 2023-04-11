package com.goody.utils.puer;

import org.apache.commons.io.IOUtils;
import org.tensorflow.Graph;
import org.tensorflow.Operand;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Constant;
import org.tensorflow.op.image.DecodeJpeg;
import org.tensorflow.op.io.ReadFile;
import org.tensorflow.types.TString;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO
 *
 * @author Goody
 * @version 1.0, 2023/4/4
 * @since 1.0.0
 */
public class Main {
    public static void toy(String imageBytes) {
        try (SavedModelBundle model = SavedModelBundle.load("puer/src/main/resources/model/", "serve")) {
            try (Graph g = new Graph(); Session s = new Session(g)) {

                final Ops tf = Ops.create(g);

                Constant<TString> fileName = tf.constant("puer/src/main/resources/sfw-pic.jpg");
                ReadFile readFile = tf.io.readFile(fileName);
                DecodeJpeg.Options options = DecodeJpeg.channels(3L);
                DecodeJpeg decodeImage = tf.image.decodeJpeg(readFile.contents(), options);

                Operand output = tf.expandDims(decodeImage, tf.constant(0));
                output = tf.image.resizeBilinear(output, tf.constant(new int[]{224, 224}));
                output = tf.math.sub(output, tf.constant(117F));
                output = tf.math.div(output, tf.constant(1F));

                // Generally, there may be multiple output tensors, all of them must be closed to prevent resource leaks.
                Tensor tensor = s.runner().fetch(output).run().get(0);

                System.out.println(model.session().runner().feed("input", tensor).fetch("predictions").run().get(0));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("sfw-pic.jpg");
        toy(IOUtils.toString(is));
    }
}
