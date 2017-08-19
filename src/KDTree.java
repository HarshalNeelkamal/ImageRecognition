import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

public class KDTree {
	ComparisionParas.Node root = null;
	int parameters = 4;
	
	public KDTree() {

	}
	
	public void add(ComparisionParas.Node n){
		root = InternalAdd(root, n, 0);
	}
	
	public boolean isEmpty(){
		return root == null;
	}
	
	private ComparisionParas.Node InternalAdd(ComparisionParas.Node root, ComparisionParas.Node n, int depth){
		if(root == null){
			root = n;
			return root;
		}
		if(n.paras[depth%parameters] < root.paras[depth%parameters]){
			root.left = InternalAdd(root.left, n, depth+1);
			root.lCount++;
			root.left.prev = root;
		}else{
			root.right = InternalAdd(root.right, n, depth+1);
			root.right.prev = root;
			root.rCount++;
		}
		return root;	
	}
	
	public HashMap<String,Integer> nearestNeighoubrsFor(int paras[]){
		ComparisionParas.Node subSection = search(root, paras, 0);
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		
		Queue<ComparisionParas.Node> Q = new LinkedList<ComparisionParas.Node>();
		Q = sortedNeighoubrsCollection(paras, subSection, Q);
		int size = Q.size();
		for(int i = 0; i< size; i++){
			ComparisionParas.Node n = Q.remove();
			if(map.containsKey(n.set)){
				map.put(n.set, map.get(n.set) + 1);
			}else{
				map.put(n.set, 1);
			}
			if(i == 3)
				break;
		}
			
		return map;
	}
	
	private Queue<ComparisionParas.Node> sortedNeighoubrsCollection(int paras[], ComparisionParas.Node node, Queue<ComparisionParas.Node> q){
		if(node == null)
			return q;
		int size = q.size();
		ComparisionParas.Node n = node;
		while(size > 0){
			if(eucledianDistance(paras, n.paras) < eucledianDistance(paras, q.peek().paras)){
				q.add(n);
				n = (q.remove());
			}else{
				q.add(q.remove());
			}
			size--;
		}
		q.add(n);
		q = sortedNeighoubrsCollection(paras, node.left, q);
		q = sortedNeighoubrsCollection(paras, node.right, q);
		return q;
	}
	
	public TreeMap<Double, String> newAddNeighoubr(TreeMap<Double, String> map,ComparisionParas.Node n, int paras[]){
		if(n == null)
			return map;
		double dist = eucledianDistance(paras, n.paras);
		map.put(dist, n.set);
		map = newAddNeighoubr(map, n.left, paras);
		map = newAddNeighoubr(map, n.left, paras);
		return map;
	}
	
	public double eucledianDistance(int data1[],int data2[]){
		double sum = 0;
		for(int i = 0; i < data1.length; i++){
			sum += (data1[i] - data2[i])*(data1[i] - data2[i]);
		}
		return Math.sqrt(sum);
	}
	
	public HashMap<String,Integer> addNeighoubrs(HashMap<String,Integer> map, ComparisionParas.Node n){
		if(n == null)
			return map;
		if(map.containsKey(n.set)){
			int data = map.get(n.set);
			map.put(n.set, ++data);
		}else{
			map.put(n.set, 1);
		}
		map = addNeighoubrs(map, n.left);
		map = addNeighoubrs(map, n.right);
		return map;
	}
	
	private ComparisionParas.Node search(ComparisionParas.Node root, int dataPara[], int depth){
		if(root == null)
			return null;
		if(dataPara[depth%parameters] < root.paras[depth%parameters]){
			if(root.left != null && root.lCount < 4)
				return root;
			else if(root.left == null && root.rCount < 4)
				return root;
			else if(root.left != null)				
				return search(root.left, dataPara, depth + 1);
			return search(root.right, dataPara, depth + 1);
		}else{
			if(root.right != null && root.rCount < 4)
				return root;
			else if(root.right == null && root.lCount < 4)
				return root;
			else if(root.right != null)				
				return search(root.right, dataPara, depth + 1);
			return search(root.left, dataPara, depth + 1);
		}
	}
	
	public void clear(){
		if(root.left != null)
			root.left.prev = null;
		if(root.right != null)
			root.right.prev = null;
		root = null;
	}
	
}
