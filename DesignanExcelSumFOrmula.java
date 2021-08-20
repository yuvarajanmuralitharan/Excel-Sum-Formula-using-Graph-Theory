package Leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

//If a cell have the formula like c3 = c3+1 
// then it is a circular formula  which 
// is never end so each cell does not have
// its name as child.


// the formulat does not have cycle
// like a3 is child  of b3 and b3 is a child of c3 and c3 is a  child of  a3


// it is possisble that  one parent have more one child
// and one child have more parents

// it  is like graph but without cycle


class Excel 

{
	class Cell 
	{
        int value;
        Map<String, Integer> children;
        
        Cell(int value, Map<String, Integer> children) 
        {
            this.value = value;
            this.children = children;
        }
        
    }

    public Cell[][] form;
    public Stack<int[]> stack = new Stack<>();
    
    public Excel(int H, char W)
    
    {
        form = new Cell[H][W - 'A' + 1];
    }
    
    public void set(int r, char c, int v)
    {
        form[r - 1][c - 'A'] = new Cell(v, new HashMap<String,Integer>());
        topologicalSort(r - 1, c - 'A');
        sumStack();
    }
    
    public int get(int r, char c) 
    {
        if (form[r - 1][c - 'A'] == null) {
            return 0;
        }
        return form[r - 1][c - 'A'].value;
    }
    
    public int sum(int r, char c, String[] strs) 
    {
        Map<String, Integer> range = convertToMap(strs);
        int sum = getSum(r - 1, c - 'A', range);
        set(r, c, sum);
        form[r - 1][c - 'A'] = new Cell(sum, range);
        
        return sum;
    
    }
    
    private void topologicalSort(int row, int col) 
    {
        for (int i = 0; i < form.length; i++)
        {
            for (int j = 0; j < form[0].length; j++) 
            {
                if (form[i][j] != null && form[i][j].children.containsKey("" + (char)('A' + col) + (row + 1)))
                {
                    topologicalSort(i, j);
                }
            }
        }
        stack.push(new int[]{ row, col });
    }
    
    private void sumStack() 
    {
        while (!stack.isEmpty()) 
        {
            
        	int[] c = stack.pop();
            Cell cell = form[c[0]][c[1]];
            if (cell.children.size() > 0)
            {
                getSum(c[0], c[1], cell.children);
            }
            
        }
    }
    
    private int getSum(int row, int col, Map<String, Integer> children)
    {
        int sum = 0;        
        for (String s : children.keySet())
        {
            int i = Integer.parseInt(s.substring(1)) - 1;
            int j = s.charAt(0) - 'A';
            sum += (form[i][j] == null ? 0 : form[i][j].value) * children.get(s);
        }
        form[row][col] = new Cell(sum, children);
        
        return sum;
    }
    
    private Map<String, Integer> convertToMap(String[] strs) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : strs) 
        {
            if (s.indexOf(":") < 0) 
            {
                map.put(s, map.getOrDefault(s, 0) + 1);
            } 
            else 
            {
                String[] range = s.split(":");
                int i1 = Integer.parseInt(range[0].substring(1));
                int i2 = Integer.parseInt(range[1].substring(1));
                char j1 = range[0].charAt(0);
                char j2 = range[1].charAt(0);
                
                for (int i = i1; i <= i2; i++) {
                    for (char j = j1; j <= j2; j++) {
                        String key = "" + j + i;
                        map.put(key, map.getOrDefault(key, 0) + 1);
                    }
                }
            }
        }
        return map;
    }
}


public class DesignanExcelSumFOrmula 
{
	
	public static void main(String args[])
	{
	Excel excel = new Excel(3,'C');
	
	excel.set(1, 'A', 2);
	
	String[] x = {"A1", "A1:B2"};
	System.out.println(excel.sum(3,'C',x));
	
	excel.set(2, 'B', 2);
	
	for(int i=0;i<excel.form.length;i++)
	{
		for(int j=0;j<excel.form[0].length;j++)
		{
			if(excel.form[i][j]==null)
				System.out.print(0 + " ");
			else
				System.out.print(excel.form[i][j].value + " ");
		}
		System.out.println();
	}
	}
}

	