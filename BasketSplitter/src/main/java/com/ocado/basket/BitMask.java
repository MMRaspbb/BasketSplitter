package com.ocado.basket;

public class BitMask {
    private int length;
    private boolean[] number;
    public BitMask(int length){
        this.length = length;
        this.number = new boolean[length];
    }
    public void increment(){
        for(int i = length - 1; i >=0 ; i--){
            if(number[i]){
                number[i] = false;
            } else{
                number[i] = true;
                break;
            }
        }
    }

    public boolean[] getNumber() {
        return number;
    }
    public int sumBinaryNumber(){
        int sum = 0;
        for(boolean bit: number){
            if(bit){
                sum+=1;
            }
        }
        return sum;
    }
}
