
import java.util.*;

class Path {
    static int ROW = 100;
    static int COL = 100;

    public static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    };

    static class queueNode {
        Point pt; 
        int dist;

        public queueNode(Point pt, int dist) {
            this.pt = pt;
            this.dist = dist;
        }
    };

    static boolean isValid(int row, int col) {
        return (row >= 0) && (row < ROW) && (col >= 0) && (col < COL);
    }

    static int rowNum[] = { -1, 0, 0, 1 };
    static int colNum[] = { 0, -1, 1, 0 };

    static int ShortestPath(int mat[][], Point src, Point dest) {
        if (mat[src.x][src.y] != 1) {
            System.out.println("Source != 1");
            return -1;
        }
        if (mat[dest.x][dest.y] != 1) {
            System.out.println("Desination != 1");
            return -1;
        }

        boolean[][] visited = new boolean[ROW][COL];

        visited[src.x][src.y] = true;

        Queue<queueNode> q = new LinkedList<>();

        queueNode s = new queueNode(src, 0);
        q.add(s);


        while (!q.isEmpty()) {
            queueNode curr = q.peek();
            Point pt = curr.pt;

            if (pt.x == dest.x && pt.y == dest.y)
                return curr.dist;

            q.remove();

            for (int i = 0; i < 4; i++) {
                int row = pt.x + rowNum[i];
                int col = pt.y + colNum[i];

                if (isValid(row, col) && mat[row][col] == 1 && !visited[row][col]) {
                    visited[row][col] = true;
                    queueNode Adjcell = new queueNode(new Point(row, col), curr.dist + 1);
                    q.add(Adjcell);
                }
                
            }
        }

        return -1;
    }
    
    public static int main() {
        int mat[][] = 
        {{1, 0, 1, 1, 1, 1, 0, 1, 1, 1 }, 
        { 1, 0, 1, 0, 1, 1, 1, 0, 1, 1 }, 
        { 1, 1, 1, 0, 1, 1, 0, 1, 0, 1 }, 
        { 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 }, 
        { 1, 1, 1, 0, 1, 1, 1, 0, 1, 0 },
        { 1, 0, 1, 1, 1, 1, 0, 1, 0, 0 }, 
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
        { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1 }, 
        { 1, 1, 0, 0, 0, 0, 1, 0, 0, 1 }}; 

        Point source = new Point(0, 0);
        Point dest = new Point(4, 6);

        int dist = ShortestPath(mat, source, dest);

        if (dist != Integer.MAX_VALUE) {
            //System.out.println("Shortest Path " + dist);
        }
        else
            System.out.println("Shortest Path doesn't exist");
        return dist;

    }
}