#include <stdio.h>

void sparse(int m,int n,int matrix[m][n]) {
	for(int i = 0 ; i<m ; i++) {
		for(int j = 0 ; j<n; j++) {
			printf("Enter Element[%d][%d]: ",i,j);
			scanf("%d",&matrix[i][j]);}}}

void tuple(int m,int n, int matrix[m][n],int tuple1[][3]) {
	int i = 0, j = 0, k = 1, c = 0 ;
	while(i<m) {
		while(j<n) {
			if(matrix[i][j]!= 0 ) {
				tuple1[k][0]=i;
				tuple1[k][1]=j;
				tuple1[k][2]=matrix[i][j];
				k++;
				c++;}j++;}
		j = 0;
		i++;}
    tuple1[0][0]=m;
	  tuple1[0][1]=n;
	  tuple1[0][2]=c;}

int rowsize(int m,int n,int matrix[m][n]) {
	int c = 0;
	for(int i = 0; i<m ; i++) {
		for(int j =0; j<n; j++) {
			if(matrix[i][j]!=0) {
				c++;
			}}}
	return c;}

void printSparse(int m,int n,int matrix[m][n]) {
	for(int i = 0 ; i<m ; i++) {
		for(int j = 0 ; j<n; j++) {
			printf("%d\t",matrix[i][j]);
		}printf("\n");
	}printf("\n");}

void printTuple(int k, int tuple[k][3] ) {
	for(int i = 0 ; i<k ; i++) {
		for(int j = 0 ; j<3; j++) {
			printf("%d\t",tuple[i][j]);
		}printf("\n");
	}printf("\n");}

int SumTuple(int n1 , int n2 ,int tuple1[][3],int tuple2[][3],int sum[][3]){
    int i = 1 , j = 1 , ptr = 1 , elem = 0 ;
    while(i<n1 && j<n2){
        if(tuple1[i][0] == tuple2[j][0] && tuple1[i][1] == tuple2[j][1]){
            sum[ptr][0]= tuple1[i][0];
            sum[ptr][1]= tuple1[i][1];
            sum[ptr][2]= tuple1[i][2]+tuple2[j][2];
            i++;j++;ptr++;elem++;
        }else if(tuple1[i][0] == tuple2[j][0]){
            if(tuple1[i][1]<tuple2[j][1]){
                sum[ptr][0]= tuple1[i][0];
                sum[ptr][1]= tuple1[i][1];
                sum[ptr][2]= tuple1[i][2];
                i++;ptr++;elem++;
            }else{
                sum[ptr][0]= tuple2[j][0];
                sum[ptr][1]= tuple2[j][1];
                sum[ptr][2]= tuple2[j][2];
                j++;ptr++;elem++;}
        }else if (tuple1[i][0] < tuple2[j][0]){
            sum[ptr][0]= tuple1[i][0];
            sum[ptr][1]= tuple1[i][1];
            sum[ptr][2]= tuple1[i][2];
            i++;ptr++;elem++; 
        }else{
            sum[ptr][0]= tuple2[j][0];
            sum[ptr][1]= tuple2[j][1];
            sum[ptr][2]= tuple2[j][2];
            j++;ptr++;elem++;}
    }
    while(i<n1){
        sum[ptr][0]= tuple1[i][0];
        sum[ptr][1]= tuple1[i][1];
        sum[ptr][2]= tuple1[i][2];
        i++;ptr++;elem++;}
     while(j<n2){
        sum[ptr][0]= tuple2[j][0];
        sum[ptr][1]= tuple2[j][1];
        sum[ptr][2]= tuple2[j][2];
        j++;ptr++;elem++;}
    sum[0][0]= tuple1[0][0];
    sum[0][1]= tuple1[0][1];
    sum[0][2]= elem;
    return elem+1;}

void trans(int m , int n , int matrix[m][n]){
    int transpose[n][m];
    for(int i = 0 ; i<m ; i++){
        for(int j = 0 ; j < n ; j++){
            transpose[j][i]=matrix[i][j];
        }}    
     printSparse(n,m,transpose);}

void main()
{   
    int r1,c1,r2,c2;
    printf("Enter rows of first matrix");
    scanf("%d",&r1);
    printf("Enter cols of first matrix");
    scanf("%d",&c1);
    printf("Enter rows of second matrix");
    scanf("%d",&r2);
    printf("Enter cols of second matrix");
    scanf("%d",&c2);
	  int a[r1][c1],b[r2][c2];
	  printf("Creating Sparse Matrix I\n");
    sparse(r1,c1,a);
    printf("Creating Sparse Matrix II\n");
	  sparse(r2,c2,b);
	  printf("The first matrix is\n");
	  printSparse(r1,c1,a);
	  printf("The second matrix is\n");
	  printSparse(r2,c2,b);
	  int n1 = rowsize(r1,c1,a) + 1;
	  int n2 = rowsize(r2,c2,b) + 1;
	  int tuple1[n1][3],tuple2[n2][3];
	  tuple(r1,c1,a,tuple1);
	  tuple(r2,c2,b,tuple2);
	  printf("The tuple of first matrix is\n");
	  printTuple(n1,tuple1);
	  printf("The tuple of second matrix is\n");
	  printTuple(n2,tuple2);
	  int n3 = n1+n2;
	  int sum[n3][3];
	  n3 = SumTuple(n1,n2,tuple1,tuple2,sum);
	  printf("The Sum Tuple\n");
	  printTuple(n3,sum);
	  printf("The Transposed Sum Tuple\n");
	  trans(n3,3,sum);
}
