#include <stdio.h>

void display(int n , int top , int s[n]){
    if(top < 0){
         printf("Stack is empty.\n");
    }else{
        for(int i = 0 ; i<=top ; i++){
            printf("%d\t",s[i]);
        } printf("\n");
    }
}

void push(int n , int *top , int s[n]){
    int item ;
    if(*top>=n-1){
        printf("The stack is full , No insertion is possible.\n");
    }else{
        printf("Enter the element you want to push?");
        scanf("%d",&item);
        (*top)++;
        s[*top]=item;
        printf("Item is pushed\n");
    }
         
}

void pop(int n , int *top , int s[n]){
    int item;
    if(*top<0){
        printf("Stack is empty.\n");
    }else{
        item = s[*top];
        (*top)--;
        printf("The item popped : %d\n",item);
    }
}

void insert(int n ,int *top , int s[n]){
    for(int i = 0 ; i<n ; i++){
        printf("Insert Element [%d]",i);
        scanf("%d",&s[i]);
        (*top)++;
    }
    printf("The stack is created successfully \n");
}

void main(){
    int n , ch , top = -1, stop = 0;
    printf("How much elements do you want to add ?");
    scanf("%d",&n);
    int a[n];
    insert(n,&top,a);
    while(1){
        printf("1. PUSH\n");
        printf("2. POP\n");
        printf("3. DISPLAY\n");
        printf("4. EXIT\n");
        printf("Enter your choice ?");
        scanf("%d",&ch);
        switch(ch){
            case 1:
                push(n,&top,a);
                break;
            case 2:
                pop(n,&top,a);
                break;
            case 3:
                display(n,top,a);
                break;
            case 4:
                stop = 1;
                break;
            default:
                printf("Enter a valid choice");
        }
        if(stop == 1){
            break;
        }
    }
    
}
