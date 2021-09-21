/**
 * @author: Antony B. Maceda C.
 */

package com.sales.market.repository;

import com.sales.market.model.ItemInventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemInventoryRepository extends GenericRepository<ItemInventory> {

    @Query("select i from ItemInventory i left join i.item item where item.id=:itemId")
    ItemInventory findItemInventoryByItemId(@Param("itemId") Long itemId);

}
