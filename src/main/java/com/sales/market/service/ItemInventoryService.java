/**
 * @author: Antony B. Maceda C.
 */

package com.sales.market.service;

import com.sales.market.model.Item;
import com.sales.market.model.ItemInventory;

public interface ItemInventoryService extends GenericService<ItemInventory> {

    ItemInventory findItemInventoryByItemId(Long itemId);

    ItemInventory saveInitialItemInventory(Item item);
}