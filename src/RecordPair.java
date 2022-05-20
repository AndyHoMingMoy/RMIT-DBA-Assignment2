public class RecordPair <Key extends Comparable<Key>, Value> {

    public Key key;
    public Value value;

    public RecordPair(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "RecordPair { Key = " + key + ", Value = " + value + " }";
    }
}
