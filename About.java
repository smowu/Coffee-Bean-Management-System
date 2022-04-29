public class About{
    public static void dispAbout(){
        About about = new About();
        Display aboutScreen = 
        new Display("About", about.toString() + "\n", null, 5);
        aboutScreen.display();
        switch(Display.getInput()){
            default: break;
        }
    }
    public String toString(){
        return 
        String.format("\n" +
                      "\n Developed by " +
                      "\n " + Display.minusBar(12) +
                      "\n Group A4CS1103A" +
                      "\n Durrani Afiq Bin Saidin    (2020769853)" +
                      "\n Luqman Agus Bin Salim      (2020702587)\n");                     
    }
} 