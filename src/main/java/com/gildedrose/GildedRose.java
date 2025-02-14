package com.gildedrose;

import java.util.Map;
import java.util.function.Function;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {    	
    	// Define the default strategies and value upper bound.
    	Function<Item, Integer> defaultQualityDeltaStrategy = item -> (item.sellIn < 1) ? -2 : -1;
    	Integer defaultQualityMaxValue = 50;
    	Function<Item, Integer> defaultSellInStrategy = item -> -1;
    	
    	// Register item-specific Quality changing strategies.
    	// Each strategy should return only the per-day delta in Quality.
    	Map<String, Function<Item, Integer>> qualityDeltaStrategyRegistry = Map.of(
    			"Aged Brie", item -> (item.sellIn < 1) ? 2 : 1, 
    			"Sulfuras, Hand of Ragnaros", item -> 0, 
    			"Backstage passes to a TAFKAL80ETC concert", item -> {
    		            if (item.sellIn > 10) return 1;
    		            if (item.sellIn > 5) return 2;
    		            if (item.sellIn > 0) return 3;
    		            return Integer.MIN_VALUE;
    		        }, 
				"Conjured Mana Cake", item -> (item.sellIn < 0) ? -4 : -2    										
    			);
    		    	
    	// Register item-specific Quality max values.
    	Map<String, Integer> qualityMaxValueRegistry = Map.of(
    			"Sulfuras, Hand of Ragnaros", 80
    			);

    	// Register item-specific Sell-In changing strategies.
    	// Each strategy should return only the per-day delta in Sell-In.
    	Map<String, Function<Item, Integer>> sellInDeltaStrategyRegistry = Map.of(
    			"Sulfuras, Hand of Ragnaros", item -> 0
    			);
    		
    	// Execute update for each Item object...
        for (Item item : items) {
        	// Update Quality...
        	int qualityMaxValue = qualityMaxValueRegistry
        			.getOrDefault(item.name, defaultQualityMaxValue);
        	int qualityDelta = qualityDeltaStrategyRegistry
        			.getOrDefault(item.name, defaultQualityDeltaStrategy)
					.apply(item);
        	item.quality = Math.max(0, Math.min(qualityMaxValue, item.quality + qualityDelta));
        	
        	// Update Sell-In...
        	int sellInDelta = sellInDeltaStrategyRegistry
        			.getOrDefault(item.name, defaultSellInStrategy)
        			.apply(item);
        	item.sellIn += sellInDelta;        	
        }        	
    }
}