public class Payment{
    private double amountToPay;
    private double amountPaid;
    private boolean confirm;
    
    public Payment(){}
    public Payment(double amount){
        this.amountToPay = amount;
        this.amountPaid = 0;
        this.confirm = false;
    }
    
    public void setAmountToPay(double amount){this.amountToPay = amount;}
    public void setAmountPaid(double paid){this.amountPaid = paid;}
    public void setConfirm(boolean confirm){this.confirm = confirm;}
    
    public double getAmountToPay(){return amountToPay;}
    public double getAmountPaid(){return amountPaid;}
    public boolean getConfirm(){return confirm;}
    
    public static Payment choosePayment(double amount){
        Payment payment = new Payment();
        boolean exit = false;
        do{
            String dispAmountToPay = 
            String.format("\n TOTAL : RM %.2f\n", amount);
            String[] selection = {"Credit Card", "Cash"};
            Display makePayment = 
            new Display("Choose Payment Method", dispAmountToPay, selection,1);
            makePayment.display();
            
            switch(Display.getInput()){
                case "1": CreditCard card = new CreditCard(amount);
                          card.makePayment();
                          payment = card;
                          break;
                case "2": Cash cash = new Cash(amount);
                          cash.makePayment();
                          payment = cash;
                          break;
                case "0": exit = true; 
                          break;
                default : Display.invalidMessage(1); break;
            }
            if(payment.confirm){
                Display.message("Successful Payment!",2);
                if(payment instanceof Cash){
                    Cash cash = (Cash)payment;
                    cash.dispBalance();
                }
                exit = true;
            }         
        }while(!exit);
        return payment;
    }
} 