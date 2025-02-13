package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        int arr[]= {3,5,2};

        List<Integer> integerList = new ArrayList<>();


        for (int i =0 ; i <arr.length; i++)



        {

            int temp =1;

            for (int j =0 ; j<arr.length;j++)
           {
               if (arr[i]!=arr[j])
               {

                   temp = temp *arr[j];

               }
           }
            integerList.add(temp);

        }

        for (int a : integerList)
        {
            System.out.println(a + "Done");
        }


    }
}
