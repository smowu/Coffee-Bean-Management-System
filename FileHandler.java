import java.util.Scanner;
import java.io.*;
import java.util.StringTokenizer;

public class FileHandler{
    public static String getFilepath(int category){
        String filepath = "";
        switch(category){
            case -2: filepath = "Restore\\FoodsRestore.txt"; break;
            case -1: filepath = "Restore\\BeveragesRestore.txt"; break;
            case  1: filepath = "BeveragesList.txt"; break;
            case  2: filepath = "FoodsList.txt"; break;
            case  4: filepath = "EmployeeList.txt"; break;
            case  5: filepath = "SalesData.txt"; break;
            default: break;
        }
        return filepath;
    }
    
    public static Item[] readItemFiles(int category){
        Item[] itemList = new Item[128];
        String filepath = getFilepath(category);
        try{
            BufferedReader readFile = new BufferedReader(
                                      new FileReader(filepath));
            String input;

            for(int line = -2; (input = readFile.readLine()) != null; line++){
                StringTokenizer st = new StringTokenizer(input, "|");
                if(line >= 0 && st.hasMoreTokens()){
                    String itemType = st.nextToken();
                    String itemName = st.nextToken();
                    double[] itemPrice = new double[2];
                    itemPrice[0] = Double.parseDouble(st.nextToken());
                    if(category == 1 || category == -1)
                        itemPrice[1] = Double.parseDouble(st.nextToken());
                    int itemSold = Integer.parseInt(st.nextToken());
                    double itemSales = Double.parseDouble(st.nextToken());
                    
                    itemList[line] = 
                    constructItem(category, itemType, itemName, itemPrice,
                                  itemSold, itemSales);
                }
            }
            readFile.close();
        }catch(FileNotFoundException e){
            Display.message("File not found!"+"\nError: "+ e,2);
        }catch(IOException ioe){
            Display.message("Failed to read file!"+"\nError: "+ ioe,2);
        } 
        return itemList;
    }
    public static Item constructItem(int category, String itemType, 
                                     String itemName, double[] itemPrice, 
                                     int itemSold, double itemSales){
        Item item = null;
        if(category == 1){
            if(itemType.equals("Coffee")){
                item = new Coffee(itemName, itemPrice[0], itemPrice[1], 
                                  itemSold, itemSales);
            }else if(itemType.equals("Tea")){
                item = new Tea(itemName, itemPrice[0], itemPrice[1], 
                               itemSold, itemSales);
            }else if(itemType.equals("Soft Drink")){
                item = new SoftDrink(itemName, itemPrice[0], itemPrice[1], 
                                     itemSold, itemSales);
            }
        }else if(category == 2){
            if(itemType.equals("Sandwich"))
                item = 
                new Sandwich(itemName, itemPrice[0], itemSold, itemSales);
            else if(itemType.equals("Dessert"))
                item = 
                new Dessert(itemName, itemPrice[0], itemSold, itemSales);
        }
        return item;
    }
    public static Item[] getAllItems(){
        Item[] allItems = new Item[128];
        Item[] allBeverages = readItemFiles(1);
        Item[] allFoods = readItemFiles(2);
        
        int all = 0;
        for(int i = 0; allBeverages[i] != null; i++)
            allItems[all++] = allBeverages[i];
        for(int i = 0; allFoods[i] != null; i++)
            allItems[all++] = allFoods[i];

        return allItems;
    }
    public static Item[] getRestoredItems(){
        Item[] allItems = new Item[128];
        Item[] allBeverages = readItemFiles(-1);
        Item[] allFoods = readItemFiles(-2);
        
        int all = 0;
        for(int i = 0; allBeverages[i] != null; i++)
            allItems[all++] = allBeverages[i];
        for(int i = 0; allFoods[i] != null; i++)
            allItems[all++] = allFoods[i];

        return allItems;
    }
    
    public static void restoreItems(int fileNum){
        try{
            BufferedReader restoredFile = 
            new BufferedReader(new FileReader(getFilepath(-fileNum)));
            FileWriter targetFile = new FileWriter(getFilepath(fileNum));
            
            String str, strNew = "";
            while((str = restoredFile.readLine()) != null)
                strNew += str + "\n";
            targetFile.write(strNew);
            
            restoredFile.close();
            targetFile.close();
            
        }catch(FileNotFoundException e){
            Display.message("File not found!"+"\nError: "+ e,2);
        }catch(IOException ioe){
            Display.message("Failed to read/write file!"+"\nError: "+ ioe,2);
        } 
    }
    
    public static void writeItemFiles(int category, Item[] newItemData){
        String filepath = getFilepath(category);
        try{
            PrintWriter writeFile = 
            new PrintWriter(new BufferedWriter(new FileWriter(filepath)));
            
            if(category == 1)
                writeFile.println(
                "DRINK CATEGORY|NAME|PRICE(R)|PRICE(L)|ITEM SOLD|TOTAL SALES");
            else
                writeFile.println("TYPE|NAME|PRICE|ITEM SOLD|TOTAL SALES");
            writeFile.println(Display.charBar('=',64));
            
            for(int i = 0; newItemData[i] != null; i++)
                writeFile.println(newItemData[i].itemData());
                
            writeFile.close();
        }catch(FileNotFoundException e){
            Display.message("File not found!"+"\nError: "+ e,2);
        }catch(IOException ioe){
            Display.message("Failed to write file!"+"\nError: "+ ioe,2);
        }  
    }
    
    public static Sales readSalesFiles(){
        double[] weeklySales = new double[7];
        double[] monthlySales = new double[12];
        int totalItemSold = 0;
        double grossSales = 0, grossTax = 0, totalSales = 0.00;
        String filepath = getFilepath(5);
        try{
            BufferedReader readFile = new BufferedReader(
                                      new FileReader(filepath));
            String input;
            for(int day = -2; 
                day < 7 && (input = readFile.readLine()) != null; day++){
                if(day >= 0){
                    StringTokenizer st = new StringTokenizer(input, "|");
                    st.nextToken();
                    weeklySales[day] = Double.parseDouble(st.nextToken());
                }
            }readFile.readLine();
            for(int m = -2; 
                m < 12 && (input = readFile.readLine()) != null; m++){
                if(m >= 0){
                    StringTokenizer st = new StringTokenizer(input, "|");
                    st.nextToken();
                    monthlySales[m] = Double.parseDouble(st.nextToken());
                }
            }readFile.readLine();
            for(int line = -1; 
                line < 5 && (input = readFile.readLine()) != null; line++){
                if(line > 0){
                    StringTokenizer st = new StringTokenizer(input, "|");                    
                    if(line == 1){
                        st.nextToken();
                        totalItemSold = Integer.parseInt(st.nextToken());
                    }else if(line == 2){
                        st.nextToken();
                        grossSales = Double.parseDouble(st.nextToken());
                    }else if(line == 3){
                        st.nextToken();
                        grossTax = Double.parseDouble(st.nextToken());
                    }else if(line == 4){
                        st.nextToken();
                        totalSales = Double.parseDouble(st.nextToken());
                    }
                }
            }readFile.close();
        }catch(FileNotFoundException e){
            Display.message("File not found!"+"\nError: "+ e,2);
        }catch(IOException ioe){
            Display.message("Failed to read file!"+"\nError: "+ ioe,2);
        }  
        return
            new Sales(weeklySales, monthlySales, totalItemSold, grossSales, 
                      grossTax, totalSales);
    }

    public static void writeSalesFiles(Sales sales){
        String filepath = getFilepath(5);
        try{
            PrintWriter writeFile = 
            new PrintWriter(new BufferedWriter(new FileWriter(filepath)));
            
            writeFile.println(" DAY|TOTAL SALES ");
            writeFile.println(Display.charBar('=',40));
            writeFile.println(sales.weeklyData());    
            
            writeFile.println(" MONTH|TOTAL SALES ");
            writeFile.println(Display.charBar('=',40));
            writeFile.println(sales.monthlyData());
            
            writeFile.println(" SALES DATA ");
            writeFile.println(Display.charBar('=',40));
            writeFile.println(sales.salesData());

            writeFile.close();
        }catch(FileNotFoundException e){
            Display.message("File not found!"+"\nError: "+ e,2);
        }catch(IOException ioe){
            Display.message("Failed to write file!"+"\nError: "+ ioe,2);
        } 
    }
    
    public static Employee[] readEmployeeFiles(){
        Employee[] employees = new Employee[128];
        String filepath = getFilepath(4);
        
        try{
            BufferedReader readFile = new BufferedReader(
                                      new FileReader(filepath));
            String input;
            for(int i = -2; ((input = readFile.readLine()) != null); i++){
                StringTokenizer st = new StringTokenizer(input, "|");
                if(i >= 0 && st.hasMoreTokens()){
                    String idNumber = st.nextToken();
                    String password = st.nextToken();
                    String name = st.nextToken();
                    String ic = st.nextToken();
                    String email = st.nextToken();
                    
                    if(idNumber.charAt(0) == 'C'){
                        int orders = Integer.parseInt(st.nextToken());
                        double sales = Double.parseDouble(st.nextToken());
                        employees[i] = 
                        new Cashier(idNumber, password, name, ic, email, 
                                    orders, sales);
                    }else if(idNumber.charAt(0) == 'M')
                        employees[i] = 
                        new Manager(idNumber, password, name, ic, email);   
                }   
            }
            readFile.close();
        }catch(FileNotFoundException e){
            Display.message("File not found!"+"\nError: "+ e,2);
        }catch(IOException ioe){
            Display.message("Failed to read file!"+"\nError: "+ ioe,2);
        } 
        return employees;
    }
    
    public static Cashier[] getAllCashiers(){
        Employee[] employees = readEmployeeFiles();
        Cashier[] cashiers = new Cashier[128];
        
        for(int i = 0, j = 0; i < employees.length; i++){
            if(employees[i] instanceof Cashier){
                cashiers[j] = (Cashier)employees[i];
                j++;
            }
        }
        return cashiers;
    }
    public static Manager[] getAllManagers(){
        Employee[] employees = readEmployeeFiles();
        Manager[] managers = new Manager[128];
        
        for(int i = 0, j = 0; i < employees.length; i++){
            if(employees[i] instanceof Manager){
                managers[j] = (Manager)employees[i];
                j++;
            }
        }
        return managers;
    }
    
    public static void updateCashierData(Cashier[] cashiers){
        Employee[] employees = new Employee[128];
        Manager[] managers = getAllManagers();
        int num = 0;
        
        for(int i = 0; managers[i] != null; i++)
            employees[num++] = managers[i];
        for(int i = 0; cashiers[i] != null; i++)
            employees[num++] = cashiers[i];
            
        writeEmployeeFiles(employees);
    }

    public static void writeEmployeeFiles(Employee[] employee){
        String filepath = getFilepath(4);
        try{
            PrintWriter writeFile = 
            new PrintWriter(new BufferedWriter(new FileWriter(filepath)));
            
            writeFile.println(
            "ID|PASSWORD|NAME|IC NO|EMAIL|SUCESSUL ORDERS|SALE");
            writeFile.println(Display.charBar('=',80));
            
            for(int i = 0; employee[i] != null; i++)
                writeFile.println(employee[i].employeeData());

            writeFile.close();
        }catch(FileNotFoundException e){
            Display.message("File not found!"+"\nError: "+ e,2);
        }catch(IOException ioe){
            Display.message("Failed to write file!"+"\nError: "+ ioe,2);
        }
    }
} 