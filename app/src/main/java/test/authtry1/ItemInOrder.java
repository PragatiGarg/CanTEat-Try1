package test.authtry1;

/**
 * Created by User on 20-03-2018.
 */

public class ItemInOrder {
    String itemId;
    int quantity;

    public void ItemInOrder(){

    }

    public ItemInOrder(String itemId, int quantity) {

        this.itemId = itemId;
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }
}
