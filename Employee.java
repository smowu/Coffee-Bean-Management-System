public abstract class Employee{
    private Log log;
    private String position;
    private String idNumber;
    private String password;
    private String name;
    private String ic;
    private String email;
    
    public Employee(){}
    public Employee(String id, String pass, String n, String ic, String email){
        setPosition();
        this.idNumber = id;
        this.password = pass;
        this.name = n;
        this.ic = ic;
        this.email = email;
    }
    
    public void setLog(Log log){this.log = log;}
    public void setPosition(){
        if(this instanceof Cashier)
            this.position = "Cashier";
        else if(this instanceof Manager)
            this.position = "Manager";
    }
    public void setIdNumber(String id){this.idNumber = id;}
    public void setPassword(String pass){this.password = pass;}
    public void setName(String name){this.name = name;}
    public void setIC(String ic){this.ic = ic;}
    public void setEmail(String email){this.email = email;}
    
    public Log getLog(){return log;}
    public String getPosition(){return position;}
    public String getIdNumber(){return idNumber;}
    public String getPassword(){return password;}
    public String getName(){return name;}
    public String getIC(){return ic;}
    public String getEmail(){return email;}
    
    public void clearEmployee(){
        log = null;
        position = null;
        idNumber = null;
        password = null;
        name = null;
        ic = null;
        email = null;
    }
    
    public String dispEmployeeSummary(){
        return "\n WELCOME BACK " + name + "!" +
               "\n" + Display.equalBar(60) +
               "\n ID      : " + idNumber + 
               "\n Name    : " + name +
               "\n";
    }
    
    public String toString(){
        return 
        String.format("\n ID Number           : %s" +
                      "\n Position            : %s" +
                      "\n Name                : %s" +
                      "\n IC Number           : %s" +
                      "\n Email Address       : %s",
                      idNumber, position, name, ic, email);
    }
    
    public abstract void dispEmployeeMenu();
    public abstract String employeeData();
    
    public void dispEmployeeProfile(){
        Display dispProfile = 
        new Display("Your Profile", toString() + "\n", null,5);
        dispProfile.display();
        switch(Display.getInput()){
            default: break;
        }
    }
} 