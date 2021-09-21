/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market.service;

import com.sales.market.model.ItemInstance;
import com.sales.market.model.MovementType;

import java.util.List;

public interface ItemInstanceService extends GenericService<ItemInstance> {

    ItemInstance saveItemInstance (ItemInstance itemInstance, MovementType movementType);

    boolean verifyItemsToBeRegisteredAreNotDuplicated (List<String> itemInstances);

    boolean notifySupervisorStockLowerThanMininumAllowed (String itemName);
}
