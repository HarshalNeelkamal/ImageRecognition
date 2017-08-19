
public class LeaveOutGraph {
	
	int k = 0;
	List graph[];
	
	class Node{
		int arr[];
		Node next = null;
		Node(int A[], int B[]){
			arr = new int[k];
			for(int i = 0; i < A.length; i++){
				arr[i] = A[i];
			}
			for(int i = A.length; i < A.length + B.length; i++){
				arr[i] = B[i-A.length];
			}
		}
	}
		
	class List{
		Node root;
		String section = "";
		public List(Node root,String section) {
			this.root = root;
			this.section = section; 
		}
		
		public int[] mean(){
			Node temp = root;
			int count = 0;
			int ret[] = null;
			while(temp != null){
				if(ret == null){
					ret = temp.arr;
				}else{
					for(int i = 0; i< ret.length;i++){
						ret[i] += temp.arr[i]; 
					}
				}
				temp = temp.next;
				count++;
			}
			for(int i = 0; i< ret.length;i++){
				ret[i] /= count; 
			}
			return ret;
		}
		
		public int meanEucledianDist(){
			int arr[] = mean();
			int dist = 0;
			int count = 0;
			Node temp = root;
			while(temp != null){
				dist += (int)eucledianDist(arr,temp.arr);
				temp = temp.next;
				count++;
			}
			return (dist/count);
		}
		
		public double eucledianDist(int arr[], int temp[]){
			int dist = 0;
			for(int i = 0; i< arr.length;i++){
				dist += ((arr[i] - temp[i])*(arr[i] - temp[i]));
			}
			return Math.sqrt(dist);
		}
		
	}
	
	public LeaveOutGraph(int length, int features) {
		graph = new List[length];
		k = features;
		for(int i = 0 ; i < length; i++){
			graph[i] = new List(null, "");
		}
	}
	
	public void add(int A[], int B[], String set){
		Node n = new Node(A, B);
		boolean done = false;
		for(int i = 0 ; i < graph.length; i++){
			if(graph[i].section.equals(set)){
				n.next = graph[i].root;
				graph[i].root = n;
				done = true;
				break;
			}
		}
		if(!done){
			for(int i = 0 ; i < graph.length; i++){
				if(graph[i].section.equals("")){
					graph[i] = new List(n, set);
					done = true;
					break;
				}
			}
		}
	}
	
	public String isInCategory(int A[], int B[]){
		Node n = new Node(A, B);
		String bestSet = "";
		double min = Double.MAX_VALUE;
		boolean found = false;
		for(int i = 0; i < graph.length; i++){
			if(outerEucledianDist(graph[i].mean(), n.arr) < min){
				bestSet = graph[i].section;
				min = outerEucledianDist(graph[i].mean(), n.arr);
			}
			if(graph[i] != null && outerEucledianDist(graph[i].mean(), n.arr) < (graph[i].meanEucledianDist()*1.5)){
				found = true;
			}
		}	
		System.out.println("best pred: "+bestSet);
		if(!found){
			bestSet = "Not a Fruit";
		}
		return " prediction: "+bestSet;
	}
	
	private double outerEucledianDist(int arr[], int temp[]){
		int dist = 0;
		for(int i = 0; i< arr.length;i++){
			dist += ((arr[i] - temp[i])*(arr[i] - temp[i]));
		}
		return Math.sqrt(dist);
	}
	
}
