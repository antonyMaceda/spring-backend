/**
 * @author: Antony B. Maceda C.
 */

package com.sales.market.controller;

import com.sales.market.dto.ItemInventoryEntryDto;
import com.sales.market.model.ItemInventoryEntry;
import com.sales.market.service.ItemInventoryEntryService;
import com.sales.market.service.GenericService;
import com.sales.market.vo.ItemInventoryEntryVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/iteminventoryentries")
public class ItemInventoryEntryController extends GenericController<ItemInventoryEntry, ItemInventoryEntryDto> {
    private ItemInventoryEntryService service;

    public ItemInventoryEntryController(ItemInventoryEntryService service) {
        this.service = service;
    }

    @Override
    protected GenericService getService() {
        return service;
    }

    @PostMapping("/entry")
    public ResponseEntity<?> registerNewEntry (@RequestBody ItemInventoryEntryVo itemInventoryEntryVo) {
        ItemInventoryEntry itemInventoryEntry = service.registerOperationItemInventoryEntry(itemInventoryEntryVo.getList(), itemInventoryEntryVo.getMovementType());
        return new ResponseEntity<ItemInventoryEntryDto>(toDto(itemInventoryEntry), HttpStatus.OK);
    }
}