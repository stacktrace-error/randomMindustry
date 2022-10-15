package randomMindustry.random;

import arc.struct.Seq;

public class RandomUtil {
    public static <T> void shuffle(Seq<T> seq, SyncedRand rand) {
        T[] items = seq.items;
        for (int i = seq.size - 1; i >= 0; i--) {
            int ii = rand.random(i);
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }
}