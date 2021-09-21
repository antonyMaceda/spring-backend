/**
 * @author: Antony B. Maceda C.
 */

package com.sales.market.dto;

import com.sales.market.model.ItemInventoryEntry;
import com.sales.market.model.MovementType;

import java.math.BigDecimal;

public class ItemInventoryEntryDto extends DtoBase<ItemInventoryEntry> {

    private Long itemInventoryId;
    private MovementType movementType;
    private BigDecimal quantity; // represent sale or buy instances quantity
    private String itemInstanceSkus; //represents a list of the sku of the involved item instances

    public Long getItemInventoryId() {
        return itemInventoryId;
    }

    public void setItemInventoryId(Long itemInventoryId) {
        this.itemInventoryId = itemInventoryId;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getItemInstanceSkus() {
        return itemInstanceSkus;
    }

    public void setItemInstanceSkus(String itemInstanceSkus) {
        this.itemInstanceSkus = itemInstanceSkus;
    }
}
