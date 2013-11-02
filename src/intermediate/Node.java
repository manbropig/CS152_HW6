/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intermediate;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author jamie
 */
public class Node 
{
    public Node leftChild;
    public Node rightChild;
    public Node parent;
    public String token;
    public ArrayList<TreeMap> links;
    public boolean flag;
    public Node ()
    {
        links = new ArrayList<TreeMap>(1);
        this.flag = false;
    }
    public Node(String tok) 
    {
        links = new ArrayList<TreeMap>(1);
        this.token = tok;
    }

    public String toString() {

        if(leftChild == null && rightChild == null)
            return token;
        else
            return "--";

    }
}