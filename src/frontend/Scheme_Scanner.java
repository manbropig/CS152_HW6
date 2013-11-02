/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 *
 * @author jamie
 * Scanner should print the token string and the token type 
 * (symbol, number, reserved word, etc.), one token per output line. 
 * It should ignore comments.
 */
public class Scheme_Scanner 
{

    File file;
    Hashtable reserved;
    Hashtable dontAdd;
    static int index;
   
    @SuppressWarnings("empty-statement")
    public Scheme_Scanner(File file)
    {
        this.file = file;
        String [] res = {"and","begin", "cond", "define", "else", "if", 
            "lambda", "let","letrec", "let*", "not", "or", "quote", "'"};
        
        String [] noAdd = {"(",")", "#", "[", "]", "{", 
            "}", ";",",", "\"", "'"};
       //System.out.println(res[res.length-1]);
        
        reserved = new Hashtable();
        for(int r=0; r < res.length; r++)
        {
            reserved.put(res[r], true);
        }
        
        for(int a = 0; a < noAdd.length; a++)
        {
            dontAdd = new Hashtable();
            dontAdd.put(noAdd[a], true);
        }
        
        //reserved = res;
        index = 0;
    }
    
    /**
     * Reads file into a string for parsing
     * @param file
     * @return
     * @throws FileNotFoundException 
     */
    public String getFile(File file) throws FileNotFoundException
    {
        //reads until the end of file
        String content = new Scanner(file).useDelimiter("\\Z").next();
        //System.out.println(content);
        
        return content;
    }
    
    
    private static String skipWhiteSpace(char[] ary)
    {
        
            
        //skip all whitespace
        while(isWhite(ary[index]))
        {
            Scheme_Scanner.index++;
            if(index >= ary.length)
                return getNextTokenIndex(ary);
        }
        
        return getNextTokenIndex(ary);
        
        //return index;
    }
    
    private static String skipComments( char[] ary)
    {
        if(ary[index] == ';')
        {
            while(ary[index] != '\n')
            {
                index++;
                if(index >= ary.length)
                    return getNextTokenIndex(ary);
            }
        }
        
        return getNextTokenIndex(ary);
    }
    
    public static boolean isWhite(char c)
    {
        if (c ==' ' || c== '\n' )
        {
            return true;
        }
        else
            return false;
    }
    
    private static boolean isLetter(char c)
    {
        String s = "" + c;
        if(s.matches("^[A-Za-z]"))
            return true;
        else
            return false;
    }
    
    private static boolean isDigit(char c)
    {
        String s = "" + c;
        if(s.matches("^[0-9]"))
            return true;
        else
            return false;
    }
    private static boolean isSymbol(char c)
    {
        if((c == '-') || (c == '?')|| (c == '*'))
            return true;
        else
            return false;
    }
    
    private static boolean isListPart(char c) 
    {
        String s = "" + c;
        if(c == '('|| c == ')')
            return true;
        else 
            return false;
    }
    
    
    private static boolean isWordPart( char[] ary)
    {
        if(index >= ary.length)
            return false;
        if((isLetter(ary[index]))
                ||(isDigit(ary[index]))
                ||(isSymbol(ary[index])))
            return true;
        else
            return false;
        
    }
    
    private static boolean isQuote(char c) 
    {
        if(c == '\'')
            return true;
        else
            return false;
    }
    
    private static boolean isSpecialSymbol(char c) 
    {
        String s = c + "";
        if(s.matches("^[+*-]"))
            return true;
        else
            return false;
    }
    
    private static boolean isBoolOrCharStart(char c)
    {
        if(c == '#')
            return true;
        else
            return false;
    }
    
    
    private static boolean isStringStart(char c)
    {
        if(c == '"')
            return true;
        else 
            return false;
    }
    
    private static boolean isOther(char c) 
    {
        String s = c + "";
        if(!s.matches("^[a-zA-Z0-9,?/()]"))
            return true;
        else
            return false;
    }
    
    
    
    
 /***************** STRING METHODS *****************/
    public boolean isReserved(String t)
    {
        if(reserved.containsKey(t))
            return true;
        else 
            return false;
    }
    
    //NEED TO ALLOW ONE "." IN A NUMBER STRING
    public static boolean isNumber(String t)
    {
        char start = t.charAt(0);
        if(isDigit(start))
            return true;
        else
            return false;
        
    }
    
    public boolean isSpecial(String t)
    {
        if(dontAdd.contains(t))
            return true;
        else
            return false;
    }
    
    private static boolean isWord(String t)
    {
        if(isLetter(t.charAt(0)))
            return true;
        else
            return false;
    }
    
    public static boolean isListStart(String s)
    {
        if(s.equals("("))
            return true;
        else 
            return false;
    }
    
    public static boolean isListEnd(String s)
    {
        if(s.equals(")"))
            return true;
        else 
            return false;
    }
    
    public static boolean isBool(String s)
    {
        if(s.equals("#t") || s.equals("#f"))
            return true;
        else
            return false;
    }
    
    private static boolean isString(String s)
    {
        char c = s.charAt(0);
        if(c == '"')
            return true;
        else
            return false;
    }
    
    public static boolean isChar(String s)
    {
        if(s.charAt(0) == '#' && s.charAt(1) == '\\')
            return true;
        else 
            return false;
    }
    
/***************** STRING METHODS END *****************/
    
    
    
    public static String getNextTokenIndex( char[] ary)
    {
        if(index >= ary.length)
            return "";
        char c = ary[index];
        if(isWhite(c))
            return skipWhiteSpace(ary);
        else
        if(c == ';')
            return skipComments(ary);
        
        String token = "";
        if(isLetter(c))
        {
            while(isWordPart(ary))
            {
                token += ary[index++];
            }
        }
        else if(isDigit(c))
        {
            while(isDigit(ary[index]))
            {
                token += ary[index++] + "";            
            }
        }
        else if(isBoolOrCharStart(c))
        {
            //if its a boolean true
            if(ary[index+1] == 't')
            {
                token = "#t";
                index++;
                index++;
            }
            //if its a boolean false
            else if(ary[index+1] == 'f')
            {
                token = "#f";
                index++;
                index++;
            }
            //if its a character : #\ means character in scheme
            else if(ary[index+1] == '\\')
            {
                token +=ary[index++] + "";//get #
                token +=ary[index++] + "";//get \
                token +=ary[index++] + "";//get character
            }
            else
                //this would mean there is a syntax error
                token = ary[index++] + "";
        }
        else if(isStringStart(c))
        {
            do
            {
                token += ary[index++] + "";
            }
            while(!isStringStart(ary[index]));
            
            token += ary[index++] + "";//get the ending quote 
        }
        else if(isListPart(c) || isQuote(c) || isSpecialSymbol(c) || isOther(c))
        {
            token = ary[index++] + "";//move index to next character
        }
        
        
        return token;
    }
    
    
    /**
     * Collects all tokens into an ArrayList
     * @param lines
     * @return 
     */
    public ArrayList<String> gatherTokens(char[] lines)
    {
        ArrayList<String> tokens = new ArrayList();
        for(int j = 0; j < lines.length; j = index)
        {
            String output = getNextTokenIndex(lines);
            if(!output.equals(""))
                tokens.add(output);
            //System.out.println(index + ": " + output);
        }
        return tokens;
    }
    
    /**
     * Prints each token along with their type
     * @param tokens 
     */
    public void printTokens(ArrayList tokens) {
        int size = tokens.size();
        for (int i = 0; i < size; i++) {
            String token = (String) tokens.get(i);

            //check if EOF
            if(token.equals(""))
                return;
            //check if token is reserved word
            else if (isReserved(token)) {
                System.out.printf(token + "\t: Reserved word.\n");
            } //check if boolean
            else if (isBool(token)) {
                System.out.printf(token + "\t: Boolean\n");
            } //check if token is number
            else if (isNumber(token)) {
                System.out.printf(token + "\t: Number\n");
            } //check if token is special character
            else if (isSpecial(token)) {
                System.out.printf(token + "\t: Special Symbol\n");
            } //check if token is word
            else if (isWord(token)) {
                System.out.printf(token + "\t: Word\n");
            } //check if token is "("
            else if (isListStart(token)) {
                System.out.printf(token + "\t: List start\n");
            } //check if token is ")"
            else if (isListEnd(token)) {
                System.out.printf(token + "\t: List end\n");
            } //check if string
            else if (isString(token)) {
                System.out.printf(token + "\t: String\n");
            }//check if character
            else if(isChar(token)){
                System.out.printf(token + "\t: Character\n");
            }
        }
    }

    
    
}

//scanners job is to pull tokens out of the file.
//parsers job is to take the tokens and figure out what the statements/exprs are that it is parsing.