/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market.repository;


import com.sales.market.model.ItemInstance;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemInstanceRepository extends GenericRepository<ItemInstance> {

    @Query("select i.identifier from ItemInstance i")
    List<String> getItemInstancesSkus();
}
