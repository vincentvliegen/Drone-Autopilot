package demo;

public enum DemoEnum {
		SCAN_LETTERL("Scan"), HIT_MULTIPLE_OBJECT("Fly"), SINGLE_OBSTACLE("Obstacle"), WIND_SHOW("Wind"), SCAN_HOLLOWCUBE("Scan"), HIT_OBJECT_WITH_OBSTACLE("FlyObstacle"), HIT_OBSTACLE_AND_WIND("FlyObstacleWind"); 
	
		private String text;

		private DemoEnum(String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
		
		public boolean getOverrideStatus() {
			// Wou deze is ff uittesten
			//return this == DemoEnum.SCAN_SINGLE_OBJECT? true : false;
			return this.toString().equals("Scan");
		}
		
		public boolean getGeneratorWindStatus() {
			return this == DemoEnum.WIND_SHOW || this == HIT_OBSTACLE_AND_WIND;
		}
		
		public boolean getGeneratorEmptyWorldStatus() {
			return this == DemoEnum.SCAN_LETTERL || this == SINGLE_OBSTACLE || this == DemoEnum.SCAN_HOLLOWCUBE;		}
		
		public boolean getCreateSingleObjectStatus() {
			return this == DemoEnum.SCAN_LETTERL || this == SINGLE_OBSTACLE || this == DemoEnum.SCAN_HOLLOWCUBE;
		}
		
		public boolean getTargetStatus() {
			return this == DemoEnum.SCAN_LETTERL || this == SCAN_HOLLOWCUBE;
		}
		
		public boolean getGeneratorObstacleStatus() {
			return this == HIT_OBJECT_WITH_OBSTACLE || this == HIT_OBSTACLE_AND_WIND; //Vliegen nooit opzettelijk met obstacles?
		}
}
