
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author YovanJong & Henrico Leodra
 * Reference dari YouTube
 * Judul videonya "8 Puzzle Breadth First search"
 * akun Oguzcan Adabuk dia pake C#
 * link https://www.youtube.com/watch?v=6edibwHBDFk
 */
public class MainBFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] puzzle = new int[9];
        System.out.println("Masukkan initial state:");
        for(int i=0;i<9;i++){
            puzzle[i] = sc.nextInt();
        }
        System.out.println("Finding solution.......");
        Node root = new Node(puzzle);
        UninformedSearch ui = new UninformedSearch();
        
        ArrayList<Node> solution = ui.BreathFirstSearch(root);
        
        int jumlahLangkah = 0;
        if(solution.size()>0){
            for(int i=0;i<solution.size();i++){
                solution.get(i).printPuzzle();
                jumlahLangkah++;
            }
            jumlahLangkah--;
            System.out.println("Total langkah yang dibutuhkan: "+jumlahLangkah);
        }
        else{
            System.out.println("No path to solution is found");
        }
    }
}

class Node{
    public ArrayList<Node> children = new ArrayList<Node>();
    public Node parent;
    public int[] puzzle = new int[9];
    public int x = 0;
    public int col = 3;
    
    
    public Node(int[] p){
        this.setPuzzle(p);
    }
    
    public void setPuzzle(int[] p){
        for(int i=0;i<this.puzzle.length;i++){
            this.puzzle[i] = p[i];
        }
    }
    
    public void expandNode(){
        for(int i=0;i<this.puzzle.length;i++){
            if(this.puzzle[i] == 0){
                this.x = i;
            }
        }
        
        this.moveRight(puzzle, this.x);
        this.moveLeft(puzzle, this.x);
        this.moveUp(puzzle, this.x);
        this.moveDown(puzzle, this.x);
    }
    
    public boolean GoalTest(){
        boolean isGoal = true;
        int m = this.puzzle[0];
        
        for(int i=1;i<this.puzzle.length;i++){
            if(m>this.puzzle[i]){
                isGoal = false;
            }
            m = this.puzzle[i];
        }
        
        return isGoal;
    }
    
    public void moveRight(int[] p, int i){
        if(i%this.col < this.col-1){
            int[] pc = new int[9];
            this.copyPuzzle(pc, p);
            
            int temp = pc[i+1];
            pc[i+1] = pc[i];
            pc[i] = temp;
            
            Node child = new Node(pc);
            this.children.add(child);
            child.parent = this;
        }
    }
    
    public void moveLeft(int[] p, int i){
        if(i%this.col > 0){
            int[] pc = new int[9];
            this.copyPuzzle(pc, p);
            
            int temp = pc[i-1];
            pc[i-1] = pc[i];
            pc[i] = temp;
            
            Node child = new Node(pc);
            this.children.add(child);
            child.parent = this;
        }
    }
    
    public void moveUp(int[] p, int i){
        if(i-this.col >= 0){
            int[] pc = new int[9];
            this.copyPuzzle(pc, p);
            
            int temp = pc[i-3];
            pc[i-3] = pc[i];
            pc[i] = temp;
            
            Node child = new Node(pc);
            this.children.add(child);
            child.parent = this;
        }
    }
    
    public void moveDown(int[] p, int i){
        if(i+this.col < this.puzzle.length){
            int[] pc = new int[9];
            this.copyPuzzle(pc, p);
            
            int temp = pc[i+3];
            pc[i+3] = pc[i];
            pc[i] = temp;
            
            Node child = new Node(pc);
            this.children.add(child);
            child.parent = this;
        }
    }
    
    public void copyPuzzle(int[] a, int[] b){
        for(int i=0;i<b.length;i++){
            a[i] = b[i];
        }
    }
    
    public void printPuzzle(){
        int m = 0;
        for(int i=0;i<this.col;i++){
            for(int j=0;j<this.col;j++){
                System.out.print(this.puzzle[m]+" ");
                m++;
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    public boolean isSamePuzzle(int[] p){
        boolean samePuzzle = true;
        for(int i=0;i<p.length;i++){
            if(this.puzzle[i] != p[i]){
                samePuzzle = false;
            }
        }
        return samePuzzle;
    }
}

class UninformedSearch{
    
    public UninformedSearch(){
    
    }
    
    public ArrayList<Node> BreathFirstSearch(Node root){
        ArrayList<Node> pathToSolution = new ArrayList<Node>();
        ArrayList<Node> openList = new ArrayList<Node>();
        ArrayList<Node> closedList = new ArrayList<Node>();
        
        openList.add(root);
        boolean goalFound = false;
        
        while(openList.size()>0 && !goalFound){
            Node currentNode = openList.get(0);
            closedList.add(currentNode);
            openList.remove(0);
            
            currentNode.expandNode();
            
            for(int i=0;i<currentNode.children.size();i++){
                Node currentChild = currentNode.children.get(i);
                if(currentChild.GoalTest()){
                    System.out.println("Goal Found");
                    goalFound = true;
                    
                    this.pathTrace(pathToSolution, currentChild);
                }
                
                if(!this.contains(openList, currentChild) && !this.contains(closedList,currentChild)){
                    openList.add(currentChild);
                }
            }
        }
        
        return pathToSolution;
    }
    
    public void pathTrace(ArrayList<Node> path, Node n){
        System.out.println("Tracing path....");
        Node current = n;
        path.add(current);
        
        while(current.parent != null){
            current = current.parent;
            path.add(current);
        }
    }
    
    public static boolean contains(ArrayList<Node> list, Node c){
        boolean contains = false;
        
        for(int i=0;i<list.size();i++){
            if(list.get(i).isSamePuzzle(c.puzzle)){
                contains = true;
            }
        }
        
        return contains;
    }
}