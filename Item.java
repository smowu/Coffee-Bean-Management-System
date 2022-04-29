public class Item{
    private String itemType;
    private String itemName;
    private double[] itemPrice;
    private int itemQty;
    private double itemTotal;
    
    private int itemSold;
    private double itemSales;
    
    public Item(){
        this.itemType = "No Data";
        this.itemName = "No Data";
        this.itemPrice = new double[2];
        this.itemPrice[0] = 0.00;
        if(this instanceof Beverage)
            this.itemPrice[1] = 0.00;
        this.itemQty = 0;
        this.itemTotal = 0.00;
    }
    public Item(Item i){
        this.itemType = i.itemType;
        this.itemName = i.itemName;
        this.itemPrice = new double[2];
        this.itemPrice[0] = i.itemPrice[0];
        if(this instanceof Beverage)
            this.itemPrice[1] = i.itemPrice[1];
        this.itemQty = i.itemQty;
        this.itemTotal = i.itemTotal;
        
        this.itemSold = i.itemSold;
        this.itemSales = i.itemSales;
    }
    public Item(String name, double priceR, double priceL, int sold, 
                double sales){
        this.setItemType();
        this.itemName = name;
        this.itemPrice = new double[2];
        this.itemPrice[0] = priceR;
        this.itemPrice[1] = priceL;
        this.itemQty = 0;
        this.itemTotal = 0.00;
        
        this.itemSold = sold;
        this.itemSales = sales;
    }
    public Item(String name, double price, int sold, double sales){
        this.setItemType();
        this.itemName = name;
        this.itemPrice = new double[1];
        this.itemPrice[0] = price;
        this.itemQty = 0;
        this.itemTotal = 0.00;
        
        this.itemSold = sold;
        this.itemSales = sales;
    }
    
    public void setItemType(){
        if(this instanceof Beverage){
            if(this instanceof Coffee)
                itemType = "Coffee";
            else if(this instanceof Tea)
                itemType = "Tea";
            else if(this instanceof SoftDrink)
                itemType = "Soft Drink";
        }else if(this instanceof Food){
            if(this instanceof Sandwich)
                itemType = "Sandwich";
            else if(this instanceof Dessert)
                itemType = "Dessert";
        }
    }
    public void setItemTotal(double total){this.itemTotal = total;}
    public void setItemSold(int sold){this.itemSold = sold;}
    public void setItemSales(double sales){this.itemSales = sales;}
    
    public String getItemType(){return itemType;}
    public String getItemName(){return itemName;}
    public double[] getItemPrice(){return itemPrice;}
    public int getItemQty(){return itemQty;}
    public double getItemTotal(){return itemTotal;}
    public int getItemSold(){return itemSold;}
    public double getItemSales(){return itemSales;}
    
    public void addOneItem(){
        ++itemQty;
    }
    public void minusOneItem(){
        if(itemQty != 1)
            --itemQty;
    }
    public void customAmount(){
        boolean input = false;
        do{
            System.out.print("\fInput Custom Amount: ");
            itemQty = Display.getIntInput();
            
            if(itemQty > 0) 
                input = true;
            else 
                Display.invalidMessage(0);
        }while(!input);
    }
    
    public boolean confirmItem(){
        boolean confirm = false;
        if(itemQty == 0) 
            Display.invalidMessage(1);
        else{
            confirm = true;
            Display.message("Item Saved to Cart.",2);
        }
        return confirm;
    }
    
    public double getPrice(){
        double price = 0.00;
        if(itemPrice != null){
            price = itemPrice[0];
            if(this instanceof Beverage){
                Beverage beverage = (Beverage)this;
                if(beverage.getSize() == 'L')
                    price = beverage.getItemPrice()[1];
                if(beverage.isIced())
                    price += 0.50;
            }
        }
        return price;
    }
    public double calcItemPrice(){
        return getPrice() * itemQty;
    }
    
    public void clearItemSales(){
        itemSold = 0;
        itemSales = 0.00;
    }
    
    public String toString(){
        String itemPriceStr;
        if(this instanceof Beverage){
            itemPriceStr = 
            String.format("\n Item Price(R)  : RM %.2f" +
                          "\n Item Price(L)  : RM %.2f",
                          itemPrice[0], itemPrice[1]);
        }else
            itemPriceStr = 
            String.format("\n Item Price     : RM%.2f", itemPrice[0]);
        
        return 
        String.format("\n Item Type      : %s" +
                      "\n Item Name      : %s" +
                      "%s" +
                      "\n Item Sold      : %d" +
                      "\n Item Sales     : RM %.2f",
                      itemType, itemName, itemPriceStr, 
                      itemSold, itemSales);
    }
    
    public String dispItemModifier(){
        String dispItemName = itemName;
        if(this instanceof Beverage){
            Beverage item = (Beverage)this;
            dispItemName = item.strItemName();
        }
        return 
        String.format("\n Item Name  : %s" +
                      "\n Item Price : RM %.2f" +
                      "\n-----------------------------" +
                      "\n Qty        : %d" +
                      "\n Item Total : RM %.2f\n" +
                      Display.hashBar(60) +
                      "\n [Choose Quantity]\n",
                      dispItemName, getPrice(), itemQty, itemTotal);
    }
    
    public String itemData(){
        if(this instanceof Beverage)
            return 
            String.format("%s|%s|%.2f|%.2f|%d|%.2f|",
                          itemType, itemName, itemPrice[0], itemPrice[1], 
                          itemSold, itemSales);
        else
            return 
            String.format("%s|%s|%.2f|%d|%.2f|",
                          itemType, itemName, itemPrice[0], 
                          itemSold, itemSales);
    }
    
    public int findItemCategory(){
        int category = 0;
        if(this instanceof Beverage) 
            category = 1;
        else if(this instanceof Food) 
            category = 2;
        return category;
    }
    
    public static int selectItemCategory(){
        int category = -1;
        String[] selection = {"Beverages", "Foods"};
        Display categoryList = 
        new Display("Select Item Category", null, selection,1);
        categoryList.display();
        
        switch(Display.getInput()){ 
            case "1": category = 1; break; // Beverages
            case "2": category = 2; break; // Foods
            case "0": category = 0; break;
            default : Display.invalidMessage(1); break;
        }
        return category;
    }
    
    public static void selectNewItemCategory(){
        Item newItem = null;
        int category = 0;
        boolean exit = false, confirm = false;
        do{
            category = selectItemCategory();
            if(category == 0)
                exit = true;
            else{
                String itemType = selectItemType(category);
                if(itemType != null){
                    newItem = createNewItem(category, itemType);
                    storeNewItem(newItem, category);
                    exit = true;
                }
            }
        }while(!exit);
    }
    public static void storeNewItem(Item newItem, int category){
        Item[] allItems = FileHandler.readItemFiles(category);
        int i = 0;
        while(allItems[i] != null)
            i++;
        if(allItems[i] == null)
            allItems[i] = newItem;
        
        FileHandler.writeItemFiles(category, allItems);
        Display.message("Successfully created new item!",2);
        
        Display dispNewItem = 
        new Display("New Item", newItem.toString()+"\n", null,5);
        dispNewItem.display();
        
        switch(Display.getInput()){
            default: break;
        }
    }
    
    public static Item createNewItem(int category, String itemType){
        Item newItem;
        String itemName = "";
        double itemPrice0 = 0.00, itemPrice1 = 0.00;

        itemName = editItemName();
        itemPrice0 = editItemPrice(0);
        if(category == 1)
            itemPrice1 = editItemPrice(1);

        switch(itemType){
            case "Coffee": 
                newItem = new Coffee(itemName, itemPrice0, itemPrice1, 0,0.00);
                break;
            case "Tea": 
                newItem = new Tea(itemName, itemPrice0, itemPrice1, 0,0.00);
                break;
            case "Soft Drink": 
                newItem = new SoftDrink(
                          itemName, itemPrice0, itemPrice1, 0,0.00);
                break;
            case "Dessert": 
                newItem = new Dessert(itemName, itemPrice0, 0, 0.00);
                break;
            case "Sandwich": 
                newItem = new Sandwich(itemName, itemPrice0, 0, 0.00);
                break;
            default: 
                newItem = null; break;
        }
        return newItem;
    }
    
    public static String selectItemType(int category){
        String itemType = "";
        int type = 0, slct = 0;
        if(category == 1)
            slct = 3;
        else if(category == 2)
            slct = 2;
        String[] typeSlct = new String[slct];
        
        if(category == 1){
            typeSlct[0] = "Coffee"; 
            typeSlct[1] = "Tea";
            typeSlct[2] = "Soft Drink";
        }else if(category == 2){
            typeSlct[0] = "Dessert";
            typeSlct[1] = "Sandwich";
        }
            
        Display typeList = 
        new Display("Select Item Type", null, typeSlct,1);
        typeList.display();
        
        switch(Display.getInput()){ 
            case "1": itemType = typeSlct[0]; break;
            case "2": itemType = typeSlct[1]; break;
            case "3": if(category == 1)
                        itemType = typeSlct[2];
                      else
                        Display.invalidMessage(1); 
                      break;
            case "0": itemType = null; break;
            default : Display.invalidMessage(1); break;
        }
        return itemType;
    }
    
    public boolean dispItem(){
        boolean exit = false, isRemoved = false;
        do{
            String[] selection = {"Edit Item Info", "Clear Item Sales", 
                                  "Remove Item"};
            Display selectedItemInfo = 
            new Display(getItemType()+" | "+getItemName(), toString()+"\n", 
                        selection,1);
            selectedItemInfo.display();
            
            switch(Display.getInput()){
                case "1": editItem(); break;
                case "2": confirmClearItemSales(); break;
                case "3": exit = isRemoved = confirmRemoveItem();
                          break;
                case "0": exit = true; break;
                default : Display.invalidMessage(0); break;
            }
        }while(!exit);
        return isRemoved;
    }
    public void editItem(){
        boolean exit = false;
        do{ 
            Display dispItemEdit;
            if(this instanceof Beverage){
                String[] selection = 
                {"Item Name     : "+itemName,
                 "Item Price(R) : RM"+itemPrice[0],
                 "Item Price(L) : RM"+itemPrice[1]};
                dispItemEdit =  
                new Display("Edit Selection", null, selection,1);
            }else{
                String[] selection = 
                {"Item Name     : "+itemName,
                 "Item Price    : RM"+itemPrice[0]};
                dispItemEdit = 
                new Display("Edit Selection", null, selection,1);
            }
            dispItemEdit.display();
            
            switch(Display.getInput()){
                case "1": itemName = editItemName(); break;
                case "2": itemPrice[0] = editItemPrice(0); break;
                case "3": if(this instanceof Beverage)
                              itemPrice[1] = editItemPrice(1);
                          else
                              Display.invalidMessage(1); 
                          break;
                case "0": exit = true; break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
    }
    public static String editItemName(){
        System.out.print("\fEnter New Name: ");
        String itemName = Display.getInput();
        return itemName;
    }
    public static double editItemPrice(int priceType){
        double[] itemPrice = new double[2];
        boolean input = false;
        do{
            if(priceType == 1)
                System.out.print("\fEnter Another New Price (RM): ");
            else
                System.out.print("\fEnter New Price (RM): ");
            try{
                itemPrice[priceType] = Double.parseDouble(Display.getInput());
            }catch(Exception e){
                itemPrice[priceType] = -1;
            }
            if(itemPrice[priceType] >= 0) 
                input = true;
            else 
                Display.invalidMessage(0);
        }while(!input);
        return itemPrice[priceType];
    }
    
    public void confirmClearItemSales(){
        boolean exit = false;
        do{
            String[] selection = {"Yes"};
            Display dispClearItemSales = 
            new Display("Are you sure you want to clear this item sales?", 
                        null, selection,2);
            
            dispClearItemSales.display();
            
            switch(Display.getInput()){
                case  "":
                case "1": clearItemSales();
                          Display.message("Successfully cleared item sales!",2);
                          exit = true; 
                          break;
                case "0": exit = true; break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
    }
    
    public boolean confirmRemoveItem(){
        boolean exit = false, confirm = false;
        do{
            String[] selection = {"Yes"};
            Display dispRemoveItem = 
            new Display("Are you sure you want to remove this item?", 
                        null, selection,2);
            
            dispRemoveItem.display();
            
            switch(Display.getInput()){
                case  "":
                case "1": exit = confirm = true; break;
                case "0": exit = true; break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
        return confirm;
    }
    
    public static Item dispItemsInCategory(int category){
        Item[] listOfItems = FileHandler.readItemFiles(category);
        Item selectedItem = null;
        
        // For displaying list of items and it's prices
        String[] itemSelection = new String[128];
        for(int i = 0; listOfItems[i] != null; i++){
            itemSelection[i] = 
            String.format("%-"+10+"s| "+"%-"+24+"s"+" %"+8+".2f",
                          listOfItems[i].getItemType(), 
                          listOfItems[i].getItemName(), 
                          listOfItems[i].getItemPrice()[0]);
            if(listOfItems[i] instanceof Beverage){
                itemSelection[i] += 
                String.format("%"+8+".2f",listOfItems[i].getItemPrice()[1]);
            }
        }
        boolean exit = false;
        do{
            String label = 
            String.format("\n  NO. %-"+10+"s| "+"%-"+24+"s ", 
                          "ITEM TYPE", "ITEM NAME");
            if(category == 1)
                label += String.format("%"+8+"s"+"%"+8+"s",
                                       "REGULAR", "LARGE");
            else
                label += String.format("%"+8+"s", "PRICE");
                
            Display itemsInCategory = 
            new Display("Item List", null, itemSelection,1);
            itemsInCategory.setAddText(label,1);
            itemsInCategory.display();
            
            // Determining valid dynamic input range
            int select = Display.getIntInput(), max = 0;
            while(itemSelection[max] != null) 
                max++;
            if(select < 0 || select >= ++max) 
                select = -1;
            
            switch(select){
                case -1: Display.invalidMessage(1); break;
                case  0: exit = true; break;
                default: selectedItem = listOfItems[--select];
                         selectedItem = selectedItem.dispModifyItem(category);
                         if(selectedItem != null) 
                             exit = true;
                         break;
            }
        }while(!exit);
        return selectedItem;
    }
    
    public Item dispModifyItem(int category){
        Item item = this;
        String[] selection = {"+1", "-1", "Custom Amount", "Add To Cart"};
        String changeSizeStr = null;
        boolean isBeverage = false;
        if(this instanceof Beverage){
            isBeverage = true;
            changeSizeStr = 
            Display.minusBar(60) + "\n Customize Drink " +
            "\n  [R] Regular [L] Large | [H] Hot [C] Cold(+RM0.50)";
        }
        
        item.itemQty = 1;
        boolean confirm = false;
        do{
            item.itemTotal = item.calcItemPrice();
            Display modifierMenu = 
            new Display("Edit Item", item.dispItemModifier(), selection,2);
            modifierMenu.setAddText(changeSizeStr,2);
            modifierMenu.display();
            String input = Display.getInput();
            switch(input){
                case "1": item.addOneItem(); break;
                case "2": item.minusOneItem(); break;
                case "3": item.customAmount(); break;
                case  "": 
                case "4": confirm = item.confirmItem(); break;
                case "r": case "R": case "l": case "L":
                          if(isBeverage){
                              Beverage beverage = (Beverage)item;
                              char size = input.charAt(0);
                              beverage.setSize(Character.toUpperCase(size));
                              item = beverage; 
                          }else 
                              Display.invalidMessage(1);
                          break;
                case "h": case "H": case "c": case "C":
                          if(isBeverage){
                              Beverage beverage = (Beverage)item;
                              char iced = input.charAt(0);
                              beverage.setIsIced(iced);
                              item = beverage; 
                          }else
                              Display.invalidMessage(1);
                          break;
                case "0": item = null;
                          confirm = true;
                          Display.message("Item Canceled.",2); 
                          break;
                default : Display.invalidMessage(1); 
                          break;
            }
        }while(!confirm);
        return item;
    }
} 