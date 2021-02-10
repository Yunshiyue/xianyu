package shopping;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Qingyue
 */
public class ProductItem implements Serializable {
    private final int quantity;
    private final Product product;
    private int selected = 1;
    private Date date;
    private int orderId;
    private String buyer;

    public ProductItem(Product a, int quantity) {
        product = a;
        this.quantity = quantity;
    }

    //购物车
    public ProductItem(Product a, int quantity, int selected) {
        product = a;
        this.quantity = quantity;
        this.selected = selected;
    }

    //订单
    public ProductItem(Product a, int quantity, int selected, Date date, int orderId) {
        product = a;
        this.quantity = quantity;
        this.selected = selected;
        this.date = date;
        this.orderId = orderId;
    }

    //销售情况
    public ProductItem(Product a, int quantity, int selected, Date date, int orderId, String buyer) {
        product = a;
        this.quantity = quantity;
        this.selected = selected;
        this.date = date;
        this.orderId = orderId;
        this.buyer = buyer;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getSelected() { return selected; }

    public Date getDate() { return date; }

    public int getOrderId() { return orderId; }

    public String getBuyer() { return buyer; }
}
