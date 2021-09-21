/**
 * @author: Antony B. Maceda C.
 */

package com.sales.market.service;

import com.sales.market.model.ItemInstance;
import com.sales.market.model.ItemInventoryEntry;
import com.sales.market.model.MovementType;

import java.util.List;

public interface ItemInventoryEntryService extends GenericService<ItemInventoryEntry> {

    ItemInventoryEntry registerOperationItemInventoryEntry(List<ItemInstance> itemInstances, MovementType movementType);
}