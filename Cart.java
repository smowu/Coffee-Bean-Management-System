public class Cart{
    private Item[] itemList;
    private int itemNo;
    private int totalItemQty;
    private double subTotal;
    
    public Cart(){
        this.itemList = new Item[128];
        this.itemNo = 0;
        this.totalItemQty = 0;
        this.subTotal = 0.00;
    }
    
    public void setItem(Item item, int i){this.itemList[i] = item;}
    
    public Item[] getList(){return itemList;}
    public int getTotalItemQty(){return totalItemQty;}
    public double getSubTotal(){return subTotal;}
    
    public void calcSubTotal(){
        subTotal = 0.00;
        totalItemQty = 0;
        for(int i = 0; !isItemListNull(i); i++){
            subTotal += itemList[i].getItemTotal();
            totalItemQty += itemList[i].getItemQty();
        } 
    }
    
    public boolean isThereItem(){
        if(totalItemQty > 0) 
            return true;
        else 
            return false;
    }
    public boolean isItemListNull(int i){
        if(itemList[i] == null) 
            return true;
        else 
            return false;
    }
    
    public String receiptToString(){
        String receiptItemList = "";
        for(int i = 0; 
            itemList[i] != null && itemList[i].getItemQty() != 0; i++){
            
            Item item = itemList[i];
            String name = item.getItemName();
            if (name.length() > 18)
                name = item.getItemName().substring(0, 18);
            receiptItemList += 
            String.format("\n  "+"%"+2+"d "+"%-"+18+"s "+"%"+3+ 
                          "d  "+"%"+6+".2f"+"%"+6+".2f ",
                          i + 1, name, item.getItemQty(), 
                          item.getPrice(), item.getItemTotal());
        }
        return receiptItemList;
    }
    
    public String toString(){
        String listStr = "";
        for(int i = 0; i < itemList.length; i++)
            listStr += "\n" + itemList[i].toString();
        return listStr;
    }
    
    public void addItemToCart(Item newItem){
        itemList[itemNo] = newItem;
        calcSubTotal();
        ++itemNo;
    }
    public void removeItemFromCart(int i){
        itemList[i] = new Item();
        for(int j = i + 1; itemList[j] != null; j++){
            itemList[i] = itemList[++i];
            itemList[j] = null;
        }
        --itemNo;
        Display.message("Item Removed from Cart.", 2);
    }
    
    public static Item addItem(){
        Item selectedItem = new Item();
        selectedItem = null;
        boolean exit = false;
        do{
            int category = Item.selectItemCategory();
            if(category == 0)
                exit = true;
            else if(category == 1 || category == 2){ 
                selectedItem = Item.dispItemsInCategory(category);
                if(selectedItem != null) 
                    exit = true;
            }
        }while(!exit);
        return selectedItem;
    }
    
    public boolean editItem(int i){   
        boolean isBeverage = false;
        String sizeSelection = null;
        if(itemList[i] instanceof Beverage){
            isBeverage = true;
            sizeSelection = 
            Display.minusBar(60) + "\n Customize Drink " +
            "\n  [R] Regular [L] Large | [H] Hot [C] Cold(+RM0.50)";
        }
        
        boolean confirm = false, save = false;
        do{
            String[] selection = 
            {"+1", "-1", "Custom Amount", "Save To Cart", "Remove Item"};
            Display editOrder = 
            new Display("Edit Item", itemList[i].dispItemModifier(), selection,2);
            editOrder.setAddText(sizeSelection,2);
            editOrder.display();
            
            String input = Display.getInput();
            switch(input){
                case "1": itemList[i].addOneItem(); break;
                case "2": itemList[i].minusOneItem(); break;
                case "3": itemList[i].customAmount(); break;
                case  "":
                case "4": confirm = itemList[i].confirmItem();
                          save = true; 
                          break;
                case "5": removeItemFromCart(i);
                          confirm = save = true;
                          break;
                case "r": case "R": case "l": case "L":
                          if(isBeverage){
                              char size = input.charAt(0);
                              Beverage beverage = (Beverage)itemList[i];
                              beverage.setSize(Character.toUpperCase(size));
                              itemList[i] = beverage;
                          }else 
                              Display.invalidMessage(1);
                          break;
                case "h": case "H": case "c": case "C":
                          if(isBeverage){
                              Beverage beverage = (Beverage)itemList[i];
                              char iced = input.charAt(0);
                              beverage.setIsIced(iced);
                              itemList[i] = beverage; 
                          }else
                              Display.invalidMessage(1);
                          break;
                case "0": confirm = confirmCancel();
                          save = false;
                          break;
                default : Display.invalidMessage(1); break;
            }
            if(itemList[i] != null)
                itemList[i].setItemTotal(itemList[i].calcItemPrice());
        }while(!confirm);
        
        if(save)
            return true;
        else
            return false;
    }
    public static boolean confirmCancel(){
        boolean exit = false, confirm = false;
        do{
            String[] selection = {"Yes"};
            Display confirmCancel = 
            new Display("Are you sure you want to cancel edit?", 
                        null, selection,1);
            confirmCancel.display();
            
            switch(Display.getInput()){
                case  "":
                case "1": exit = confirm = true;
                          Display.message("Edit canceled.",2);
                          break;
                case "0": exit = true; break;         
                default : Display.invalidMessage(1); break; 
            }
        }while(!exit);
        return confirm;
    }
} 