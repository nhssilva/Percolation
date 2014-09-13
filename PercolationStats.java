public class PercolationStats 
{
    private double[] percolationThreshold; 
    
    private int N;
    private int T;
    private double mean;
    private double standardDeviation;
    private double variance;
    private double rootOfSamples;
        
    public PercolationStats(int gridSize, int reps)
    {               
        if (gridSize < 0)
            throw new java.lang.IllegalArgumentException("Grid Size < 0.");
        if (reps < 0)
            throw new java.lang.IllegalArgumentException("Samples < 0.");
        
        N = gridSize;
        T = reps;
         percolationThreshold = new double[T];
         rootOfSamples = Math.sqrt((double) T); 
    }
    
    private void setPercolationThreshold(int index, double value)
    {    percolationThreshold[index] = value;
    }
    
    // sample mean of percolation threshold
    public double mean()
    {   double meanSum = 0;     
        for (int k = 0; k < T; k++) 
        {    meanSum += percolationThreshold[k];
        }  
        mean = meanSum/(double) T; 
        return mean;
    }                     
    
    // sample standard deviation of percolation threshold
    public double stddev()                   
    {   double diffSums = 0;
        int p;
        for (p = 0; p < T; p++)
        {   double diff;            
            diff = percolationThreshold[ p ] - mean;
            diff = diff*diff; 
            diffSums += diff;
            
        }
        variance = diffSums / ((double) (T-1));
        
        standardDeviation = Math.sqrt(variance);
        return standardDeviation;
    }
    
    public double confidenceLo()
    {   // returns lower bound of the 95% confidence interval
        return (mean - ((1.96*standardDeviation)/rootOfSamples));
    }
    
    public double confidenceHi()             
    {   // returns upper bound of the 95% confidence interval
        return (mean + ((1.96*standardDeviation)/rootOfSamples));
    }

    public static void main(String [] args) 
    {                  
        int gridSize = Integer.parseInt(args[0]);
        int reps = Integer.parseInt(args[1]);
        
        PercolationStats pStats = new PercolationStats(gridSize, reps);
               
        int totalOpened;
        int totalSites = gridSize*gridSize;
        int row; 
        int col;
        for (int i = 0; i < reps; i++) {  
            totalOpened = 0;  
            Percolation perco = new Percolation(gridSize);  
            
            while (!perco.percolates()) {  
                row = StdRandom.uniform(gridSize) + 1;  
                col = StdRandom.uniform(gridSize) + 1;  
                if (!perco.isOpen(row, col)) 
                {   // Site was Blocked, so Open it and increase open count
                    perco.open(row, col);  
                    totalOpened++;  
                }   
            }  

            pStats.setPercolationThreshold(i, 
                            ((double) totalOpened)/((double) totalSites));         
        }       
      
        pStats.mean();
        pStats.stddev();
      
        System.out.println("");
        System.out.println(" grid size               = " + pStats.N);
        System.out.println(" samples                 = " + pStats.T);              
        System.out.println(" mean                    = " + pStats.mean);
        System.out.println(" stddev                  = " 
                               + pStats.standardDeviation);
        System.out.println(" 95% confidence interval = " 
                          + pStats.confidenceLo()+", "+pStats.confidenceHi());
     }

}