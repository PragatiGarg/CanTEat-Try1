package test.authtry1;

import java.util.List;

/**
 * Created by User on 20-03-2018.
 */

public class Order {

    String generatedOrderId;
    String orderId;
    String userId;
    float totalAmount;
    boolean status;

    List<ItemInOrder> items;

    long timeOfOrder;

    public void Order(){

    }

    public Order(String generatedOrderId, String orderId, String userId, float totalAmount, boolean status, List<ItemInOrder> items, long timeOfOrder) {
        this.generatedOrderId = generatedOrderId;
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
        this.timeOfOrder = timeOfOrder;
    }

    public String getGeneratedOrderId() {
        return generatedOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public boolean isStatus() {
        return status;
    }

    public List<ItemInOrder> getItems() {
        return items;
    }

    public long getTimeOfOrder() {
        return timeOfOrder;
    }
}
