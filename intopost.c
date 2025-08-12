#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

#define MAX 100


char stack[MAX];
int top = -1;

void push(char c) {
    if (top >= MAX - 1) {
        printf("Stack overflow!\n");
        exit(1);
    }
    stack[++top] = c;
}

char pop() {
    if (top == -1) {
        printf("Stack underflow!\n");
        exit(1);
    }
    return stack[top--];
}

char topElement() {
    if (top == -1) {
        return '\0'; 
    }
    return stack[top];
}

int precedence(char op) {
    if (op == '^') return 3;
    if (op == '*' || op == '/') return 2;
    if (op == '+' || op == '-') return 1;
    return 0;
}

void infixToPostfix(char* infix, char* postfix) {
    top = -1; 

    int i = 0, k = 0;
    char ch;

    while ((ch = infix[i++]) != '\0') {
        if (isdigit(ch) || (ch >= 'A' && ch <= 'Z')) {
            postfix[k++] = ch;
        } else if (ch == '(') {
            push(ch);
        } else if (ch == ')') {
            while (top != -1 && topElement() != '(') {
                postfix[k++] = pop();
            }
            if (top == -1) {
                printf("Mismatched parentheses detected: no matching '('\n");
                exit(1);
            }
            pop(); 
        } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^') {
            while (top != -1 && topElement() != '(' && precedence(topElement()) >= precedence(ch)) {
                postfix[k++] = pop();
            }
            push(ch);
        } else {
            printf("Invalid character detected: %c\n", ch);
            exit(1);
        }
    }

    while (top != -1) {
        if (topElement() == '(') {
            printf("Mismatched parentheses detected: extra '('\n");
            exit(1);
        }
        postfix[k++] = pop();
    }

    postfix[k] = '\0';
}

int intStack[MAX];
int topInt = -1;

void pushInt(int val) {
    if (topInt >= MAX - 1) {
        printf("Integer stack overflow!\n");
        exit(1);
    }
    intStack[++topInt] = val;
}

int popInt() {
    if (topInt == -1) {
        printf("Integer stack underflow!\n");
        exit(1);
    }
    return intStack[topInt--];
}

int power(int base, int exp) {
    int result = 1;
    for (int i = 0; i < exp; i++) {
        result *= base;
    }
    return result;
}

int operandValue(char ch) {
    if (isdigit(ch)) {
        return ch - '0';
    } else if (ch >= 'A' && ch <= 'Z') {
        return ch - 'A' + 1;
    } else {
        printf("Invalid operand during evaluation: %c\n", ch);
        exit(1);
    }
}

 

int main() {
    char infix[MAX], postfix[MAX];

    printf("Enter infix expression:");
    scanf("%s", infix);

    infixToPostfix(infix, postfix);

    printf("Postfix expression: %s\n", postfix);

   
    return 0;
}

