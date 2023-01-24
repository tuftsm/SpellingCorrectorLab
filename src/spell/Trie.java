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
        nodeCount = 1;
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
                if (thisNode.getValue() == 0) {
                    wordCount = wordCount + 1;
                }
                thisNode.incrementValue();
//                wordCount = wordCount + 1;
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

                char childLetter = (char)('a'+i);
                curWord.append(childLetter);

                toStringHelper(child, curWord, output);

                curWord.deleteCharAt(curWord.length() - 1);
            }
        }

    }

    @Override
    public boolean equals(Object o) {

        // check if o is null (return false)
        System.out.println("Made it here a" + o.toString());
        if (o == null) {
//            System.out.println("Made it here b");
            return false;
        }
//        check if o is equal to this (return true)
        if (o == this) {
//            System.out.println("Made it here c");
            return true;
        }
        //check if this and o have same class (if not, return false)
        if (o.getClass() != this.getClass()) {
//            System.out.println("Made it here d");
            return false;
        }

        Trie d = (Trie)o;
//        System.out.println("Made it here e");
        //check if this and d have same word/node counts (if not, return false)
        if (d.getNodeCount() != this.getNodeCount()) {
//            System.out.println("Made it here f");
            return false;
        }
        if (d.getWordCount() != this.getWordCount()) {
//            System.out.println("Made it here g");
            return false;
        }
        System.out.println("Made it here h" + "\n" + this.toString() + d.toString());
        return equalsHelper(this.root, d.root);

    }

    private boolean equalsHelper(Node n1, Node n2) {
//        System.out.println("Made it here i");
        //check if n1 and n2 have same count (if not, return false)
        if (n1.getValue() != n2.getValue()) {
//            System.out.println("Made it here j");
            return false;
        }
        //check if n1 and n2 both have non-null children in same indexes (if not, return false)
        Vector n1Nulls = new Vector();
        Vector n2Nulls = new Vector();
        for (int i = 0; i < 26; i++) {
//            System.out.println("Made it here k");

            Node child1 = n1.getChildren()[i];
            Node child2 = n2.getChildren()[i];
            if (child1 != null) {
                System.out.println("Made it here k1 index: " + i);
                n1Nulls.add(i);
            }
            if (child2 != null) {
                System.out.println("Made it here k2 index: " + i);
                n2Nulls.add(i);
            }
        }
        boolean equalArrays = n1Nulls.equals(n2Nulls);
        System.out.println("Made it here k2.5...1:" + n1Nulls + " 2: " + n2Nulls);
        if (!equalArrays) {
//            System.out.println("Made it here k3...1: " + n1Nulls + " 2: " + n2Nulls);
            return false;
        }

        //recurse on children, compare child subtrees
        for (int j = 0; j < 26; j++) {
            System.out.println("Made it here l index: " + j);
            Node child1Recurse = n1.getChildren()[j];
            Node child2Recurse = n2.getChildren()[j];

            if (child1Recurse != null && child2Recurse != null) {
                System.out.println("Made it here m with index: " + j);
                if(!equalsHelper(child1Recurse, child2Recurse)) {
                    return false;
                }
            }
//            System.out.println("Made it here n");
        }
//        System.out.println("Made it here o");
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
