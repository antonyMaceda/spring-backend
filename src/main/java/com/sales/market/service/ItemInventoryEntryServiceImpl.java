/**
 * @author: Antony B. Maceda C.
 */

package com.sales.market.service;

import com.sales.market.model.ItemInstance;
import com.sales.market.model.ItemInventory;
import com.sales.market.model.ItemInventoryEntry;
import com.sales.market.model.MovementType;
import com.sales.market.repository.ItemInventoryEntryRepository;
import com.sales.market.repository.GenericRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemInventoryEntryServiceImpl extends GenericServiceImpl<ItemInventoryEntry> implements ItemInventoryEntryService {
    private final ItemInventoryEntryRepository repository;
    private final ItemInstanceService itemInstanceService;
    private final ItemInventoryService itemInventoryService;

    public ItemInventoryEntryServiceImpl(ItemInventoryEntryRepository repository, ItemInstanceService itemInstanceService, ItemInventoryService itemInventoryService) {
        this.repository = repository;
        this.itemInstanceService = itemInstanceService;
        this.itemInventoryService = itemInventoryService;
    }

    @Override
    protected GenericRepository<ItemInventoryEntry> getRepository() {
        return repository;
    }

    @Override
    public ItemInventoryEntry registerOperationItemInventoryEntry(List<ItemInstance> itemInstances, MovementType movementType) {
        ItemInventoryEntry itemInventoryEntry = new ItemInventoryEntry();
        String itemInstanceSkus = "";
        List<String> skus = new ArrayList<>();
        itemInstances.forEach(item -> skus.add(item.getIdentifier()));
        if (movementType == MovementType.BUY) {
            if (!itemInstanceService.verifyItemsToBeRegisteredAreNotDuplicated(skus)) {
                throw new DuplicateKeyException("Hay items duplicados en la lista de items ingresada para la compra");
            }
        }
        for (int i =0; i < itemInstances.size(); i++) {
            if (itemInstanceSkus.contains(itemInstances.get(i).getIdentifier())) {
                throw new DuplicateKeyException("El item con el identificador " + itemInstances.get(i).getIdentifier() + " ya fue ingresado.\n La operación fue cancelada, revise los datos de los items registrados y vuelva a ingresarlos");
            }
            if (i == (itemInstances.size()-1)) {
                itemInstanceSkus = itemInstanceSkus.concat(itemInstances.get(i).getIdentifier());
            } else {
                itemInstanceSkus = itemInstanceSkus.concat(itemInstances.get(i).getIdentifier()).concat("\n");
            }
        }
        itemInstances.forEach(item -> itemInstanceService.saveItemInstance(item, movementType));

        ItemInventory itemInventory = itemInventoryService.findItemInventoryByItemId(itemInstances.get(0).getItem().getId());
        itemInventoryEntry.setItemInstanceSkus(itemInstanceSkus);
        itemInventoryEntry.setItemInventory(itemInventory);
        itemInventoryEntry.setMovementType(movementType);
        itemInventoryEntry.setQuantity(new BigDecimal(itemInstances.size()));
        return repository.save(itemInventoryEntry);
    }

}
