package com.aidanogrady.qgrady;

import java.util.HashMap;
import java.util.Map;

/**
 * The box class holds all the information that defines a non-local box,
 * containing the probability distribution, and handling the validity of the
 * non-local box as well.
 *
 * @author Aidan O'Grady
 * @since 0.4
 */
public class Box {
    private Map<Instance, Double> distribution;

    /**
     * The number of inputs in the set-up.
     */
    private int inputs;

    /**
     * The number of outputs in the set-up.
     */
    private int outputs;

    /**
     * Constructor.
     *
     * @param box - the probability distribution.
     */
    public Box(double[][] box) {
        distribution = new HashMap<Instance, Double>(0);
        inputs = (int) (Math.log(box.length) / Math.log(2));
        outputs = (int) (Math.log(box[0].length) / Math.log(2));

        for(int i = 0; i < box.length; i++) {
            int[] input = intToBitArray(i, inputs);
            for(int j = 0; j < box[i].length; j++) {
                int[] output = intToBitArray(j, outputs);
                Instance prob = new Instance(inputs, outputs);
                prob.setInput(input);
                prob.setOutput(output);
                distribution.put(prob, box[i][j]);
            }
        }

        for(Instance prob : distribution.keySet()) {
            System.out.println(prob + " -> " + distribution.get(prob));
        }
        System.out.println("Non-signalling: " + isNonSignalling());
    }

    private int[] intToBitArray(int value, int size) {
        int[] array = new int[size];
        int index = size - 1;
        while(index >= 0) {
            array[index] = value % 2;
            value = value / 2;
            index--;
        }
        return array;
    }

    /**
     * A valid non-local box must fulfill the 'non-signalling' property. This
     * property can be summarized as 'the input of one party cannot influence
     * the output of another party's output'.
     *
     * @return if box is non-signalling.
     */
    private boolean isNonSignalling() {
        for (int a = 0; a < 2; a++) {
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 2; y++) {
                    for (int y_ = 0; y_ < 2; y_++) {
                        double sumB = 0;
                        double sumB_ = 0;
                        for (int b = 0; b < 2; b++) {
                            int[] in = {x, y};
                            int[] out = {a, b};
                            sumB += prob(in, out);
                            in = new int[] {x, y_};
                            sumB_ += prob(in, out);
                        }
                        if (sumB != sumB_)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns of the probability of the given output being produced by the
     * given input.
     *
     * @param input - input being exmained
     * @param output - outpu being examined
     * @return p(output, input)
     */
    public double prob(int[] input, int[] output) {
        Instance prob = new Instance(inputs, outputs);
        prob.setInput(input);
        prob.setOutput(output);
        return distribution.get(prob);
    }
}
