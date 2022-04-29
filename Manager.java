public class Manager extends Employee{
    public Manager(){super();}
    public Manager(String id, String pass, String n, String ic, String email){
        super(id, pass, n, ic, email);
    }
    
    public String toString(){
        return super.toString();
    }
    
    public String employeeData(){
        return String.format("%s|%s|%s|%s|%s|", getIdNumber(), getPassword(), 
                             getName(), getIC(), getEmail());
    }
    
    public static void restoreAllItems(){
        Item[] restoredItems = FileHandler.getRestoredItems();

        Beverage[] restoredBeverages = new Beverage[128];
        for(int i = 0, j = 0; restoredItems[i] != null; i++){
            if(restoredItems[i] instanceof Beverage){
                restoredBeverages[j] = (Beverage)restoredItems[i];
                j++;
            }
        }
        FileHandler.writeItemFiles(1, restoredBeverages);
        
        Food[] restoredFoods = new Food[128];
        for(int i = 0, j = 0; restoredItems[i] != null; i++){
            if(restoredItems[i] instanceof Food){
                restoredFoods[j] = (Food)restoredItems[i];
                j++;
            }
        }
        FileHandler.writeItemFiles(2, restoredFoods);
    }
    
    public void dispEmployeeMenu(){
        boolean logout = false;
        do{
            String[] selection = {"View Sales", "View All Cashiers", 
                                  "Manage Items", "View Profile"};
            Display managerMenu = 
            new Display("Manager Menu", dispEmployeeSummary(), selection,3);
            managerMenu.display();
            
            switch(Display.getInput()){
                case "1": Sales.dispSales();
                          break;
                case "2": Cashier.dispAllCashiers();
                          break;
                case "3": manageItemsMenu();
                          break;
                case "4": dispEmployeeProfile(); 
                          break;
                case "0": logout = getLog().logout(); 
                          break;
                default : Display.invalidMessage(1); break;
            }
        }while(!logout);
    }

    public void manageItemsMenu(){
        boolean exit = false;
        do{
            String[] selection = {"Manage Existing Items", "Add New Item", 
                                  "Restore All Items"};
            Display manageItemMenu = 
            new Display("Manage Items", null, selection,1);
            manageItemMenu.display();
            
            switch(Display.getInput()){ 
                case "1": manageItemsSelection(); break; 
                case "2": Item.selectNewItemCategory(); break;
                case "3": confirmRestoreItems(); break;
                case "0": exit = true; break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
    }
    public void manageItemsSelection(){
        boolean exit = false;
        do{
            int category = Item.selectItemCategory();
            if(category == 0)
                exit = true;
            else if(category == 1 || category == 2){
                manageItems(category);
            }
        }while(!exit);
    }
    public void manageItems(int category){
        boolean exit = false;
        do{
            Item[] itemList = FileHandler.readItemFiles(category);
            String[] itemSelections = new String[128];
            for(int i = 0; itemList[i] != null; i++){
                itemSelections[i] = 
                String.format("%-"+10+"s| "+"%-"+24+"s ",
                              itemList[i].getItemType(), 
                              itemList[i].getItemName());
            }
            String label = 
            String.format("\n  NO. %-"+10+"s| "+"%-"+24+"s ", 
                          "ITEM TYPE", "ITEM NAME");
                
            Display itemsInCategory = 
            new Display("Item List", null, itemSelections,1);
            itemsInCategory.setAddText(label,1);
            itemsInCategory.display();
            
            // Determining valid input range
            int select = Display.getIntInput(), max = 0;
            while(itemSelections[max] != null) 
                max++;
            if(select < 0 || select >= ++max) 
                select = -1;
            
            boolean isRemoved = false;
            switch(select){
                case -1: Display.invalidMessage(1); break;
                case  0: exit = true; break;
                default: isRemoved = itemList[--select].dispItem();
                         break;
            }
            
            if(isRemoved){
                itemList[select] = null;
                for(int j = select + 1; itemList[j] != null; j++){
                    itemList[select] = itemList[++select];
                    itemList[j] = null;
                }
                Display.message("Item successfully removed!", 2);
            }
            FileHandler.writeItemFiles(category, itemList);
        }while(!exit);
    }
    
    public void confirmRestoreItems(){
        boolean exit = false;
        do{
            String[] selection = {"Confirm"};
            Display confirmRestore = 
            new Display("Restore all items to their default settings?", 
                        null, selection,2);
            confirmRestore.display();
            
            switch(Display.getInput()){
                case  "": 
                case "1": FileHandler.restoreItems(1); 
                          FileHandler.restoreItems(2);
                          Display.message("Items succesfully restored!",2);
                          exit = true;
                          break;
                case "0": exit = true; break;
                default : Display.invalidMessage(1); break;
            }
        }while(!exit);
    }
} 