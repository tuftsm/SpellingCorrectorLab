package spell;

import java.util.*;


public class Trie implements ITrie {

    private Node root;
    private int wordCount;
    private int nodeCount;
    //Constructor
    public Trie() {
        root = new Node();
        wordCount = 0;
        nodeCount = 0;
    }
    @Override
    public void add(String word) {
        //start at root
        Node thisNode = root;
        //parse through each letter in the word
        for (int i = 0; i < word.length(); i++) {
            //for each letter, convert to an index.
            char letter;
            letter = word.charAt(i);
            int index = letter - 'a';
            // if node's child is null in that index, create a child node in that index, move to that index (increment node count)
            Node child = thisNode.getChildren()[index];
            if (child == null) {
                thisNode.getChildren()[index] = new Node();
                nodeCount = nodeCount + 1;
            }
            //if node's child is NOT null in that index, move to that index
            thisNode = thisNode.getChildren()[index];
            //if location in recursion == end of word, increment word count
            if ((i+1) == word.length()) {
                thisNode.incrementValue();
                wordCount = wordCount + 1;
            }
        }
    }

    @Override
    public INode find(String word) {
        //start at root
        Node thisNode = root;
        //recurse through each letter in the word
        for (int j = 0; j < word.length(); j++) {
            //convert letter to index
            char letter;
            letter = word.charAt(j);
            int index = letter - 'a';
            //if thisNode has null at index, return null
            Node child = thisNode.getChildren()[index];
            if (child == null) {
                return null;
            }
            //if thisNode has child at index, move to thisNode = child and continue with next letter
            thisNode = thisNode.getChildren()[index];
            //when at final letter, if wordCount > 0, return thisNode. Else, return null
        }
        if (thisNode.getValue() > 0) {
            return thisNode;
        }
        else {
            return null;
        }
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {

        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();

        toStringHelper(root, curWord, output);
        return output.toString();
    }

    private void toStringHelper(INode n, StringBuilder curWord, StringBuilder output) {

        if (n.getValue() > 0) {
            //append node's word to output
            output.append(curWord.toString());
            output.append("\n");
        }

        for (int i = 0; i < 26; i++) {
            INode child = n.getChildren()[i];
            if (child != null) {

                char childLetter = (char)('a'+1);
                curWord.append(childLetter);

                toStringHelper(child, curWord, output);

                curWord.deleteCharAt(curWord.length() - 1);
            }
        }

    }

    @Override
    public boolean equals(Object o) {

        // check if o is null (return false)
        if (o == null) {
            return false;
        }
        //check if o is equal to this (return true)
        if (o == this) {
            return true;
        }
        //check if this and o have same class (if not, return false)
        if (o.getClass() != this.getClass()) {
            return false;
        }

        Trie d = (Trie)o;

        //check if this and d have same word/node counts (if not, return false)
        if (d.getNodeCount() != this.getNodeCount()) {
            return false;
        }
        if (d.getWordCount() != this.getWordCount()) {
            return false;
        }

        return equalsHelper(this.root, d.root);

    }

    private boolean equalsHelper(Node n1, Node n2) {

        //check if n1 and n2 have same count (if not, return false)
        if (n1.getValue() != n2.getValue()) {
            return false;
        }
        //check if n1 and n2 both have non-null children in same indexes (if not, return false)
        Vector n1Nulls = new Vector();
        Vector n2Nulls = new Vector();
        for (int i = 0; i < 26; i++) {

            Node child1 = n1.getChildren()[i];
            Node child2 = n2.getChildren()[i];
            if (child1 != null) {
                n1Nulls.add(i);
            }
            if (child2 != null) {
                n2Nulls.add(i);
            }
        }
        if (n1Nulls != n2Nulls) {
            return false;
        }

        //recurse on children, compare child subtrees
        for (int j = 0; j < 26; j++) {

            Node child1Recurse = n1.getChildren()[j];
            Node child2Recurse = n2.getChildren()[j];

            if (child1Recurse != null && child2Recurse != null) {
                return equalsHelper(child1Recurse, child2Recurse);
            }
        }
        return true;
    }


    @Override
    public int hashCode() {

        int multiplied = 1;

        for (int k = 0; k < 26; k++) {
            Node rootChild = root.getChildren()[k];
            if (rootChild != null) {
                multiplied = multiplied * k;
            }
        }

        int calculatedCode = wordCount * nodeCount * multiplied;
        return calculatedCode;
    }
}
