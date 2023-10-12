/*
Developed by Volodymyr Havryliuk
Task description:

Implement your task and push to the public git repository. Share the link with the mentor.
Create a simple java class that will read data from the console and will print results to console.
Task 1 (Maximal Rectangle, Matrix Challenge)
Have the function MatrixChallenge(strArr) take the strArr parameter being passed which will be a 2D matrix of 0 and 1's,
and determine the area of the largest rectangular submatrix that contains all 1's.
For example: if strArr is ["10100", "10111", "11111", "10010"] then this looks like the following matrix:

1 0 1 0 0
1 0 1 1 1
1 1 1 1 1
1 0 0 1 0

For the input above, you can see the bolded 1's create the largest rectangular submatrix of size 2x3,
so your program should return the area which is 6. You can assume the input will not be empty.
Examples
Input: ["1011", "0011", "0111", "1111"]
Output: 8

Input: ["101", "111", "001"]
Output: 3


 */

import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

/*
the solution of the problem was based on algorithm that finds the largest rectangular area
possible in a histogram where the largest rectangle can be made of a number of contiguous bars
whose heights are given in an array.In our case, I update the next row with the previous row and find the largest area under the histogram,
i.e. consider each 1’s as filled squares and 0’s with an empty square and consider each row as the base.
 */
public class MaximalRectangle {

    public static void main(String[] args) {
        String[] matrix = matrixInput();//get user input
        int result = MatrixChallenge(matrix);//get result
        System.out.println("result is " + result);//show result of MatrixChallenge function
    }

    /**
     * Method that returns the matrix input
     *
     * @return string array of the matrix
     */
    public static String[] matrixInput() {
        Scanner scanner = new Scanner(System.in);
        //ask the amount of rows in a matrix
        System.out.println("Matrix input:");
        String matrixInput = scanner.nextLine();
        String[] matrix = regExprValidation(matrixInput);
        if (matrix == null) {
            throw new RuntimeException("Incorrect input");
        }
        inputValidation(matrix);

        return matrix;
    }

    public static String[] regExprValidation(String input) {
        String[] matrixResult = null;

        if (input.matches("\\s*\\[[\\w|\\W]*]\\s*")) { //checks whether it is an array input
            input = input.replaceAll("\\[|\\]", "");//replace brackets
            matrixResult = input.split("\\s*,\\s*");//split with commas
            for (int i = 0; i < matrixResult.length; i++) {
                matrixResult[i] = matrixResult[i].replaceAll("\\s*\"\\s*", "");//remove quotation marks
            }
        }

        return matrixResult;
    }

    /**
     * Method that checks whether user input of a matrix row is correct
     *
     * @param matrix array of string that represents the matrix rows
     */
    public static void inputValidation(String[] matrix) {
        int matrixRowLength = matrix[0].length();
        for (String row : matrix) {
            //checks if all rows have the same length
            if (row.length() != matrixRowLength) {
                throw new RuntimeException("Incorrect matrix row length");
            }
            //checks if a string  does not have any other values except 0 and 1
            for (char ch : row.toCharArray()) {
                if (!(ch == '0' || ch == '1')) {
                    throw new RuntimeException("Incorrect input exception");
                }
            }
        }
    }

    /**
     * function that takes the strArr parameter being passed which will be a 2D matrix of 0 and 1's,
     * and determine the area of the largest rectangular submatrix that contains all 1's.
     *
     * @param strArr a string array that represents the matrix of 0's and 1's
     * @return largest rectangular submatrix that contains all 1's
     */
    public static int MatrixChallenge(String[] strArr) {
        int matrixRowLength = strArr[0].length();
        int[] intArr = new int[matrixRowLength];//the matrix row that stores heights of bars
        int maxArea = 0;
        int currentArea;
        // Run through all rows of given matrix
        for (String s : strArr) {
            for (int j = 0; j < matrixRowLength; j++) {
                //updating histogram height if value is not equal 0
                if (s.charAt(j) != '0') {
                    intArr[j] += 1;
                } else {
                    intArr[j] = 0;
                }
            }
            //getting current max area in the matrix
            currentArea = getMaxArea(intArr, intArr.length);
            //comparing to the previous values
            maxArea = Math.max(currentArea, maxArea);
        }
        return maxArea;
    }

    /**
     * function that finds the largest rectangular area possible in a given histogram where the
     * largest rectangle can be made of a number of contiguous bars whose heights are given in an
     * array. For every bar ‘x’, we calculate the area with ‘x’ as the smallest bar in the rectangle.
     * If we calculate the such area for every bar ‘x’ and find the maximum of all areas, our task is
     * done. The soultion is using stack to store bars.
     *
     * @param histogram
     * @param n
     * @return
     */
    static int getMaxArea(int[] histogram, int n) {
        //The stack holds indexes of histogram[] array
        Stack<Integer> s = new Stack<>();
        int max_area = 0;//max area
        int tp;//to store a top of stack
        int area_with_top;
        int i = 0;
        // Run through all bars of given histogram
        while (i < n) {
            // If this bar is higher than the bar on top
            // stack, push it to stack
            if (s.empty() || histogram[s.peek()] <= histogram[i]) {
                s.push(i++);
            }
            // If this bar is lower than top of stack, then
            // calculate area of rectangle with stack top as
            // the smallest (or minimum height) bar.
            else {
                tp = s.pop();
                area_with_top = histogram[tp] * (s.empty() ? i : i - s.peek() - 1);
                // update max area, if needed
                if (max_area < area_with_top) {
                    max_area = area_with_top;
                }
            }
        }

        // Now pop the remaining bars from stack and
        // calculate area with every popped bar as the
        // smallest bar
        while (!s.empty()) {
            tp = s.pop();
            area_with_top = histogram[tp] * (s.empty() ? i : i - s.peek() - 1);

            if (max_area < area_with_top) {
                max_area = area_with_top;
            }
        }

        return max_area;
    }
}
