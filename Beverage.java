public class Beverage extends Item{
    private char size;
    private boolean isIced;

    public Beverage(){super();}
    public Beverage(String name, double priceR, double priceL, int sold, 
                    double sales){
        super(name, priceR, priceL, sold, sales);
        this.size = 'R';
        this.isIced = false;
    }
    public Beverage(Beverage b){
        super(b);
        this.size = b.size;
        this.isIced = b.isIced;
    }

    public void setSize(char size){this.size = size;}
    public void setIsIced(char iced){
        if(iced == 'c' || iced == 'C')
            this.isIced = true;
        else
            this.isIced = false;
    }
    
    public char getSize(){return size;}
    public boolean isIced(){return isIced;}
    
    public String strIced(){
        if(isIced) 
            return "Cold";
        else 
            return "Hot";
    }

    public String strItemName(){
        return size + " " + strIced() + " " + this.getItemName();
    }
} 