public class Cash extends Payment{
    private double balance;
    
    public Cash(double amount){
        super(amount);
        this.balance = 0.00;
    }
    
    public double getBalance(){return balance;}

    public void makePayment(){
        double amount = 0.00;
        boolean retry = false; 
        do{
            Display creditCard = 
            new Display("Enter Cash Paid | TOTAL: " + 
                        String.format("RM %.2f", getAmountToPay()),null,null,0);
            creditCard.display();
            
            System.out.print("\n Cash Paid (RM) : ");
            try{
                amount = Double.parseDouble(Display.getInput());
            }catch(NumberFormatException e){
                amount = 0.00;
            }
            setAmountPaid(amount);
            
            retry = validateCash();
        }while(!getConfirm() && retry);
    }
    
    public boolean validateCash(){
        boolean retry = false;
        if(getAmountPaid() < getAmountToPay()){
            boolean exit = false;
            do{
                String[] selection = {"Retry"};
                Display invalid = new Display("Invalid Amount!", null, 
                                              selection,2);
                invalid.display();
                
                switch(Display.getInput()){
                    case  "": 
                    case "1": retry = exit = true; break;
                    case "0": exit = true; break;
                    default : Display.invalidMessage(1); break;
                }
            }while(!exit);
        }else{
            balance = getAmountPaid() - getAmountToPay();
            setConfirm(true);
        }
        return retry;
    }
    
    public void dispBalance(){
        String showBalance = String.format("\n Balance : RM %.2f", balance);
        Display dispBalance = 
        new Display("Successful Cash Payment", null, null,5);
        dispBalance.setAddText(showBalance,2);
        dispBalance.display();
        switch(Display.getInput()){
            default: break;
        }
    }
} 