package com.gildedrose;

import java.util.Map;
import java.util.function.Function;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
    	
    	// Register item-specific quality changing strategies here...
    	Map<String, Function<Item, Integer>> qualityDeltaStrategyRegistry = Map.of(
    			"Aged Brie", item -> (item.sellIn < 1) ? 2 : 1,
    			"Sulfuras, Hand of Ragnaros", item -> 0,
    			"Backstage passes to a TAFKAL80ETC concert", 
    				item -> (item.sellIn > 10) ? 1 : 
    						(item.sellIn > 5) ? 2 : 
							(item.sellIn > 0) ? 3 : Integer.MIN_VALUE,
				"Conjured Mana Cake", item -> (item.sellIn < 0) ? -4 : -2    										
    			);
    		    	
    	// Register item-specific quality max values here... 
    	Map<String, Integer> qualityMaxValueRegistry = Map.of(
    			"Sulfuras, Hand of Ragnaros", 80
    			);
    	

    	// Register item-specific sell-in changing strategies here...
    	Map<String, Function<Item, Integer>> sellInDeltaStrategyRegistry = Map.of(
    			"Sulfuras, Hand of Ragnaros", item -> 0
    			);
    	
    	// Define the default strategies and value bound...
    	Function<Item, Integer> defaultQualityDeltaStrategy = item -> (item.sellIn < 1) ? -2 : -1;
    	Integer defaultQualityMaxValue = 50;
    	Function<Item, Integer> defaultSellInStrategy = item -> -1;
    	
    	// Execute update...
        for (Item item : items) {

        	// Update quality...
        	int qualityMaxValue = qualityMaxValueRegistry
        			.getOrDefault(item.name, defaultQualityMaxValue);
        	int qualityDelta = qualityDeltaStrategyRegistry
        			.getOrDefault(item.name, defaultQualityDeltaStrategy)
					.apply(item);
        	item.quality = Math.max(0, Math.min(qualityMaxValue, item.quality + qualityDelta));
        	
        	// Update sellIn...
        	int sellInDelta = sellInDeltaStrategyRegistry
        			.getOrDefault(item.name, defaultSellInStrategy)
        			.apply(item);
        	item.sellIn += sellInDelta;
        	
        }        	
    }
}