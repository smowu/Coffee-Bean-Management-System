public class CreditCard extends Payment{
    private String cardNo;
    private String expiryDate;
    private String cvc;
    
    public CreditCard(double amount){
        super(amount);
    }
    
    public void clearCard(){
        this.cardNo = null;
        this.expiryDate = null;
        this.cvc = null;
    }
   
    public void makePayment(){
        boolean retry = false; 
        do{
            Display creditCard = 
            new Display("Enter Credit Card Information",null,null,0);
            creditCard.display();
            
            System.out.print("\n Card Number (16-Digits) : ");
            cardNo = Display.getInput();
            System.out.print(" Exipry Date     (MM/YY) : ");
            expiryDate = Display.getInput();
            System.out.print(" CVC          (3-Digits) : ");
            cvc = Display.getInput();
            
            retry = validateCard();
        }while(!getConfirm() && retry);
        clearCard();
    }
    
    public boolean validateCard(){
        boolean retry = false;
        if(!isCardNo() || !isExpiryDate() || !isCVC()){
            boolean exit = false;
            do{
                String[] selection = {"Retry"};
                Display invalidCard = 
                new Display("Invalid Card | Wrong format input or Expired Card", 
                            null, selection,2);
                invalidCard.display();
                
                switch(Display.getInput()){
                    case  "":
                    case "1": exit = retry = true; break;
                    case "0": exit = true; break;
                    default : Display.invalidMessage(1); break;
                }
            }while(!exit);
        }else 
            setConfirm(true);
        return retry;
    }
    public boolean isCardNo(){
        if(!Display.isNumeric(cardNo) || cardNo.length() != 16) 
            return false;
        else 
            return true;
    }
    public boolean isExpiryDate(){
        Time current = new Time();
        boolean valid = false;
        boolean format = 
        (expiryDate.length() == 5 && expiryDate.substring(2,3).equals("/"));
        int cardMonth = 0, cardYear = 0;
        int currentMonth = Integer.parseInt(current.getDDMMYY().substring(3,5));
        int currentYear = Integer.parseInt(current.getDDMMYY().substring(8,10));
        
        try{
            cardMonth = Integer.parseInt(expiryDate.substring(0,2));
            cardYear = Integer.parseInt(expiryDate.substring(3,5));
        }catch(Exception e){
            valid = false;
        }
        
        if(currentYear <= cardYear && format){
            if(currentYear == cardYear && currentMonth > cardMonth)
                valid = false;
            else 
                valid = true;
        } 
        return valid;
    }
    public boolean isCVC(){
        if(!Display.isNumeric(cvc) || cvc.length() != 3) 
            return false;
        else 
            return true;
    }
} 