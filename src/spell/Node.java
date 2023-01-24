package spell;

public class Node implements INode {

    private int wordCount;
    private Node[] children;
    //Constructor
    public Node() {
        wordCount = 0;
        children = new Node[26];
    }

    @Override
    public int getValue() {
        return wordCount;
    }

    @Override
    public void incrementValue() {
        wordCount = wordCount + 1;
    }

    @Override
    public Node[] getChildren() {
        return children;
    }
}
