/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import intermediate.Node;
import intermediate.ParseTree;
import intermediate.SymbolEntry;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author jamie
 */
public class Scheme_Parser 
{
    Scheme_Scanner scanner;
    public ArrayList<TreeMap> symbolTables;
    
    public Scheme_Parser(File file)
    {
        scanner = new Scheme_Scanner(file);
        //only enter names into symbol table
        //names = names of identifiers/variables
        //symbolTable = new TreeMap<String, SymbolEntry>();
        symbolTables = new ArrayList<TreeMap>(1);
        TreeMap topLevel = new TreeMap();
        symbolTables.add(topLevel);
    }
    
    //method to enter symbols into symbol table
    //make sure not to enter reserved words
    /**
     * puts all SymbolEntries into symbol table
     * @param tokens 
     */
    public void buildSymTab(ArrayList<String> tokens)
    {
        int size = tokens.size();
        TreeMap symTab = new TreeMap();
        for (int i = 0; i < size; i++) {
            String token = tokens.get(i);
            
            //TODO:
            //if not a reserved word, OR a number,OR boolean, OR ( OR ),
            //OR ANYTHING IN SLIDES 2 - 3 OF 10/24/13 slides
            //add to symbol table
            if(
                    !this.scanner.isReserved(token) && 
                    !this.scanner.isSpecial(token)&&
                    !Scheme_Scanner.isNumber(token) &&
                    !Scheme_Scanner.isBool(token) &&
                    !Scheme_Scanner.isListStart(token) &&
                    !Scheme_Scanner.isListEnd(token) &&
                    !Scheme_Scanner.isChar(token)
                    )
            {
                //later on probably need a method to createa attributes
                
                //attributes are allowed to be null at this point
                SymbolEntry entry = new SymbolEntry(token, null);
                symTab.put(token, entry);
                
            }
        }
        symbolTables.add(symTab);
    }
 
    public void clearIntCode(Node root) 
    {
        root = null;
    }

    public void clearSymTab(int index) 
    {
        symbolTables.remove(index);
        //symbolTables.get(index) = new TreeMap<String, SymbolEntry>();
    }
    
    public ArrayList<TreeMap> getSymStack()
    {
        return this.symbolTables;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        // Replace this file path with the path to input.lisp
        File file = new File("/Users/jamie/School Work/CS 152/CS152_Interpreter/"
                + "input2.lisp");
        
        Scheme_Parser parser = new Scheme_Parser(file);
        
        String data = parser.scanner.getFile(file);
        
        char[] lines = data.toCharArray();//split into array of characters
        
        ArrayList tokens = parser.scanner.gatherTokens(lines);
        parser.scanner.printTokens(tokens);
        
        //parser.buildSymTab(tokens);
        
        ParseTree tree = new ParseTree(parser, tokens);
        tree.buildSelf();
        System.out.println();
    }
}