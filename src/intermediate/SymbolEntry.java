/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intermediate;

import java.util.Hashtable;

/**
 *
 * @author jamie
 */
public class SymbolEntry {
    String name;
    Hashtable attributes;
    
    //For this assignment, the attributes of each symbol table entry can be null
    public SymbolEntry(String name, Hashtable attrb)
    {
        this.name = name;
        if(attrb == null)
            attributes = new Hashtable();
        else
            attributes = attrb;
    }
    
    @Override
    public String toString()
    {
        return this.name;
    }
}
