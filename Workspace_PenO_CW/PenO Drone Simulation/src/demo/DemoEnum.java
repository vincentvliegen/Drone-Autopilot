package demo;

public enum DemoEnum {
		SCAN_SINGLE_OBJECT("Scan"), HIT_MULTIPLE_OBJECT("Fly"), SINGLE_OBSTACLE("Obstacle"), WIND_SHOW("Wind"); 
	
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
			return this == DemoEnum.SCAN_SINGLE_OBJECT;
		}
		
		public boolean getGeneratorWindStatus() {
			return this == DemoEnum.WIND_SHOW;
		}
		
		public boolean getGeneratorEmptyWorldStatus() {
			return this == DemoEnum.SCAN_SINGLE_OBJECT || this == SINGLE_OBSTACLE || this == DemoEnum.WIND_SHOW;
		}
		
		public boolean getCreateSingleObjectStatus() {
			return this == DemoEnum.SCAN_SINGLE_OBJECT || this == SINGLE_OBSTACLE;
		}
		public boolean getTargetStatus() {
			return this == SCAN_SINGLE_OBJECT;
		}
		
		public boolean getGeneratorObstacleStatus() {
			return false; //Vliegen nooit opzettelijk met obstacles?
		}
}
