package hu.ait.shoppinglist.data;

import io.realm.RealmObject;
import hu.ait.shoppinglist.R;
import io.realm.annotations.PrimaryKey;

/**
 * Created by chaelimseo on 4/11/17.
 */

public class Item extends RealmObject {


    public enum ItemType{

        SKIN(0, R.drawable.skin),
        EYES(1, R.drawable.shadow),
        LIP(2, R.drawable.redlip),
        NAIL(3, R.drawable.nail),
        HAIR(4, R.drawable.hair);


        private int value;
        private int id;

        private ItemType(int value, int id){
            this.value = value;
            this.id = id;
        }

        public int getValue(){
            return value;
        }

        public int getId(){
            return id;
        }

        public static ItemType fromInt(int value){
            for (ItemType i: ItemType.values()){
                if (i.value == value){
                    return i;
                }
            }
            return SKIN;
        }
    }

    @PrimaryKey
    private String itemID;

    private String itemName;
    private String description;
    private String itemPrice;
    private boolean isBuy;
    private int itemType;

    public Item(){

    }

    public Item(String itemName, String description, String itemPrice, boolean isBuy, int itemType){

        this.itemName = itemName;
        this.description = description;
        this.itemPrice = itemPrice;
        this.isBuy = isBuy;
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public boolean isPurchased() {
        return isBuy;
    }

    public void setPurchased(boolean isBuy) {

        this.isBuy = isBuy;
    }

    public void setItemType(int itemType){
        this.itemType = itemType;
    }

    public ItemType getItemType(){
        return ItemType.fromInt(itemType);
    }

    public String getItemID(){
        return itemID;
    }
}
