import java.util.Arrays;

public class ComparisionParas {
		
	class Node{
		int paras[];
		Node right = null;
		Node left = null;
		Node prev = null;
		int lCount = 0;
		int rCount = 0;
		String set = "";
		public Node(int size, String setname){
			paras = new int[size]; 
			set = setname;
		}
	}
	
	private int size = 0;
	private int paraSize = 0;
	Node arr[] = new Node[50];
	
	public ComparisionParas(int paraSize) {
		this.paraSize = paraSize;
	}
	
	private void ensureCapacity(){
		if(size >= arr.length)
			arr = Arrays.copyOf(arr, arr.length*2);
	}
	
	public void Add(int[] para, String setname){
		ensureCapacity();
		Node n = new Node(paraSize, setname);
		n.paras = para;
 		arr[size] = n;
 		size++;
	}
	
	private void moldForKtree(){		
		penMerge(0, size-1, 0,new Node[size]);
	}
	
	private void penMerge(int start, int end, int paraIndex, Node tempNode[]){
		if(start>=end)
			return;
		mergeSort(tempNode, start, end, paraIndex);
		int mid = start + (end-start)/2;
		Node temp = arr[start];
		arr[start] = arr[mid];
		arr[mid] = temp;
		penMerge(start+1,mid,(paraIndex + 1)%paraSize, tempNode);
		penMerge(mid+1,end,(paraIndex + 1)%paraSize, tempNode);
	}
	
	private void mergeSort(Node temp[], int start, int end, int paraIndex){
		if(end <= start){
			return;
		}
		int mid = start + (end - start)/2;
		mergeSort(temp, start, mid, (paraIndex));
		mergeSort(temp, mid + 1, end, (paraIndex));
		merge(temp, start, end, paraIndex);
		
	}
	
	private void merge(Node temp[], int start, int end, int paraIndex){
		int mid = start + (end - start)/2;
		int left = start;
		int right = mid+1; 
		int i = start;
		while(left <= mid && right <= end){
			if(arr[left].paras[paraIndex] < arr[right].paras[paraIndex]){
				temp[i] = arr[left];
				left++;
			}else{
				temp[i] = arr[right];
				right++;
			}
			i++;
		}
		while(left <= mid){
			temp[i] = arr[left];
			left++;
			i++;
		}
		while(right <= end){
			temp[i] = arr[right];
			right++;
			i++;
		}
		
		for(int j = start ; j <= end; j++){
			arr[j] = temp[j];
		}
	}
	
	public Node[] returnCompletedSet(){
		moldForKtree();
		return arr;
	}
//	public void printSet(){
//		for(int i = 0; i < size; i++){
//			System.out.println("("+arr[i].paras[0]+", "+arr[i].paras[1]+", "+arr[i].paras[2]+", "+arr[i].paras[3]+") "+arr[i].set);
//		}
//	}
	
}
