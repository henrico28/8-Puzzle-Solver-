
import static java.lang.Math.abs;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;


/**
 * @author YovanJong & Henrico
 * Sumber: coding Yohan Kurnia Wijaya
 */
public class MainABintang {
    public static void main(String[] args) {
        int[] currState = {1,9,3,4,2,5,7,8,6};
        int pos=-1;
        for(int i = 0; i<currState.length; i++){
            if(currState[i]==9){
                pos=i;
                break;
            }
        }
        
        Solver s = new Solver(currState, pos);
        s.solve(); 
    }
}

class NodeA implements Comparable<NodeA>{
    NodeA parent, childUp, childDown, childLeft, childRight;
    int[] currState;
    int pos, cost, manhattan;

    NodeA(int[] p, int pos){
        this.currState = p;
        this.pos = pos;
        this.childUp = null;
        this.childDown = null;
        this.childLeft = null;
        this.childRight = null;
        this.cost = 0;
    }
    
    NodeA(int[] p, int pos, int cost, int manhattan){
        this.currState = p;
        this.pos = pos;
        this.childUp = null;
        this.childDown = null;
        this.childLeft = null;
        this.childRight = null;
        this.cost = cost;
        this.manhattan=manhattan;
    }
    
    private int manhattan(int[] p){
        int total=0;
        
        for(int i=0;i<p.length;i++){
            int row = i/3;
            int col = i%3;
            int x = (p[i]-1)%3;
            int y = (p[i]-1)/3;
            total += Math.abs(y-row)+Math.abs(x-col);
        }
        
        return total;
    }
    
    public NodeA moveUp(){
        int[] tempArr = this.currState.clone();
        int newPos = this.pos;
        
        if(newPos-3>=0){
            int temp = tempArr[newPos-3];
            tempArr[newPos-3]=tempArr[newPos];
            tempArr[newPos]=temp;
            newPos-=3;
        }
        
        if(newPos==this.pos){
            newPos=-1;
        }
        int mval = manhattan(tempArr);
        NodeA newNodeA = new NodeA(tempArr, newPos,this.cost+1, mval);
        return newNodeA;
    }
    
    public NodeA moveDown(){
        int[] tempArr = this.currState.clone();
        int newPos = this.pos;
        
        if(newPos+3<=tempArr.length-1){
            int temp = tempArr[newPos+3];
            tempArr[newPos+3]=tempArr[newPos];
            tempArr[newPos]=temp;
            newPos+=3;
        }
        
        if(newPos==this.pos){
            newPos=-1;
        }
        int mval = manhattan(tempArr);
        NodeA newNodeA = new NodeA(tempArr, newPos,this.cost+1, mval);
        return newNodeA;
    }
    
    public NodeA moveRight(){
        int[] tempArr = this.currState.clone();
        int newPos = this.pos;
        
        if(newPos%3<2){
            int temp = tempArr[newPos+1];
            tempArr[newPos+1]=tempArr[newPos];
            tempArr[newPos]=temp;
            newPos+=1; 
        }
        
        if(newPos==this.pos){
            newPos=-1;
        }
        int mval = manhattan(tempArr);
        NodeA newNodeA = new NodeA(tempArr, newPos,this.cost+1, mval);
        return newNodeA;
    }
    
    public NodeA moveLeft(){
        int[] tempArr = this.currState.clone();
        int newPos = this.pos;
        
        if(newPos%3>0){
                   int temp = tempArr[newPos-1];
                   tempArr[newPos-1]=tempArr[newPos];
                   tempArr[newPos]=temp;
                   newPos-=1; 
                }
        
        if(newPos==this.pos){
            newPos=-1;
        }
        int mval = manhattan(tempArr);
        NodeA newNodeA = new NodeA(tempArr, newPos,this.cost+1, mval);
        return newNodeA;
    }
    
    public int compareTo(NodeA s) {
        return (s.manhattan+s.cost > this.manhattan+s.cost)?1:-1;
    }
    
}

class Solver{
    NodeA init;
    PriorityQueue<NodeA> nodes;
    HashMap hm;
    String goal;
    NodeA answer;
    
    public Solver(int[] init, int pos){
        this.init = new NodeA(init, pos);
        this.hm = new HashMap();
        this.nodes = new PriorityQueue<>();
        this.goal = "[1, 2, 3, 4, 5, 6, 7, 8, 9]";
    }
    
    
    private boolean goalCheck(NodeA in){
        return Arrays.toString(in.currState).equals(this.goal);
    }
    
    private boolean goalTest(){
        return this.hm.get(this.goal)!=null;
    }
    
    public void solve(){
        this.nodes.add(init);
        
        while(!this.nodes.isEmpty()){
            NodeA temp = (NodeA) this.nodes.poll();
            expand(temp);
            if(goalTest()){
                break;
            }
        }
        
        NodeA current = this.answer;
        
        int nsteps=-1;
        Stack steps = new Stack();
        
        while(current!=null){
            nsteps++;
            steps.push(Arrays.toString(current.currState));
            current=current.parent;
        }
        
        System.out.println("Solved in: "+nsteps);
        int temp=0;
        while(!steps.isEmpty()){
            System.out.println("Step "+(temp++)+" "+steps.pop());
        }
    }
    
    public void expand(NodeA in){
        NodeA upchild = in.moveUp();
        if(upchild.pos!=-1){
            upchild.parent=in;
            String check = Arrays.toString(upchild.currState);
            if(this.hm.get(check)==null){
                this.hm.put(check,1);
                this.nodes.add(upchild);
                in.childUp=upchild;
                if(goalCheck(upchild)){
                    this.answer=upchild;
                    return;
                }
            }
        }
        NodeA downchild = in.moveDown();
        if(downchild.pos!=-1){
            downchild.parent=in;
            String check = Arrays.toString(downchild.currState);
            if(this.hm.get(check)==null){
                this.hm.put(check,1);
                this.nodes.add(downchild);
                in.childDown=downchild;
                if(goalCheck(downchild)){
                    this.answer=downchild;
                    return;
                }
            }
        }
        NodeA leftchild = in.moveLeft();
        if(leftchild.pos!=-1){
            leftchild.parent=in;
            String check = Arrays.toString(leftchild.currState);
            if(this.hm.get(check)==null){
                this.hm.put(check,1);
                this.nodes.add(leftchild);
                in.childLeft=leftchild;
                if(goalCheck(leftchild)){
                    this.answer=leftchild;
                    return;
                }
            }
        }
        NodeA rightchild = in.moveRight();
        if(rightchild.pos!=-1){
            rightchild.parent=in;
            String check = Arrays.toString(rightchild.currState);
            if(this.hm.get(check)==null){
                this.hm.put(check,1);
                this.nodes.add(rightchild);
                in.childRight=rightchild;
                if(goalCheck(rightchild)){
                    this.answer=rightchild;
                    return;
                }
            }
        }
    }
}
