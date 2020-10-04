package Cartography;

/**
 * Class to create a random maze with ones and zeros. Extends the map class.
 * @author Victor Wen, Alec Benton, Aryan Singh
 * 
 */
public class Maze extends Map
{
	/**
	 * Constructor to create a maze object. Calls the map super constructor.
	 * @param width The width of the maze
	 * @param height The height of the maze
	 */
	public Maze(int width, int height) 
	{
		super(width, height, 1);
		randomizeMaze();
	}

	/**
	 * Creates a random maze with ones as walls and zeros as open paths.
	 */
	private void randomizeMaze()
	{
		int xPos = 1;
		int yPos = data.length / 2;

		data[yPos][xPos] = 0;
		
		for(int i = 0; i < data.length*2; i++)
		{
			int shift = (int) ((Math.random() * 2) + 2) * 2;
			int direction = (int) (Math.random() * 4);
			
			if(direction == 0)
			{	
				if(xPos - shift > 0)
				{
					for(int j = 1; j <= shift; j++)
					{
						data[yPos][xPos - j] = 0;
					}

					xPos -= shift;
				}

				else
				{
					for(int j = 1; j <= shift; j++)
					{
						data[yPos][xPos + j] = 0;
					}

					xPos += shift;
				}

			}

			if(direction == 1)
			{	
				if(xPos + shift < data[0].length - 1)
				{
					for(int j = 1; j <= shift; j++)
					{
						data[yPos][xPos + j] = 0;
					}

					xPos += shift;
				}

				else
				{
					for(int j = 1; j <= shift; j++)
					{
						data[yPos][xPos - j] = 0;
					}

					xPos -= shift;
				}

			}

			if(direction == 2)
			{	
				if(yPos - shift > 0)
				{
					for(int j = 1; j <= shift; j++)
					{
						data[yPos - j][xPos] = 0;
					}

					yPos -= shift;
				}

				else
				{
					for(int j = 1; j <= shift; j++)
					{
						data[yPos + j][xPos] = 0;
					}

					yPos += shift;
				}

			}

			if(direction == 3)
			{	
				if(yPos + shift < data.length - 1)
				{
					for(int j = 1; j <= shift; j++)
					{
						data[yPos + j][xPos] = 0;
					}

					yPos += shift;
				}

				else
				{
					for(int j = 1; j <= shift; j++)
					{
						data[yPos - j][xPos] = 0;
					}

					yPos -= shift;
				}
			}
		}
		
		for( int i = 1; i < data.length - 1; i++ )
		{
			for( int j = 1; j < data[0].length - 1; j++ )
			{
				if( checkZeroAround(i, j) )
				{
					data[i][j] = 1;
				}
			}
		}
	}
	
	/**
	 * Method checks to see if a specified point is surrounded by zeros.
	 * @param i First coordinate of the point
	 * @param j Second coordinate of the point
	 * @return Returns true if the point is surrounded by zeros and false if it is not
	 */
	private boolean checkZeroAround( int i, int j )
	{
		boolean around = true;
		
		for( int m = -1; m <= 1; m++ )
		{
			for( int n = -1; n <= 1; n++ )
			{
				if( data[i+m][j+n] != 0 )
				{
					around = false;
				}
			}
		}
		
		return around;
	}
}