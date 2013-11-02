/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intermediate;

import backend.Executor;
import frontend.Scheme_Parser;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 *
 * @author jamie
 */
public class ParseTree {

    Scheme_Parser parser;
    int index;
    public ArrayList<Node> roots;
    public Executor executor;
    public ArrayList<String> tokens;
    
    public ParseTree(Scheme_Parser parser, ArrayList<String> tokens)
    {
        this.parser = parser;
        roots = new ArrayList<Node>();
        index = 0;
        this.tokens = tokens;
        executor = new Executor(this.parser, this);        
    }
    
    public void buildSelf()
    {
        int size = tokens.size();
        int start = 0;
        int end = 0;
        while(index < size)
            this.buildTree(tokens, start, end);
    }
    
    
    
    /**
     * @param tokens
     * @param current
     * @param i
     * @return 
     */
    public void buildTree(ArrayList<String> tokens, int start, int end)
    {
        Node root = new Node();
        
        root = new Node();
        Node current = root;
        int level = 0;
        
        root.flag = true; //any node with flag = true means beginning of a list
        int size = tokens.size();
        boolean letFlag = false;
        boolean lambdaFlag = false;
        boolean letStarFlag = false;
        
        for (; index < size; index++)
        {   
            String token = tokens.get(index);
            //String token = tokens.get(i);
            Node parent;

            if (token.equals("(")) //probably need to flag this node somehow
            {
                //skip this step for beginning of first list
                if(current == root) continue;
                
                current.flag = true;//mark this flag as beginning of a new list
                current.leftChild = new Node();
//              
                current.leftChild.parent = current;
//              
                current = current.leftChild;
            }
            else if(token.equals(")"))
            {
                while(!current.flag)
                    current = current.parent;
                
                current.flag  = false;
                if(current.parent == null)//if reach end of top level list
                {
                    ++index;
                    ArrayList tok = subArray(tokens, start, index);
                    this.parser.buildSymTab(tok);
                    int index = this.parser.symbolTables.size() - 1;
                    System.out.printf("\n********PRINTING SYMBOL TABLE********\n");
                    executor.printSymTab(index);
                    System.out.println("\n********PRINTING TREE********\n");
                    executor.printTree(this, current);
                    this.parser.clearIntCode(current);
                    this.parser.clearSymTab(index);
                    
                    return;
                }
                if(index+1 < size)
                {
                    if(!tokens.get(index + 1).equals(")"))
                    {
                        current.rightChild = new Node();
                        current.rightChild.parent = current;
                        current = current.rightChild;
                    }  
                }
            }
            else 
            {
                //if token is a word
                                
                //create left node and place token there
                //then create right node and make it current node
                current.leftChild = new Node();
                current.leftChild.token = token;
                //current.leftChild.link = getLastSymTab();
                current.leftChild.parent = current;
                
                //THEN...
                //check if the last word was define 
                //OR 
                //if next symbol is lambda, let, or let*
             
                if (current.parent != null && current.parent.leftChild.toString() != null) 
                {
                    String lastToken = current.parent.leftChild.token;
                    String nextToken = current.leftChild.token;

                    //if last token == define
                    if (belongsTopLevel(lastToken)) 
                    {
                        //add current token to top level sym tab
                        addToTopLevelSymTab(token, current.leftChild);
                    } 
                    //if next token is lambda
                    else if (nextLambda(nextToken)) 
                    {
                        //link the last entry in the previous symtab to this node
                        linkSym2Node(level , current);
                        
                        //create new symbol table and put it on the stack
                        //and get which level the new symTab is at on the stack
                        level = createSymTab();
                        
                        //add link from parent of lambda to symTab for vars of
                        //the lambda function
                        linkNode2Sym(level , current);
                        lambdaFlag = true;
                        //expect a non-nested list next
                        
                        
                        //NOW...
                        /*
                         * I think we just need to add the following symbols 
                         * into the symbolTable at level "level" on the symTab
                         * stack.
                         * 
                         * THEN
                         * When we see a ")" we decriment the level
                         */
                        
                    }
                    else if(nextLet(nextToken))
                    {
                        //create new symbol table on the stack
                        level = createSymTab();
                        //add link from parent of let/let* to symTab for vars of
                        //the let
                        linkNode2Sym(level , current);
                        letFlag = true;
                        /*
                         * I think after a "let" you only add the next symbol
                         * to the current symbol table
                         * 
                         * and for "let*" you add as many symbols as there are...
                         * 
                         * but how do you stop adding symbols to a table?
                         * 
                         * some kind of check must be needed before adding to 
                         * any symbol table above the top level... 
                         * I think...
                         */
                    }
                    else
                    {
                        //add the node to the current symbol table
                        //if last node was lambda or let
                        //OR
                        //if you're still within the scope of a let*
                        add2CurrentSym(level, current.leftChild);
                    }
                        
                }
                
                

                if((index < tokens.size() - 1)&&(tokens.get(index+1)).equals(")"))
                    continue;
                
                //before doing this, check if next token is ")"
                //if it is, don't do this!
                //instead travers back up until you see the flagged node
                current.rightChild = new Node();
                current.rightChild.parent = current;
                current = current.rightChild;
            }
        }
    }

    


    private ArrayList<String> 
            subArray(ArrayList<String> tokens,int start, int end)
    {
        ArrayList tok = new ArrayList(end - start);
        for(int i = 0; i < index; i++)
            tok.add(tokens.get(start++));
        return tok;
    }
    
    /**
     * Checks if a new symbol table needs to be created based on the input
     * string.
     * Also indicates that there should be a non nested list after this token
     * @param token
     * @return true if token is lambda
     */
    private boolean nextLambda(String token)
    {
      if(token.equals("lambda"))
        return true;
      return false;
    }
    
    /**
     * Checks if a new symbol table needs to be created based on the input
     * string.
     * Also indicates that there should be a double nested list after this token
     * @param token
     * @return true if token is let or let*
     */
    private boolean nextLet(String token)
    {
      if(token.equals("let") || token.equals("let*"))
        return true;
      return false;
    }
    
    /**
     * Checks if the next tokens belong in the top level symbol table.
     * This should be true if this word is "define".
     * @param token
     * @return 
     */
    private boolean belongsTopLevel(String token)
    {
        if(token != null && token.equals("define"))
            return true;
        return false;
    }
    
    /**
     * Returns TreeMap at the top of Symbol Table Stack
     * @return 
     */
    private TreeMap getLastSymTab()
    {
        int lastIndex = parser.getSymStack().size() - 1;
        return parser.getSymStack().get(lastIndex);
    }
    
    /**
     * Creates a symbol table and adds it to the Symbol Table Stack
     * @return index level of the new Symbol Table
     */
    private int createSymTab()
    {
        TreeMap symTab = new TreeMap();
        parser.getSymStack().add(symTab);
        return parser.getSymStack().size() - 1;
    }
    
    /**
     * Adds an entry to the top level Symbol Table
     * Entry is given link to a node 
     * (which should be parent of lambda or variable itself)
     * Entry is also given the level of symbol table it is on
     * @param token 
     */
    private void addToTopLevelSymTab(String token, Node node)
    {
        TreeMap top = parser.getSymStack().get(0);
        node.links.add(top);
        SymbolEntry entry = new SymbolEntry(token, null);
        entry.attributes.put("type", "definition");
        entry.attributes.put("Node", node);
        entry.attributes.put("level", 0);
        top.put(token, entry);
    }
    
    /**
     * adds a reference (link) to top level symbol table from the given node
     * @param node 
     */
    private void linkToTop(Node node)
    {
        node.links.add(parser.getSymStack().get(0));
    }
    
    /**
     * recursively gets the last keyword from the tree.
     * @param node
     * @return 
     */
    private String getLastKeyWord(Node node)
    {
        String last = "";
        if(node.leftChild.token != null && !isKeyWord(node.leftChild.token))
        {
            Node parent = node.parent;
            last = getLastKeyWord(parent);
        }
        return last;
    }
    
    private boolean isKeyWord(String token)
    {
        ArrayList keys = new ArrayList(4);
        
        keys.add("define");
        keys.add("lambda");
        keys.add("let");
        keys.add("let*");
        
        if(keys.contains(token))
            return true;
        return false;
    }
    
    private void addToSymTab(String token, Node node, int level)
    {
        SymbolEntry entry = new SymbolEntry(token, null);
        entry.attributes.put("type", "vars");//type of entry
        entry.attributes.put("Node", node);//the node being added
        entry.attributes.put("level", level);//the symbol table in the stack
        
        parser.getSymStack().get(level).put(token, entry);
        
    }
    
    /**
     * Gets the last entry out of the symbol table selected by "level" and adds
     * a link to the given node.
     * Should be used for linking the parent of a "lambda" nod to the name that
     * defines the function (like proc or func in the example)
     * @param level
     * @param node 
     */
    private void linkSym2Node(int level, Node node)
    {
        TreeMap symTab = parser.getSymStack().get(level);
        SymbolEntry entry = (SymbolEntry) symTab.lastEntry().getValue();
        entry.attributes.put("nodeLink", node);
    }
    
    private void linkNode2Sym(int level, Node node)
    {
        TreeMap symTab = parser.getSymStack().get(level);
        node.links.add(symTab);
    }
    
    /**
     * adds a node to a symbol table at the given level on the stack
     * @param level
     * @param node 
     */
    private void add2CurrentSym(int level, Node node)
    {
        TreeMap symTab = parser.getSymStack().get(level);
        Hashtable atrb = new Hashtable();
        atrb.put("type", "symbol");
        atrb.put("Node", node);
        atrb.put("level", level);
        SymbolEntry entry = new SymbolEntry(node.token, atrb);
        
        parser.getSymStack().get(level).put(node.token, entry);
    }

}