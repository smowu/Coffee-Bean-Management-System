public class Cashier extends Employee{
    private int successOrder;
    private double cashierSales;
    
    public Cashier(){super();}
    public Cashier(String id, String pass, String n, String ic, String email, 
                   int orders, double sales){
        super(id, pass, n, ic, email);
        this.successOrder = orders;
        this.cashierSales = sales;
    }
    
    public void setSuccessOrder(int orders){this.successOrder = orders;}
    public void setCashierSales(double sales){this.cashierSales = sales;}
    
    public int getSuccessOrder(){return successOrder;}
    public double getCashierSales(){return cashierSales;}
    
    public boolean isCashierMatch(Cashier cashier){
        if(this.getIdNumber().equals(cashier.getIdNumber()))
            return true;
        else
            return false;
    }
    
    public String toString(){
        return super.toString() +
               String.format("\n" + Display.minusBar(22) +
                             "\n Orders Made         : %d" +
                             "\n Total Sales         : RM%.2f",
                             successOrder, cashierSales);
    }
    
    public String employeeData(){
        return 
        String.format("%s|%s|%s|%s|%s|%d|%.2f|", 
                      getIdNumber(), getPassword(), getName(), 
                      getIC(), getEmail(), successOrder, 
                      cashierSales);
    }
    
    public void clearCashierSales(){
        successOrder = 0;
        cashierSales = 0.00;
    }
    
    public void dispEmployeeMenu(){
        boolean logout = false;
        do{
            String[] selection = {"Create New Order", "View Profile"};
            Display cashierMenu = 
            new Display("Cashier Menu", dispEmployeeSummary(), selection,3);
            cashierMenu.display();
            
            switch(Display.getInput()){
                case  "":
                case "1": Order.createOrder(this);
                          break;
                case "2": dispEmployeeProfile(); 
                          break;
                case "0": logout = getLog().logout(); 
                          break;
                default : Display.invalidMessage(1); break;
            }
        }while(!logout);
    }
    
    public static void dispAllCashiers(){
        boolean exit = false;
        do{
            Cashier[] cashiers = FileHandler.getAllCashiers();
            String[] cashierSelections = new String[128]; 

            for(int i = 0, j = 0; 
                i < cashiers.length && cashiers[i] != null; i++){
                cashierSelections[i] = 
                String.format("%s | %s", cashiers[i].getIdNumber(), 
                                         cashiers[i].getName());
            }
            
            Display cashierMenu = 
            new Display("Select Cashier", null, cashierSelections,1);
            cashierMenu.setAddText("\n      ID    | NAME",1);
            cashierMenu.display();
            
            int select = Display.getIntInput(), max = 0;
            while(cashierSelections[max] != null) 
                max++;
            if(select < 0 || select >= ++max) 
                select = -1;

            switch(select){
                case  -1: Display.invalidMessage(1); break;
                case   0: exit = true; break;
                default : cashiers[--select].dispSelectedCashier();
                          break;
            }
        }while(!exit);
    }
    
    public void dispSelectedCashier(){
        Display selectedCashierMenu = 
        new Display("Cashier | "+getIdNumber()+" | "+getName(), 
                    toString()+"\n", null,5);
        selectedCashierMenu.display();
        switch(Display.getInput()){
            default : break;
        }
    }
} 