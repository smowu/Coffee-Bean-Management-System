public class Receipt{
    public static void confirmPrintReceipt(Order order){
        boolean exit = false;
        do{
            String[] selection = {"Yes", "No"};
            Display printReceipt = 
            new Display("Do you want to print receipt?", null, selection,0);
            printReceipt.display();
            
            switch(Display.getInput()){
                case  "":
                case "1": printReceipt(order);
                          exit = true; break;
                case "2": exit = true; break;
                default : Display.invalidMessage(1); break;
            } 
        }while(!exit);
    }
    public static void printReceipt(Order order){
        System.out.print("\fPrinting Receipt");
        Display.fakeLoadTime(3,5);
        Display dispReceipt = new Display("Receipt", null, null, 5);
        dispReceipt.setAddText(toString(order),2);
        dispReceipt.display();
        switch(Display.getInput()){
            default: break;
        }
    }
    
    public static String toString(Order order){
        String strCash = "";
        if(order.getPayment() instanceof Cash){
            Cash cash = (Cash)order.getPayment();
            strCash = 
            String.format("\n  Cash              :" + "%" + 20 + ".2f" +
                          "\n  Change            :" + "%" + 20 + ".2f",
                          cash.getAmountPaid(), cash.getBalance());
        }
        return String.format(
        "\n" +
        "\n        TAPAH ROAD COFFEE  SDN BHD        " +
        "\n         COMPANY REGIS: XXXXXXX-X         " +
        "\n           LOT 19, JALAN STESEN,          " +
        "\n       35000 TAPAH, PERAK, MALAYSIA       " +
        "\n             TEL : XX-XXXXXXXX            " +
        "\n" +
        "\n                Tax Invoice               " +
        "\n" +
        "\n  ReceiptID#: " + order.getOrderID() +
        "\n  Cashier   : " + order.getCashier().getName() +
        "\n  Date      : " + order.getTimestamp() +
        "\n -----------------------------------------" +
        "\n  NO ITEM               QTY * PRICE  (RM) " +
        "%s" +
        "\n" +
        "\n -----------------------------------------" +
        "\n  SubTotal          :" + "%" + 20 + ".2f" +
        "\n  Service Tax (6%%)  :"+ "%" + 20 + ".2f" +
        "\n  Rounding Adj      :" + "%" + 20 + ".2f" +
        "\n                     ---------------------" +
        "\n                      TOTAL    RM%" + 8 + ".2f" +
        "\n -----------------------------------------" +
        "%s" +
        "\n" +
        "\n   Please keep this receipt as proof of   " +
        "\n     payment and for future references.   " +
        "\n *****************************************" +
        "\n      THANK YOU AND PLEASE COME AGAIN!    " +
        "\n",
        order.getCart().receiptToString(), order.getCart().getSubTotal(), 
        order.getServiceTaxPaid(), order.getRoundAdj(),
        order.getTotalPrice(), strCash);
    }
} 