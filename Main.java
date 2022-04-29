public class Main{
    public static void main(){
        boolean exit = false;
        do{
            Time currentTime = new Time();
            Log log = new Log();
            
            String[] selection = {"Login", "About Program"};
            Display mainMenu = 
            new Display(currentTime.toString(), menuText(), selection,4);
            mainMenu.display();
            
            switch(Display.getInput()){
                case  "": 
                case "1": log.login();
                          Employee employee = log.getEmployee();
                          if(log.isUserFound())
                              employee.dispEmployeeMenu();
                          break;
                case "2": About.dispAbout(); 
                          break;
                case "0": Display.endProgramMessage();
                          exit = true; 
                          break;
                default : Display.invalidMessage(1); break;
            }   
        }while(!exit);
    }
    
    public static String menuText(){
        return 
        "\n   _____                 _       ____                 _   " +
        "\n  |_   _|_ _ _ __   __ _| |__   |  _ \\ ___   __ _  __| |  " +
        "\n    | |/ _` | '_ \\ / _` | '_ \\  | |_) / _ \\ / _` |/ _` |  " +
        "\n    | | (_| | |_) | (_| | | | | |  _ < (_) | (_| | (_| |  " +
        "\n    |_|\\__,_| .__/ \\__,_|_| |_| |_| \\_\\___/ \\__,_|\\__,_|  " +
        "\n            |_|                                           " +
        "\n    ____       __  __             ____  _                 " + 
        "\n   / ___|___  / _|/ _| ___  ___  / ___|| |__   ___  _ __  " +
        "\n  | |   / _ \\| |_| |_ / _ \\/ _ \\ \\___ \\| '_ \\ / _ \\| '_ \\ " +
        "\n  | |__| (_) |  _|  _|  __/  __/  ___) | | | | (_) | |_) |" +
        "\n   \\____\\___/|_| |_|  \\___|\\___| |____/|_| |_|\\___/| .__/ " +
        "\n                                                   |_|    " +
        "\n";
    } 
} 