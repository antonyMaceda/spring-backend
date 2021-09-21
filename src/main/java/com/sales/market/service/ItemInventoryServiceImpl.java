/**
 * @author: Antony B. Maceda C.
 */

package com.sales.market.service;

import com.sales.market.model.Item;
import com.sales.market.model.ItemInventory;
import com.sales.market.repository.ItemInventoryRepository;
import com.sales.market.repository.GenericRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ItemInventoryServiceImpl extends GenericServiceImpl<ItemInventory> implements ItemInventoryService {
    private final ItemInventoryRepository repository;

    public ItemInventoryServiceImpl(ItemInventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    protected GenericRepository<ItemInventory> getRepository() {
        return repository;
    }

    @Override
    public ItemInventory findItemInventoryByItemId(Long itemId) {
        return repository.findItemInventoryByItemId(itemId);
    }

    @Override
    public ItemInventory saveInitialItemInventory(Item item) {
        ItemInventory itemInventory = new ItemInventory();
        itemInventory.setStockQuantity(BigDecimal.ZERO);
        itemInventory.setLowerBoundThreshold(BigDecimal.ONE);
        itemInventory.setUpperBoundThreshold(BigDecimal.TEN);
        itemInventory.setTotalPrice(BigDecimal.ZERO);
        itemInventory.setItem(item);
        return repository.save(itemInventory);
    }
}
