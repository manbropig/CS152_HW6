/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import frontend.Scheme_Parser;
import intermediate.ParseTree;
import intermediate.Node;
import intermediate.SymbolEntry;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author jamie
 */
public class Executor {
    
    ParseTree tree;
    Scheme_Parser parser;
    public Executor(Scheme_Parser parser, ParseTree tree)
    {
        this.parser = parser;
        this.tree = tree;
    }
    
    //this is the updated code in the executer class
    public void printTree(ParseTree tree, Node n) {
        if (n.parent == null)//if n is a root node
        {
            System.out.print("(");
        }

        if (n.leftChild != null && n.leftChild.leftChild != null) {
            //System.out.println("im in");
            System.out.println();
            System.out.printf("\t(");
            printTree(tree, n.leftChild);
            if (n.rightChild != null) {
                printTree(tree, n.rightChild);
            } else {
                System.out.print(")");
            }

        }
        if (n.leftChild != null && n.leftChild.leftChild == null) {
            if (n.rightChild != null) {
                if (n.leftChild.token == null) {
                    System.out.print("() ");
                } else {
                    System.out.print(n.leftChild.token + " ");
                }
                printTree(tree, n.rightChild);
            } else {
                if (n.leftChild.token == null) {
                    System.out.print("() ");
                } else {
                    System.out.print(n.leftChild.token);
                }
                System.out.print(")");


            }
        }
    }

    public void printSymTab(int index) {
            for (Iterator it = this.parser.symbolTables.get(index).entrySet().iterator(); it.hasNext();) {
            Entry<String, SymbolEntry> entry = (Entry<String, SymbolEntry>) it.next();
            String key = entry.getKey();
            SymbolEntry value = entry.getValue();
            System.out.printf("%s\n", value.toString());
        }
    }
   
}
