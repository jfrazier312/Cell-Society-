
public class Rectangle extends Cell {

	public Rectangle(int row, int col) {
		super(row, col);
		//do some other rectangular things?
	}

	@Override
	public void render() {
		// yeah
	}

	@Override
	public Shapes getShape() {
		return Shapes.RECTANGLE;
	}

}
