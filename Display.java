import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

public class Display{
    private String header;
    private String content;
    private String[] selection;
    private int exit;
    private String addText;
    private int textType;
    
    public Display(String header, String cont, String[] selection, int exit){
        this.header = header;
        this.content = cont;
        this.selection = selection;
        this.exit = exit;
    }

    public void setHeader(String header){this.header = header;}
    public void setContent(String cont){this.content = cont;}
    public void setSelection(String[] selection){this.selection = selection;}
    public void setAddText(String text, int type){
        this.addText = text;
        this.textType = type;
    }
    
    public void dispHeader(){
        System.out.println(" [" + header + "]");
    }
    public void dispSelections(){
        int i;
        for(i = 0; i < selection.length && selection[i] != null; i++){
            String button = "[" + (i + 1) + "]";
            System.out.print(
            String.format("\n " + "%" + 4 + "s %s", button, selection[i]));
        }
        System.out.print('\n');
    }
    
    public void display(){
        System.out.print('\f');
        if(header != null)
            dispHeader();
            
        if(content != null){
            System.out.print(hashBar(60));
            System.out.print(content);
        }
        if(selection != null){ 
            System.out.print(hashBar(60));
            if(addText != null && textType == 1){
                System.out.println(addText);
                System.out.print(equalBar(60));
            }
            dispSelections();
        }else
            System.out.print(hashBar(60));
        if(addText != null && textType == 2)
            System.out.println(addText);
            
        if(exit != 0){
            String text = "";
            if(exit == 1) 
                text = "BACK";
            else if(exit == 2) 
                text = "CANCEL";
            else if(exit == 3) 
                text = "LOG OUT";
            else if(exit == 4) 
                text = "EXIT PROGRAM";
            else if(exit == 5) 
                text = "OK";
            if(exit != 5) 
                System.out.print(minusBar(60));
            System.out.println("\n  [0] " + text);
        }
    }
    
    public static void message(String message, int timer){
        System.out.print("\f" + message);
        setTimer(timer);
    }
    
    public static void invalidMessage(int messageType){
        if(messageType == 1) 
            System.out.print("\fInvalid selection!");
        else 
            System.out.print("\fInvalid input!");
        setTimer(1);
    }
    public static void endProgramMessage(){
        System.out.print('\f');
        message("The program has been terminated.",3);
        System.out.print('\f');
    }
    
    public static void fakeLoadTime(int min, int max){
        int randomNum = ThreadLocalRandom.current().nextInt(min,max);
        for(int i = 0; i < randomNum; i++){
            System.out.print(".");
            setTimer(1);
        }
    }
    public static void setTimer(int time){
        try{
            TimeUnit.SECONDS.sleep(time);
        }catch(Exception e){}
    }

    public static String hashBar(int i){
        return charBar('#', i);
    }
    public static String equalBar(int i){
        return charBar('=', i);
    }
    public static String minusBar(int i){
        return charBar('-', i);
    }
    public static String charBar(char c, int i){
        String bar = "";
        for(int j = 0; j < i; j++) 
            bar += c;
        return bar;
    }
    
    public static String getInput(){
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        return input;
    }
    public static int getIntInput(){
        int input = -1;
        Scanner in = new Scanner(System.in);
        try{
            input = in.nextInt();
        }catch(Exception e){}
        return input;
    }
    
    public static boolean isNumeric(String str){ 
        try{  
            Double.parseDouble(str);  
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
} 