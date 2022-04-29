public class Sales{
    
    private Item bestCoffee;
    private double[] weeklySales;
    private double[] monthlySales;
    private int totalItemSold;
    private double grossSales;
    private double grossTax;
    private double totalSales;
    
    public Sales(){}
    public Sales(double[] weekly, double[] monthly, int totalItem, 
                 double grossSales, double grossTax, double totalSales){
        setBestCoffee();
        this.weeklySales = weekly;
        this.monthlySales = monthly;
        this.totalItemSold = totalItem;
        this.grossSales = grossSales;
        this.grossTax = grossTax;
        this.totalSales = totalSales;
    }
    
    public void setBestCoffee(){
        Item[] allItems = FileHandler.getAllItems();
        bestCoffee = new Item();
        int bestCoffeeQty = 0;
        for(int i = 0; allItems[i] != null; i++){
            if(allItems[i] instanceof Coffee && 
               allItems[i].getItemSold() > bestCoffeeQty){
                bestCoffeeQty = allItems[i].getItemSold();
                bestCoffee = allItems[i];
            }
        }
    }
    
    public String toString(){
        String grandSummary = 
        String.format("\n Total Item Sold  : %d" +
                      "\n Gross Sales      : RM %.2f" +
                      "\n Gross Tax        : RM %.2f" +
                      "\n TOTAL SALES      : RM %.2f" +
                      "\n" + Display.equalBar(60),
                      totalItemSold, grossSales, grossTax, totalSales);
                                            
        String bestProductSum = 
        String.format("\n Best Coffee Type (by highest amount sold):" +
                      "\n" + Display.equalBar(60) +
                      "\n Coffee Name      : %s" +
                      "\n Amount Sold      : %d" +
                      "\n Total Collection : RM %.2f",
                      bestCoffee.getItemName(), bestCoffee.getItemSold(), 
                      bestCoffee.getItemSales());
                                              
        String summary = grandSummary + bestProductSum + "\n";
        return summary;
    }
    
    public String weeklyToString(){
        String weeklyToString = "";
        for(int i = 0; i < Time.getWeek().length; i++){
            weeklyToString +=
            String.format("\n %-" + 10 + "s : RM %.2f", 
                          Time.getWeek()[i], weeklySales[i]);
        }      
        return weeklyToString;
    }
    public String monthlyToString(){
        String monthlyToString = "";
        for(int i = 0; i < Time.getMonth().length; i++){
            monthlyToString +=
            String.format("\n %-" + 10 + "s : RM %.2f", 
                          Time.getMonth()[i], monthlySales[i]);
        }      
        return monthlyToString;
    }
    
    public String weeklyData(){
        String weeklyData = "";
        for(int i = 0; i < Time.getWeek().length; i++)
             weeklyData += 
             String.format("%s|%.2f|\n", Time.getWeek()[i], weeklySales[i]);
        return weeklyData;
    }
    public String monthlyData(){
        String monthlyData = "";
        for(int i = 0; i < Time.getMonth().length; i++)
             monthlyData +=
             String.format("%s|%.2f|\n", Time.getMonth()[i], monthlySales[i]);
        return monthlyData;
    }
    public String salesData(){
        String salesData = "";
        salesData += "Total Items|" + totalItemSold + "|\n";
        salesData += "Gross Sales|" + grossSales + "|\n";
        salesData += "Gross Tax|" + grossTax + "|\n";
        salesData += "Total Sales|" + totalSales + "|";
        return salesData;
    }
    
    public static void storeAllSalesData(Order order){   
        storeItemSales(order);
        storeCashierSales(order);
        storeSales(order);
    }
    public static void storeItemSales(Order order){
        Item[] newItemData = order.getCart().getList();

        for(int i = 0; !order.getCart().isItemListNull(i); i++){
            int itemSold = newItemData[i].getItemSold();
            double itemSale = newItemData[i].getItemSales();
            
            itemSale += newItemData[i].getItemTotal();
            itemSold += newItemData[i].getItemQty();
            
            newItemData[i].setItemSales(itemSale);
            newItemData[i].setItemSold(itemSold);
            
            int category = newItemData[i].findItemCategory();
            Item[] itemData = FileHandler.readItemFiles(category);
            storeItemSalesData(category, itemData, newItemData,i);
        }
    }
    public static void storeItemSalesData(int category, Item[] itemData, 
                                          Item[] newItemData, int i){
        boolean itemMatch = false;
        for(int j = 0; itemData[j] != null && itemMatch == false; j++){
            if(newItemData[i].getItemName().equals(itemData[j].getItemName())){
                itemData[j] = newItemData[i];
                itemMatch = true;
            }
        }
        FileHandler.writeItemFiles(category, itemData); 
    }
    public static void storeSales(Order order){
        Sales currentSalesData = FileHandler.readSalesFiles();
        
        String orderDay = order.getTimestamp().getStrDay();
        String orderMonth = order.getTimestamp().getStrMonth();
        
        for(int i = 0; i < Time.getWeek().length; i++){
            if(Time.getWeek()[i].equals(orderDay))
                currentSalesData.weeklySales[i] += order.getTotalPrice();
        }
        
        for(int i = 0; i < Time.getMonth().length; i++){
            if(Time.getMonth()[i].equals(orderMonth))
                currentSalesData.monthlySales[i] += order.getTotalPrice();
        }
        
        currentSalesData.totalItemSold += order.getCart().getTotalItemQty();
        currentSalesData.grossSales += order.getCart().getSubTotal();
        currentSalesData.grossTax += order.getServiceTaxPaid();
        currentSalesData.totalSales += order.getTotalPrice();
        
        FileHandler.writeSalesFiles(currentSalesData);
    }
    public static void storeCashierSales(Order order){
        Cashier cashier = order.getCashier();
        Cashier[] allCashiers = FileHandler.getAllCashiers();
        
        cashier.setSuccessOrder(cashier.getSuccessOrder()+1);
        cashier.setCashierSales(
        cashier.getCashierSales() + order.getTotalPrice());
        
        boolean cashierFound = false;
        for(int i = 0; allCashiers[i] != null && !cashierFound; i++){
            if(cashier.isCashierMatch(allCashiers[i]))
                allCashiers[i] = cashier;
        }
        FileHandler.updateCashierData(allCashiers);
    }
    
    public void clearSalesData(){
        for(int i = 0; i < weeklySales.length; i++)
            weeklySales[i] = 0.00;
        for(int i = 0; i < monthlySales.length; i++)
            monthlySales[i] = 0.00;
        totalItemSold = 0;
        grossSales = 0.00;
        grossTax = 0.00;
        totalSales = 0.00;
        clearAllItemSalesData();
        clearAllCashierSalesData();
        FileHandler.writeSalesFiles(this);
    }
    public void clearAllItemSalesData(){
        Item[] allItems = FileHandler.getAllItems();
        for(int i = 0; allItems[i] != null; i++)
            allItems[i].clearItemSales();

        Beverage[] allBeverages = new Beverage[128];
        for(int i = 0, j = 0; allItems[i] != null; i++){
            if(allItems[i] instanceof Beverage){
                allBeverages[j] = (Beverage)allItems[i];
                j++;
            }
        }
        FileHandler.writeItemFiles(1, allBeverages);
        
        Food[] allFoods = new Food[128];
        for(int i = 0, j = 0; allItems[i] != null; i++){
            if(allItems[i] instanceof Food){
                allFoods[j] = (Food)allItems[i];
                j++;
            }
        }
        FileHandler.writeItemFiles(2, allFoods);
    }
    public void clearAllCashierSalesData(){
        Cashier[] cashiers = FileHandler.getAllCashiers();
        for(int i = 0; cashiers[i] != null; i++)
            cashiers[i].clearCashierSales();
            
        FileHandler.updateCashierData(cashiers);
    }
    
    public static void dispSales(){
        boolean exit = false;
        do{
            Sales sales = FileHandler.readSalesFiles();
            String[] selection = {"View Weekly Sales", "View Monthly Sales", 
                                  "Clear All Sales Data"};
            Display dispSales = 
            new Display("Sales Summary", sales.toString(), selection,1);
            dispSales.display();
            
            switch(Display.getInput()){
                case "1": sales.viewWeeklySales();
                          break;
                case "2": sales.viewMonthlySales();
                          break;
                case "3": sales.confirmClearSales();
                          break;
                case "0": exit = true; 
                          break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
    }
    
    public void viewWeeklySales(){
        Display dispWeeklySales = 
        new Display("Weekly Sales", weeklyToString() + "\n", null,5);
        dispWeeklySales.display();
        switch(Display.getInput()){
            default: break;
        }
    }
    
    public void viewMonthlySales(){
        Display dispMonthlySales = 
        new Display("Monthly Sales", monthlyToString() + "\n", null,5);
        dispMonthlySales.display();
        switch(Display.getInput()){
            default: break;
        }
    }
      
    public void confirmClearSales(){
        boolean exit = false;
        do{
            String[] selection = {"Confirm"};
            Display confirmClearSales =
            new Display("Are you sure you want to clear all sales data?",
                        null, selection,2);
            confirmClearSales.display();
            
            switch(Display.getInput()){
                case  "":
                case "1": clearSalesData();
                          Display.message("Successfully cleared all sales data!",2);
                          exit = true;
                          break;
                case "0": exit = true; break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
    }
} 