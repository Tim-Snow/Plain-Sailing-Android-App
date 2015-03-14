package com.tim.plainsailing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeContent {

	public static List<UpgradeItem> ITEMS = new ArrayList<UpgradeItem>();

	public static Map<String, UpgradeItem> ITEM_MAP = new HashMap<String, UpgradeItem>();

	static {
		addItem(new UpgradeItem("0", "Fuel", "Increase the maximum fuel capacity for the ship, each upgrade doubles the amount that can be carried (and start with!)", 5, 500));
		addItem(new UpgradeItem("1", "Boost", "Increase the distance travelled when boosting, while boosting you are able to travel through any obstacle.", 5, 750));
		addItem(new UpgradeItem("2", "Regen Fuel", "When freefalling, the ship will produce fuel.", 5, 250));
		addItem(new UpgradeItem("3", "Highscore Multiplier", "Increases the score and gold earned from each run.", 5, 750));
		addItem(new UpgradeItem("4", "Space Travel", "Allows the ability to travel in to space, a special bonus area with different controls. Can be accessed when the fuel gauge is over 75% by swiping up.", 1, 1000));
	}

	private static void addItem(UpgradeItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	public static class UpgradeItem {
		public String 	id;
		public String 	name;
		public String		info;
		public int			rank;
		public int			cost;

		public UpgradeItem(String id, String name, String info, int rank, int cost) {
			this.id 		= id;
			this.name 	= name;
			this.info 		= info;
			this.rank 	= rank;
			this.cost 	= cost;

		}

		@Override
		public String toString() {
			String retString = name + " Upgrade - Cost: " + cost;
			return retString;
		}
	}
}
