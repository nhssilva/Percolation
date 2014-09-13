public class Percolation 
{
    private static final int BLOCKED = 0;
    private static final int OPEN = 1;
    private WeightedQuickUnionUF quickUnionObject;
    private int N;
    private int TOP_ROW;
    private int BOTTOM_ROW;
    private int IMAGINARY_TOP;
    private int IMAGINARY_BOTTOM;

   // DECLARE AN N-BY-N GRID OF SITE STATES
    private int[][] nnGrid;
    
  public Percolation(int N) 
  {   if (N <= 0) throw new java.lang.IllegalArgumentException("N < 0.");
      this.N = N;
      this.TOP_ROW = 0;
      this.BOTTOM_ROW = N-1;
      this.IMAGINARY_TOP = 0;
      this.IMAGINARY_BOTTOM = (N*N)+1;
      
      // create N-by-N grid, with all sites blocked
      nnGrid = new int[N][N];
      
      for (int i = 0; i < N; i++)
      {   for (int j = 0; j < N; j++)
          {    nnGrid[i][j] = BLOCKED;
          }
      }
      
      quickUnionObject = new WeightedQuickUnionUF((N * N) + 2);  
  }
  
  private int linearizeIndexes(int row, int col) 
  {  return ((row*N) + col) + 1; 
  } 
  
  // THROW AN EXCEPTION IF INDEXES ARE OUT OF BOUNDS
  private void testForValidBoundaries(int row, int col)
  {   if (row < 0) throw new java.lang.IndexOutOfBoundsException("Row < 0.");
      if (row >= N) throw new java.lang.IndexOutOfBoundsException("Row >= N.");
      if (col < 0) throw new java.lang.IndexOutOfBoundsException("Col < 0.");
      if (col >= N) throw new java.lang.IndexOutOfBoundsException("Col >= N.");   
  }
  
  public void open(int i, int j)   
  {   int row = i - 1;  
      int col = j - 1;
      
      // TEST INDEXES FOR VALID RANGE - THROWS EXCEPTION
      testForValidBoundaries(row, col);
      
      // OPEN THE INDEXED SITE
      nnGrid[row][col] = OPEN; 
      
      // TEST TO JOIN TO THE IMAGINARY SITES
      if (row == TOP_ROW) 
      {    quickUnionObject.union(IMAGINARY_TOP, linearizeIndexes(row, col));  
      }  
      else if (row == BOTTOM_ROW)
      {    quickUnionObject.union(IMAGINARY_BOTTOM, linearizeIndexes(row, col));  
      } 
      
      
      // TEST ALL MY NEIGHBORS AND UNIONIZE THEM WITH ME
      if ((col + 1) < N) 
      {   // Test site to the right of me
          if (nnGrid[row][col+1] == OPEN)
          {    quickUnionObject.union(linearizeIndexes(row, col), 
                               linearizeIndexes(row, col+1));
          }
      }  
      if ((col - 1) >= 0) 
      { // Test site to the left of me
          if (nnGrid[row][col-1] == OPEN) { 
                quickUnionObject.union(linearizeIndexes(row, col), 
                                       linearizeIndexes(row, col-1));
          }
      }  
      if ((row + 1) < N) 
      { // Test site below me 
          if (nnGrid[row+1][col] == OPEN) 
          {     quickUnionObject.union(linearizeIndexes(row, col), 
                                        linearizeIndexes(row+1, col));
          }
      }          
      if ((row - 1) >= 0) 
      {  // Test site above me
          if (nnGrid[row-1][col] == OPEN)
          {     quickUnionObject.union(linearizeIndexes(row, col),
                               linearizeIndexes(row-1, col)); 
          }
      }
  }     
   
   public boolean isOpen(int i, int j) {  
        int row = i - 1;  
        int col = j - 1;
        
        // TEST INDEXES FOR VALID RANGE- THROWS EXCEPTION
        testForValidBoundaries(row, col); 
        
        return (nnGrid[row][col] == OPEN);  
   } 
  
   public boolean isFull(int i, int j) 
   {    int row = i - 1;  
        int col = j - 1;
        
        // TEST INDEXES FOR VALID RANGE- THROWS EXCEPTION
        testForValidBoundaries(row, col); 
        
        return quickUnionObject.connected(0, linearizeIndexes(row, col));
   } 
 
   public boolean percolates()     
   {    // does the system percolate? 
       return quickUnionObject.connected(0, (N*N)+1);  
   }    
    
 
}