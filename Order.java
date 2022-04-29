import java.lang.Math;

public class Order{
    private Cashier cashier;
    private Time timestamp;
    private String orderID;
    private Cart cart;
    private double serviceTaxPaid;
    private double roundAdj;
    private double totalPrice;
    private Payment payment;
    
    public Order(){}
    public Order(Cashier cashier){
        this.cashier = cashier;
        this.timestamp = new Time();
        this.orderID = generateOrderID();
        this.cart = new Cart();
        this.serviceTaxPaid = 0.00;
        this.roundAdj = 0.00;
        this.totalPrice = 0.00;
    }

    public Cashier getCashier(){return cashier;}
    public Time getTimestamp(){return timestamp;}
    public String getOrderID(){return orderID;}
    public Cart getCart(){return cart;}
    public double getServiceTaxPaid(){return serviceTaxPaid;}
    public double getRoundAdj(){return roundAdj;}
    public double getTotalPrice(){return calcTotalPrice();}
    public Payment getPayment(){return payment;}

    public double calcRoundAdj(){
        double whole = Math.floor(totalPrice * 10);
        double decimal = (totalPrice * 10) - whole;
        decimal /= 10;
        
        roundAdj = Math.round(decimal/0.05) * 0.05; 
        roundAdj -= decimal; 
    
        return roundAdj;
    }
    public double calcTotalPrice(){
        double tax = 0.06;
        double subtotal = cart.getSubTotal();
        
        serviceTaxPaid = Math.round((subtotal * tax)*100.00)/100.00;
        if((serviceTaxPaid*100.00 % 1) >= 5)
            serviceTaxPaid = Math.ceil(serviceTaxPaid*100.00);
        else
            serviceTaxPaid = Math.floor(serviceTaxPaid*100.00);
        serviceTaxPaid /= 100.00;

        totalPrice = subtotal + serviceTaxPaid;
        totalPrice += calcRoundAdj();
        
        return totalPrice;
    }
    
    public String dispOrderSummary(boolean isButton, int mode){
        String summary = 
        String.format("\n Order#: %s"+"%"+38+"s\n"+Display.charBar('=', 60),
                      orderID, "Date: " + timestamp.toString());          
        if(cart.isThereItem()){
            for(int i = 0; !cart.isItemListNull(i); i++){
                if(i == 0) 
                    summary += summaryLabel();

                String num = Integer.toString(i + 1);
                if(isButton) 
                    num = "[" + num + "]";
                summary += itemSummary(i, num);
            }
            summary = setSummaryMode(summary,mode);
        }else 
            summary += "\n No item selected.\n";
        return summary;
    }
    public String setSummaryMode(String summary, int mode){
        if(mode == 0){
            summary = String.format(
            "%s" +
            "\n" + Display.equalBar(60) +
            "\n Total Item Qty : %-" + 22 + "d" + 
            "SUBTOTAL RM%" + 8 + ".2f",
            summary, cart.getTotalItemQty(), cart.getSubTotal());
            
        }else if(mode == 1){
            summary = String.format(
            "%s" +
            "\n" + Display.equalBar(60) +
            "\n " + "%" + 47 + "s"+ "%" + 11 + ".2f" +
            "\n " + "%" + 47 + "s"+ "%" + 11 + ".2f" +
            "\n " + "%" + 47 + "s"+ "%" + 11 + ".2f" +
            "\n" + Display.minusBar(60) +
            "\n Total Item Qty : %-" + 22 + "d" + 
            "TOTAL    RM" + "%" + 8 + ".2f",
            summary, 
            "SubTotal          :", cart.getSubTotal(),
            "Service Tax (6%)  :", serviceTaxPaid,
            "Rounding Adj      :", roundAdj,
            cart.getTotalItemQty(), totalPrice);
        }
        return summary;
    }
    public String itemSummary(int i, String num){
        Item item = cart.getList()[i];
        String name = item.getItemName();
        if(item instanceof Beverage){
            Beverage bev = (Beverage)item;
            name = bev.strItemName();
        }
        if(name.length() > 32)
            name = item.getItemName().substring(0, 32);
        return 
        String.format("\n "+
                      "%"  +  4 + "s "     + // NO
                      "%-" + 33 + "s"      + // ITEM
                      "%"  +  7 + ".2f   " + // PRICE
                      "%"  +  3 + "d "     + // QTY
                      "%"  +  6 + ".2f",     // (RM)
                      num, name, item.getPrice(), item.getItemQty(), 
                      item.getItemTotal());
    }
    public String summaryLabel(){
        return 
        String.format("\n "+ 
                      "%"  +  4 + "s " + 
                      "%-" + 33 + "s"  + 
                      "%"  +  7 + "s"  + 
                      "%"  +  3 + "s"  +
                      "%"  +  7 + "s",
                      "NO", "ITEM", "PRICE", " * QTY", "(RM)");
    }

    public String generateOrderID(){
        Time time = timestamp;
        String id = 
        time.toString().substring(8,10) + time.toString().substring(3,5) + 
        time.toString().substring(0,2) + time.toString().substring(11,13) +
        time.toString().substring(14,16) + time.toString().substring(17,19);
        return id;
    }
    
    public static void createOrder(Cashier cashier){
        Order order = new Order(cashier);
        boolean exit = false, cancel = false;
        do{ 
            String[] selection = {"Add Item", "Edit Cart", "Confirm Order"};
            Display createOrder = 
            new Display("Item Cart", order.dispOrderSummary(false,0)+"\n", 
                        selection,2);
            createOrder.display();
            
            switch(Display.getInput()){
                case "1": Item newItem = Cart.addItem();
                          if(newItem != null)
                              order.cart.addItemToCart(newItem);
                          break;
                case "2": order.editCart(); 
                          break;
                case "3": exit = order.confirmOrder();
                          if(exit){
                              Sales.storeAllSalesData(order);
                              Receipt.confirmPrintReceipt(order);
                          }
                          break;
                case "0": exit = cancel = order.cancelOrder();
                          if(cancel)
                              Display.message("Order Canceled.", 2);
                          break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
    }
    
    public void editCart(){
        if(cart.isThereItem()){
            boolean exit = false;
            do{
                Display cartEditor = 
                new Display("Edit Cart", dispOrderSummary(true,0)+"\n", null,1);
                cartEditor.setAddText("\n Enter the item no. to modify.",2);
                cartEditor.display();
                
                // Determining valid dynamic input range
                int select = Display.getIntInput(), max = 0; 
                while(cart.getList()[max] != null) 
                    max++;
                if(select < 0 || select >= ++max) 
                    select = -1;
                
                Item originalItem = new Item();
                Beverage originalBeverage;
                int index = 0;
                if(select > 0){
                    index = select - 1;
                    originalItem = cart.getList()[index];
                    if(originalItem instanceof Beverage){
                        originalBeverage = (Beverage)originalItem;
                        originalItem = new Beverage(originalBeverage);
                    }else
                        originalItem = new Item(originalItem);
                }
                switch(select){
                    case -1: Display.invalidMessage(1); break;
                    case  0: exit = true; 
                             break;
                    default: boolean isEdited = false;
                             isEdited = cart.editItem(index);
                             if(isEdited)
                                 cart.calcSubTotal();
                             else
                                 cart.setItem(originalItem, index);
                             break;
                }
                if(!cart.isThereItem()) 
                    exit = true;
            }while(!exit);
        }else 
            Display.message("No item selected.", 2);
    }
    
    public boolean confirmOrder(){
        calcTotalPrice();
        boolean confirm = false;
        if(cart.isThereItem()){
            boolean exit = false;
            do{
                String[] selection = {"Confirm"};
                Display confirmOrder = 
                new Display("Review Order", dispOrderSummary(false,1)+"\n", 
                            selection,1);
                confirmOrder.display();
                
                switch(Display.getInput()){
                    case  "": 
                    case "1": payment = Payment.choosePayment(totalPrice);
                              exit = confirm = payment.getConfirm();
                              if(!confirm)
                                  Display.message("Payment Canceled.",2);
                              break;
                    case "0": exit = true; 
                              break;
                    default : Display.invalidMessage(1); break;
                }
            }while(!exit);
        }else 
            Display.message("No item selected.",2);
        return confirm;
    }
    
    public boolean cancelOrder(){
        boolean exit = false, cancel = false;
        do{
            String[] selection = {"Cancel Order"};
            Display cancelOrder = 
            new Display("Are you sure you want to cancel the order?", 
                        null, selection,1);
            cancelOrder.display();
            
            switch(Display.getInput()){
                case  "": 
                case "1": exit = cancel = true; break;
                case "0": exit = true; break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
        return cancel;
    }
} 