package com.sales.market.vo;

import com.sales.market.model.ItemInstance;
import com.sales.market.model.MovementType;

import java.util.List;

public class ItemInventoryEntryVo {

    List<ItemInstance> list;
    MovementType movementType;

    public List<ItemInstance> getList() {
        return list;
    }

    public void setList(List<ItemInstance> list) {
        this.list = list;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }
}
