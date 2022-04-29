import java.util.*;
import java.util.StringTokenizer;
import java.io.*;

public class Log{
    private Employee employee;
    private String tempID;
    private String tempPass;
    private String idNumber;
    private String password;
    private String name;
    private String ic;
    private String email;
    private int empOrders;
    private double empSales;
    private boolean userFound;
    private boolean retry;
    private boolean logout;
    
    public Employee getEmployee(){return employee;}
    public boolean isUserFound(){return userFound;}

    public void login(){
        do{
            retry = false;
            Scanner in = new Scanner(System.in);

            Display loginMenu = 
            new Display("Log In | For Demo, ID: C0001 or M0001 Pass: 123456", 
                        null, null,0);
            loginMenu.display();
            
            System.out.print("\n ID Number: ");
            tempID = in.nextLine();
            System.out.print(" Password : ");
            tempPass = in.nextLine();
            findEmployee(tempID,tempPass);

            if(userFound)
                verifyEmployee();
            else
                failedLogin();
        }while(retry);
    }
    
    public void findEmployee(String tempID, String tempPass){
        String filepath = "EmployeeList.txt";
        try{
            BufferedReader readFile = new BufferedReader(
                                      new FileReader(filepath));
            String input;
            for(int i = -2; 
                ((input = readFile.readLine()) != null) && !userFound; i++){

                StringTokenizer st = new StringTokenizer(input, "|");
                if(i >= 0 && st.hasMoreTokens()){
                    idNumber = st.nextToken();
                    password = st.nextToken();
                    if(idNumber.equals(tempID) && password.equals(tempPass)){
                        userFound = true;
                        name = st.nextToken();
                        ic = st.nextToken();
                        email = st.nextToken();
                        if(idNumber.charAt(0) == 'C'){
                            empOrders = Integer.parseInt(st.nextToken());
                            empSales = Double.parseDouble(st.nextToken());
                        }
                    }
                } 
            }
            readFile.close();
        }catch(FileNotFoundException e){
            Display.message("File not found!"+"\nError: "+ e,2);
        }catch(IOException ioe){
            Display.message("Failed to read file!"+"\nError: "+ ioe,2);
        }
    }
    
    public void verifyEmployee(){
        if(idNumber.charAt(0) == 'C'){
            employee = 
            new Cashier(idNumber, password, name, ic, email, 
                        empOrders, empSales);
        }else if(idNumber.charAt(0) == 'M')
            employee = new Manager(idNumber, password, name, ic, email);
        else
            userFound = false;
        employee.setLog(this);
        
        System.out.println("\fSuccessfully logged in!\n");
        System.out.print("Please wait");
        Display.fakeLoadTime(1,4);
    }
    public void failedLogin(){
        boolean exit = false;
        do{
            String[] selection = {"Retry"};
            Display failedLogin = 
            new Display("Wrong username or password!", null, selection,1);
            failedLogin.display();
            
            switch(Display.getInput()){
                case  "":
                case "1": exit = retry = true;
                          break;
                case "0": retry = false;
                          exit = true; 
                          break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
    }
    
    public boolean logout(){
        boolean exit = false;
        do{
            String[] selection = {"Yes","No"};
            Display logoutMenu = 
            new Display("Are you sure you want to log out?", null, selection,0);
            logoutMenu.display();
            
            switch(Display.getInput()){
                case  "": 
                case "1": System.out.print("\fLoging out");
                          Display.fakeLoadTime(1,4);
                          exit = logout = true; 
                          break;
                case "2": exit = true; break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
        return logout;
    }
} 