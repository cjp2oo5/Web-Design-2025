import java.util.Scanner;
class Matrix{
    void inputElements(int matrix[][],int m,int n){
        Scanner sc = new Scanner(System.in);
        for(int i = 0 ; i<m ; i++){
            for(int j = 0 ; j<n ; j++){
             System.out.println("Enter Element["+i+"]["+j+"]");
             matrix[i][j] = sc.nextInt();
            }}
        System.out.println("The matrix is created successfully");
    }
    
    void printElements(int matrix[][],int m,int n){
            for(int i = 0 ; i<m ; i++){
            for(int j = 0 ; j<n ; j++){
             System.out.print(matrix[i][j]);
             System.out.print("\t");
            } System.out.print("\n");}
    }
    
    void transpose(int matrix[][],int trans[][],int m,int n){
            for(int i = 0 ; i<m ; i++){
            for(int j = 0 ; j<n ; j++){
                trans[j][i]=matrix[i][j];
            }}System.out.println("The matrix is transposed");
    }
}
class Main {
    public static void main(String[] args) {
        int m,n;
		Scanner sq = new Scanner(System.in);
		System.out.println("Enter No of ROWS:");
		m = sq.nextInt();
		System.out.println("Enter No of COLS:");
		n = sq.nextInt();
		int [][]matrix = new int[m][n];
		int [][]trans = new int[n][m];
		Matrix tr = new Matrix();
		tr.inputElements(matrix,m,n);
		tr.printElements(matrix,m,n);
		tr.transpose(matrix,trans,m,n);
		tr.printElements(trans,n,m);
    }
}