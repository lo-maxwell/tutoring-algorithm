/**
 * An evaluator. 
 */

package Weighting;

import java.util.ArrayList;

import Items.Data;
import Items.Slot;
import Items.Tutor;

public class Evaluator {
    public static final double PENALTY = 10000;
    public static final double ADJACENT_PREF = 50;
    public static final double ADJACENT_DISPREF = 50;

    public static double evaluate(Data assign, Weighting w) {
        double score = 0;
        outer : for (Tutor t : assign.tutors) {
            if (t.numAssignments != t.slots.size()) {
                // tutor should have been assigned a slot
                // or tutor was assigned an extra slot
                score -= PENALTY;
                continue;
            }

            for (int i = 0; i < t.slots.size(); i++)
                for (int j = i + 1; j < t.slots.size(); j++)
                    if (t.slots.get(i).simultaneous(t.slots.get(j))) {
                        score -= PENALTY;
                        continue outer;
                    }

            double d = 0;
            ArrayList <Slot> temp = new ArrayList <Slot> (t.slots);
            t.slots.clear();
            // simulate re-adding the slots in one by one. 
            // order shouldn't matter when adding slots in 
            for (Slot s : temp) {
                d += w.weight (t, s);
                t.assign(s);
            }
            score += d;
        }
        return score;
    }
}
